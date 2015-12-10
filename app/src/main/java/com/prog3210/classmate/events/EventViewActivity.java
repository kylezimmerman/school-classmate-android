/*
    EventViewActivity.java

    This class handles the actions and display of the 'Event Details' view.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;
import com.prog3210.classmate.comments.Comment;
import com.prog3210.classmate.comments.CommentAdapter;
import com.prog3210.classmate.comments.CommentDialog;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.ClassmateUser;

import java.util.List;

public class EventViewActivity extends BaseAuthenticatedActivity implements CommentDialog.CommentDialogListener{
    private Event event = null;
    private CommentAdapter commentAdapter;

    private Button upvoteButton;
    private Button downvoteButton;

    private ListView commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_view);

            // Gets the sending intent containing the Event ID to be viewed
            Intent sendingIntent = getIntent();

            // Gets the Event ID to be viewed and starts the ProgressBar display
            final String eventId = sendingIntent.getStringExtra("event_id");
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.VISIBLE);

            // Gets the Event for displaying event information
            ParseQuery<Event> query = Event.getQuery();
            query.include("course");
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
        } catch (Exception e) {
            LogHelper.logError(this, "EventViewActivity", "Error displaying Event.", e.getMessage());
        }
    }

    // Sets the displayed information for the desired Event
    private void displayEventInfo(final Event event) {
        try {
            TextView eventName = (TextView) findViewById(R.id.event_name);
            eventName.setText(event.getName());

            TextView eventCourseCode = (TextView) findViewById(R.id.event_course_code);
            eventCourseCode.setText(event.getCourse().getCourseCode());

            TextView eventCourseName = (TextView) findViewById(R.id.event_course_name);
            eventCourseName.setText(event.getCourse().getName());

            TextView eventDueDate = (TextView) findViewById(R.id.event_due_date);
            eventDueDate.setText(event.getDateString());

            TextView eventFinalGradeWeight = (TextView) findViewById(R.id.event_final_grade_weight);
            eventFinalGradeWeight.setText(String.format("%s%%", event.getFinalGradeWeight()));

            TextView eventDescription = (TextView) findViewById(R.id.event_description);
            eventDescription.setText(event.getDescription());

            upvoteButton = (Button) findViewById(R.id.event_upvote_button);
            DrawableCompat.setTint(DrawableCompat.wrap(upvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
            upvoteButton.setText(String.valueOf(event.getUpvotes()));

            downvoteButton = (Button) findViewById(R.id.event_downvote_button);
            DrawableCompat.setTint(DrawableCompat.wrap(downvoteButton.getBackground()).mutate(), getResources().getColor(android.R.color.darker_gray));
            downvoteButton.setText(String.valueOf(event.getDownvotes()));

            // Manages the case for the user having upvoted the Event being viewed
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

            // Manages the case for the user having downvoted the Event being viewed
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

            //Sets OnClickListeners for the voting buttons
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
        } catch (Exception e) {
            LogHelper.logError(this, "EventViewActivity", "Error displaying Event information.", e.getMessage());
        }
    }

    // Method to load the comments for the Event being viewed
    private void loadComments(){
        try {
            final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
            commentList = (ListView) findViewById(R.id.comment_list);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_comment_button);
            commentAdapter = new CommentAdapter(this, event);

            // Sets OnClickListener for the FAB to leave a comment on the Event being viewed
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager manager = getSupportFragmentManager();
                    CommentDialog commentDialog = new CommentDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("eventName", event.getName());
                    commentDialog.setArguments(bundle);
                    commentDialog.show(manager, "dialog");
                }
            });

            // Allows pull-to-refresh action on Comments list for Event being viewed
            commentAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Comment>() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(List<Comment> list, Exception e) {
                    pullToRefresh.setRefreshing(false);
                }
            });

            // Sets the OnRefreshListener to regenerate the Comment list view
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    commentAdapter.loadObjects();
                }
            });

            // Sets the views for the Comment list
            commentList.setEmptyView(findViewById(R.id.empty_list_view));
            commentList.setAdapter(commentAdapter);
        } catch (Exception e) {
            LogHelper.logError(this, "EventViewActivity", "Error loading comments.", e.getMessage());
        }
    }

    // Sets the colours of the vote buttons
    private void setVoteButtons(int voteResult) {
        try {
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
        } catch (Exception e) {
            LogHelper.logError(this, "EventViewActivity", "Error displaying votes.", e.getMessage());
        }
    }

    /***
     * Action for when the upvote button is pressed.
     * @param v The sending View object.
     */
    private void upvote(View v) {
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

    /***
     * Action for when the downvote button is pressed.
     * @param v The sending View object.
     */
    private void downvote(View v) {
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

    /***
     * Submits the comment to Parse.
     * @param commentBody The text of the comment.
     */
    @Override
    public void onDialogSubmitClick(String commentBody) {
        try {
            final Comment comment = new Comment();
            comment.setCommentBody(commentBody);
            comment.setCommentEvent(event);
            comment.setCreator(ClassmateUser.getCurrentUser());

            // Saves the Comment for the Event being viewed
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // Handling the success/failure of saving a Comment
                    if (e == null) {
                        commentAdapter.loadObjects();
                        // Scrolls the Comments list to the bottom to display the just-added Comment
                        commentList.smoothScrollByOffset(Integer.MAX_VALUE);
                        Toast.makeText(EventViewActivity.this, "Comment successfully saved.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventViewActivity.this, "Error saving comment. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            LogHelper.logError(this, "EventViewActivity", "Error saving comment.", e.getMessage());
        }
    }
}
