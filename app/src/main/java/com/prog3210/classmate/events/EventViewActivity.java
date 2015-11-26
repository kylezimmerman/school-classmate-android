package com.prog3210.classmate.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.prog3210.classmate.R;

public class EventViewActivity extends AppCompatActivity {
    Event event = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        Intent k = getIntent();

        final String eventId = k.getStringExtra("event_id");

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event object, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    event = object;
                    displayEventInfo(object);
                } else {
                    Toast.makeText(EventViewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void displayEventInfo(Event event) {
        TextView eventName = (TextView)findViewById(R.id.event_name);
        eventName.setText(event.getName());

        TextView eventCourse = (TextView)findViewById(R.id.event_course);
        eventCourse.setText(event.getCourse().getName());

        TextView eventDueDate = (TextView)findViewById(R.id.event_due_date);
        eventDueDate.setText(event.getDateString());

        TextView eventFinalGradeWeight = (TextView)findViewById(R.id.event_final_grade_weight);
        eventFinalGradeWeight.setText(String.format("%s%%", event.getFinalGradeWeight()));

        TextView eventDescription = (TextView)findViewById(R.id.event_description);
        eventDescription.setText(event.getDescription());

        Button upvoteButton = (Button)findViewById(R.id.event_upvote_button);
        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvote(v);
            }
        });

        Button downvoteButton = (Button)findViewById(R.id.event_downvote_button);
        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downvote(v);
            }
        });
    }

    public void upvote(View v) {
        event.upvote(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    findViewById(R.id.event_upvote_button)
                            .setBackgroundColor(getResources()
                                    .getColor(R.color.upvote_color));
                }
            }
        });
    }

    public void downvote(View v) {
        event.downvote(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    findViewById(R.id.event_downvote_button)
                            .setBackgroundColor(getResources()
                                    .getColor(R.color.downvote_color));
                }
            }
        });
    }
}