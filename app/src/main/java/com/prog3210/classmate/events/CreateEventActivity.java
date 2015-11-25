package com.prog3210.classmate.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;

import com.prog3210.classmate.R;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.EventTypeAdapter;
import com.prog3210.classmate.courses.Course;
import com.prog3210.classmate.courses.CourseAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Spinner courseSpinner = (Spinner) findViewById(R.id.course_code_spinner);
        Spinner eventTypeSpinner = (Spinner) findViewById(R.id.course_code_spinner);
        Button dueDate = (Button) findViewById(R.id.due_date_button);

        event = new Event();
        Date date = new Date();
        event.setDate(date);

        dueDate.setHint(event.getDateString());

        EventTypeAdapter eventTypeAdapter = new EventTypeAdapter(this);
        eventTypeAdapter.setTextKey("typeName");
        eventTypeSpinner.setAdapter(eventTypeAdapter);

        CourseAdapter courseAdapter = new CourseAdapter(this, CourseAdapter.FilterMode.Joined);
        courseAdapter.setTextKey("courseCode");
        courseSpinner.setAdapter(courseAdapter);

    }

}
