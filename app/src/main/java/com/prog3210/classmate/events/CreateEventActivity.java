package com.prog3210.classmate.events;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.EventTypeAdapter;
import com.prog3210.classmate.courses.Course;
import com.prog3210.classmate.courses.CourseAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateEventActivity extends BaseAuthenticatedActivity {
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        final Spinner courseSpinner = (Spinner) findViewById(R.id.course_code_spinner);
        final Spinner eventTypeSpinner = (Spinner) findViewById(R.id.eventType_spinner);
        Button dueDate = (Button) findViewById(R.id.due_date_button);
        Button createEvent = (Button) findViewById(R.id.create_event_button);

        try {
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
        catch(Exception e){
            Log.e("create spinners", e.getMessage());
        }



        final int month = 0;
        final int day  = 0;
        final int year = 0;
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog picker = new
                        DatePickerDialog(CreateEventActivity.this, datePickerListener, year, month, day);

                picker.show();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEvent(eventTypeSpinner, courseSpinner);
            }
        });
    }

    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            GregorianCalendar setDate = new GregorianCalendar(year,month,day);
            event.setDate(setDate.getTime());
        }
    };

    public void CreateEvent(Spinner eventType, Spinner course){
        EditText description = (EditText) findViewById(R.id.eventDescription);
        EditText gradeWorth = (EditText) findViewById(R.id.gradeWorth);
        EditText eventName = (EditText) findViewById(R.id.eventName);

        EditText error = null;

        if (gradeWorth.getText().length() == 0){
            gradeWorth.setError("Please enter a grade worth");
            error = gradeWorth;
        }else if (eventName.getText().length() == 0 ){
            eventName.setError("Please enter an event name");
            error = eventName;
        }

        if (error != null){
            error.requestFocus();
            return;
        }

        event.setCourse((Course) course.getSelectedItem());
        event.setDescription(description.getText().toString());
        event.setName(eventName.getText().toString());
        event.setFinalGradeWeight(Double.parseDouble(gradeWorth.getText().toString()));
        event.setEventType((EventType) eventType.getSelectedItem());
        event.setCreator(ClassmateUser.getCurrentUser());

        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){
                    Intent mainActivity = new Intent(CreateEventActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                }else{
                    Toast.makeText(CreateEventActivity.this,e.getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
