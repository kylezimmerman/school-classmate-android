/*
    EventItemView.java

    This class handles display of each item in the Event feed ViewList.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.events;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prog3210.classmate.R;
import com.prog3210.classmate.courses.Course;

/**
 * Created by kzimmerman on 11/25/2015.
 */
public class EventItemView extends LinearLayout {

    TextView courseTextView;
    TextView eventNameTextView;
    TextView dueDateTextView;

    TextView upvoteCountTextView;
    TextView downvoteCountTextView;

    LinearLayout voteBar;

    public EventItemView(Context context) {
        super(context);
    }

    public EventItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViews();
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Gets the objects displayed in the layout for use.
    private void getViews() {
        courseTextView = (TextView)findViewById(R.id.course);
        eventNameTextView = (TextView)findViewById(R.id.event_name);
        dueDateTextView = (TextView)findViewById(R.id.due_date);

        upvoteCountTextView = (TextView)findViewById(R.id.upvote_count);
        downvoteCountTextView = (TextView)findViewById(R.id.downvote_count);

        voteBar = (LinearLayout)findViewById(R.id.vote_bar);
    }

    /***
     * Updates the Event layout to display updated information for the current Event.
     * @param event Current Event being viewed.
     */
    public void update(Event event) {
        Course course = event.getCourse();
        if (course != null) {
            courseTextView.setText(String.format("%s - %s", course.getCourseCode(), course.getName()));
        } else {
            courseTextView.setText("");
        }
        eventNameTextView.setText(event.getName());
        dueDateTextView.setText(event.getDateString());

        // Gets the vote values for an Event.
        int upvotes = event.getUpvotes();
        int downvotes = event.getDownvotes();

        // Sets the visibility and display of the votes for an Event.
        if (upvotes == 0 && downvotes == 0) {
            voteBar.setVisibility(View.GONE);
        } else {
            voteBar.setVisibility(View.VISIBLE);
            upvoteCountTextView.setText(String.valueOf(upvotes));
            downvoteCountTextView.setText(String.valueOf(downvotes));
        }
    }
}
