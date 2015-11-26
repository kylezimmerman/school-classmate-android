package com.prog3210.classmate.events;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/25/2015.
 */
public class EventItemView extends LinearLayout {

    TextView courseCode;
    TextView eventName;
    TextView dueDate;

    TextView upvoteCount;
    TextView downvoteCount;

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
        courseCode = (TextView)findViewById(R.id.course_code);
        eventName = (TextView)findViewById(R.id.event_name);
        dueDate = (TextView)findViewById(R.id.due_date);

        upvoteCount = (TextView)findViewById(R.id.upvote_count);
        downvoteCount = (TextView)findViewById(R.id.downvote_count);
    }

    public void update(Event event) {
        getViews();

        LinearLayout.LayoutParams upvoteLayout = (LinearLayout.LayoutParams)upvoteCount.getLayoutParams();
        LinearLayout.LayoutParams downvoteLayout = (LinearLayout.LayoutParams)upvoteCount.getLayoutParams();

        //courseCode.setText(event.getCourse().getName());
        eventName.setText(event.getName());
        dueDate.setText(event.getDateString());

        //TODO: Change to use event.getUpvotes() and getDownvotes() when Justin adds that
        int upvotes = event.getUpvotes();
        int downvotes = event.getDownvotes();
        float totalVotes = upvotes + downvotes;

        upvoteCount.setText(String.valueOf(upvotes));
        upvoteLayout.weight = upvotes;
        upvoteCount.setLayoutParams(upvoteLayout);

        downvoteCount.setText(String.valueOf(downvotes));
        downvoteLayout.weight = downvotes;
        downvoteCount.setLayoutParams(downvoteLayout);
    }
}
