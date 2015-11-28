package com.prog3210.classmate.events;

import android.content.Context;
import android.util.AttributeSet;
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

    public EventItemView(Context context) {
        super(context);
        getViews();
    }

    public EventItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViews();
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViews();
    }

    private void getViews() {
        courseTextView = (TextView)findViewById(R.id.course);
        eventNameTextView = (TextView)findViewById(R.id.event_name);
        dueDateTextView = (TextView)findViewById(R.id.due_date);

        upvoteCountTextView = (TextView)findViewById(R.id.upvote_count);
        downvoteCountTextView = (TextView)findViewById(R.id.downvote_count);
    }

    public void update(Event event) {
        getViews();

        LinearLayout.LayoutParams upvoteLayout = (LinearLayout.LayoutParams)upvoteCountTextView.getLayoutParams();
        LinearLayout.LayoutParams downvoteLayout = (LinearLayout.LayoutParams)upvoteCountTextView.getLayoutParams();

        Course course = event.getCourse();
        if (course != null) {
            courseTextView.setText(String.format("%s - %s", course.getCourseCode(), course.getName()));
        } else {
            courseTextView.setText("");
        }
        eventNameTextView.setText(event.getName());
        dueDateTextView.setText(event.getDateString());

        int upvotes = event.getUpvotes();
        int downvotes = event.getDownvotes();

        upvoteCountTextView.setText(String.valueOf(upvotes));
        upvoteLayout.weight = upvotes;
        upvoteCountTextView.setLayoutParams(upvoteLayout);

        downvoteCountTextView.setText(String.valueOf(downvotes));
        downvoteLayout.weight = downvotes;
        downvoteCountTextView.setLayoutParams(downvoteLayout);
    }
}
