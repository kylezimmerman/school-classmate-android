package com.prog3210.classmate.events;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.EventTypeAdapter;
import com.prog3210.classmate.courses.Course;
import com.prog3210.classmate.courses.CourseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateEventActivity extends BaseAuthenticatedActivity {
    Event event;
    Button dueDate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        final Spinner courseSpinner = (Spinner) findViewById(R.id.course_code_spinner);
        final Spinner eventTypeSpinner = (Spinner) findViewById(R.id.eventType_spinner);
        dueDate = (Button) findViewById(R.id.due_date_button);
        Button createEvent = (Button) findViewById(R.id.create_event_button);

        Date date = new Date();
        event = new Event();
        event.setDate(date);
        dueDate.setHint(event.getDateString());

        EventTypeAdapter eventTypeAdapter = new EventTypeAdapter(this);
        eventTypeAdapter.setTextKey("typeName");
        eventTypeAdapter.setPaginationEnabled(false);
        eventTypeSpinner.setAdapter(eventTypeAdapter);

        CourseAdapter courseAdapter = new CourseAdapter(this, CourseAdapter.FilterMode.Joined);
        courseAdapter.setShowDetails(false);
        courseAdapter.setPaginationEnabled(false);
        courseSpinner.setAdapter(courseAdapter);


        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(event.getDate());

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog picker = new
                        DatePickerDialog(CreateEventActivity.this, datePickerListener, year, month, day);

                picker.show();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(eventTypeSpinner, courseSpinner);
            }
        });
    }

    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            GregorianCalendar setDate = new GregorianCalendar(year,month,day);
            event.setDate(setDate.getTime());
            dueDate.setHint(event.getDateString());
        }
    };

    public void createEvent(Spinner eventType, Spinner course){
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
                if (e == null) {
                    sendPushNotification();
                    finish();
                } else {
                    Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendPushNotification() {
        try {
            //Build the message to send
            String message = String.format("Event '%s' was added to '%s - %s'",
                    event.getName(),
                    event.getCourse().getCourseCode(),
                    event.getCourse().getName());

            //Create a parse push notification
            ParsePush push = new ParsePush();

            //Send it to everyone in the course's channel
            push.setChannel("course_" + event.getCourse().getObjectId());

            //Set the alert text and event_id (so that the client can take you to the correct Activity)
            push.setData(new JSONObject()
                    .put("alert", message)
                    .put("event_id", event.getObjectId())
            );

            //Don't send it to yourself.
            push.setQuery(ParseInstallation.getQuery().
                    whereNotEqualTo("installationId",
                            ParseInstallation.getCurrentInstallation().getObjectId()));

            //Asynchronously send the push.
            push.sendInBackground();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
