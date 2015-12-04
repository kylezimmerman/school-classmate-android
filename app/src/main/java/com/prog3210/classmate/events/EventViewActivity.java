package com.prog3210.classmate.events;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import com.prog3210.classmate.R;
import com.prog3210.classmate.comments.CommentAdapter;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;

import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.comments.Comment;
import com.prog3210.classmate.comments.CommentDialog;


import java.util.List;

public class EventViewActivity extends BaseAuthenticatedActivity implements CommentDialog.CommentDialogListener{
    private Event event = null;
    private CommentAdapter commentAdapter;

    private Button upvoteButton;
    private Button downvoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        Intent k = getIntent();

        final String eventId = k.getStringExtra("event_id");
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.loading_spinner);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_comment_button);
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event object, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    event = object;
                    displayEventInfo(object);
                    loadComments();
                } else {
                    Toast.makeText(EventViewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO pop up a sweet dialogue
                FragmentManager manager = getSupportFragmentManager();
                CommentDialog commentDialog = new CommentDialog();

                commentDialog.show(manager, "dialog");
            }
        });
    }

    private void displayEventInfo(final Event event) {
        TextView eventName = (TextView)findViewById(R.id.event_name);
        eventName.setText(event.getName());

        TextView eventCourseCode = (TextView)findViewById(R.id.event_course_code);
        eventCourseCode.setText(event.getCourse().getCourseCode());

        TextView eventCourseName = (TextView)findViewById(R.id.event_course_name);
        eventCourseName.setText(event.getCourse().getName());

        TextView eventDueDate = (TextView)findViewById(R.id.event_due_date);
        eventDueDate.setText(event.getDateString());

        TextView eventFinalGradeWeight = (TextView)findViewById(R.id.event_final_grade_weight);
        eventFinalGradeWeight.setText(String.format("%s%%", event.getFinalGradeWeight()));

        TextView eventDescription = (TextView)findViewById(R.id.event_description);
        eventDescription.setText(event.getDescription());

        upvoteButton = (Button)findViewById(R.id.event_upvote_button);
        DrawableCompat.setTint(DrawableCompat.wrap(upvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
        upvoteButton.setText(String.valueOf(event.getUpvotes()));

        downvoteButton = (Button)findViewById(R.id.event_downvote_button);
        DrawableCompat.setTint(DrawableCompat.wrap(downvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
        downvoteButton.setText(String.valueOf(event.getDownvotes()));

        event.hasUpvoted(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    if (count == 1) {
                        DrawableCompat.setTint(DrawableCompat.wrap(upvoteButton.getBackground()).mutate(), getResources().getColor(R.color.upvote_color));
                    }
                } else {
                    Toast.makeText(EventViewActivity.this, "Could not place vote. Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });

        event.hasDownvoted(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    if (count == 1) {
                        DrawableCompat.setTint(DrawableCompat.wrap(downvoteButton.getBackground()).mutate(), getResources().getColor(R.color.downvote_color));
                    }
                } else {
                    Toast.makeText(EventViewActivity.this, "Could not place vote. Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvote(v);
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downvote(v);
            }
        });
    }

    private void loadComments(){
        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.pull_to_refresh);
        commentAdapter = new CommentAdapter(this, event);
        ListView commentList = (ListView) findViewById(R.id.comment_list);

        commentAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Comment>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Comment> list, Exception e) {
                pullToRefresh.setRefreshing(false);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentAdapter.loadObjects();
            }
        });

        commentList.setEmptyView(findViewById(R.id.empty_list_view));

        commentList.setAdapter(commentAdapter);
    }

    private void setVoteButtons(int voteResult) {
        if (voteResult == VoteCallback.UPVOTE) {
            DrawableCompat.setTint(DrawableCompat.wrap(upvoteButton.getBackground()).mutate(), getResources().getColor(R.color.upvote_color));
            DrawableCompat.setTint(DrawableCompat.wrap(downvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
        } else if (voteResult == VoteCallback.NEUTRAL) {
            DrawableCompat.setTint(DrawableCompat.wrap(upvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
            DrawableCompat.setTint(DrawableCompat.wrap(downvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
        } else if (voteResult == VoteCallback.DOWNVOTE) {
            DrawableCompat.setTint(DrawableCompat.wrap(upvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
            DrawableCompat.setTint(DrawableCompat.wrap(downvoteButton.getBackground()).mutate(), getResources().getColor(R.color.downvote_color));
        }
    }

    public void upvote(View v) {
        event.upvote(new VoteCallback() {
            @Override
            public void done(int voteResult, ParseException e) {
                if (e == null) {
                    setVoteButtons(voteResult);

                    upvoteButton.setText(String.valueOf(event.getUpvotes()));
                    downvoteButton.setText(String.valueOf(event.getDownvotes()));
                }
            }
        });
    }

    public void downvote(View v) {
        event.downvote(new VoteCallback() {
            @Override
            public void done(int voteResult, ParseException e) {
                if (e == null) {
                    setVoteButtons(voteResult);

                    upvoteButton.setText(String.valueOf(event.getUpvotes()));
                    downvoteButton.setText(String.valueOf(event.getDownvotes()));
                }
            }
        });
    }

    @Override
    public void onDialogSubmitClick(String commentBody) {
        final Comment comment = new Comment();
        comment.setCommentBody(commentBody);
        comment.setCommentEvent(event);
        comment.setCreator(ClassmateUser.getCurrentUser());

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    //TODO refresh the darn list ?? show a toast with a success message
                    commentAdapter.loadObjects();
                    Toast.makeText(EventViewActivity.this,"Comment added succesfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Show a show a toasst as to why the message was saved
                }
            }
        });
    }

}
