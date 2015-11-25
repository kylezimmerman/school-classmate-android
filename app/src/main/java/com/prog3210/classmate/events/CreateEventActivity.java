package com.prog3210.classmate.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;

import com.prog3210.classmate.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        event = new Event();

        Spinner course = (Spinner) findViewById(R.id.course_code_spinner);
        Spinner eventType = (Spinner) findViewById(R.id.course_code_spinner);
        Button dueDate = (Button) findViewById(R.id.due_date_button);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, LLL D");
        Date date = new Date();
        String todayFormatted = dateFormat.format(date);
        dueDate.setHint(todayFormatted);


    }

}
