/*
    CreateEventActivity.java

    Class handling the actions and display of the 'Create Event' view.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.events;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.EventTypeAdapter;
import com.prog3210.classmate.core.NotificationHelper;
import com.prog3210.classmate.courses.Course;
import com.prog3210.classmate.courses.CourseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateEventActivity extends BaseAuthenticatedActivity {
    Event event;
    Button dueDate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        // Getting the object references for the items in the activity_event_create.xml layout
        final Spinner courseSpinner = (Spinner) findViewById(R.id.course_code_spinner);
        final Spinner eventTypeSpinner = (Spinner) findViewById(R.id.eventType_spinner);
        dueDate = (Button) findViewById(R.id.due_date_button);
        Button createEvent = (Button) findViewById(R.id.create_event_button);

        // Getting the due date for the new Event
        Date date = new Date();
        event = new Event();
        event.setDate(date);
        dueDate.setHint(event.getDateString());

        // Creating an EventTypeAdapter to populate the EventType Spinner
        EventTypeAdapter eventTypeAdapter = new EventTypeAdapter(this);
        eventTypeAdapter.setTextKey("typeName");
        eventTypeSpinner.setAdapter(eventTypeAdapter);

        // Checking for the ID of the course in which the user chose to create an event
        final String sendingCourseId = getIntent().getStringExtra("sending_course_id");

        //Creating a CourseAdapter to populate the Course Spinner
        CourseAdapter courseAdapter = new CourseAdapter(this, CourseAdapter.FilterMode.Joined);
        courseAdapter.setShowDetails(false);
        courseAdapter.enableSpinnerSupport();
        courseSpinner.setAdapter(courseAdapter);

        // adding listener to set the pre-selected Course, if the user created an event from the Course view
        courseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Course>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Course> objects, Exception e) {
                // Looping over courses in list to set the pre-selected Course
                for (int i = 0; i < objects.size(); i++) {
                    Course course = objects.get(i);
                    if (course.getObjectId().equals(sendingCourseId)) {
                        courseSpinner.setSelection(i);
                    }
                }
            }
        });
        courseSpinner.setAdapter(courseAdapter);

        // Setting click listener for the DatePicker
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

    // Creating the OnDateSetListener action as an anonymous function
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            GregorianCalendar setDate = new GregorianCalendar(year,month,day);
            event.setDate(setDate.getTime());
            dueDate.setText(event.getDateString());
        }
    };

    /***
     * Method to create an event from the user's input.
     * @param eventType The EventType of the Event, selected by the user.
     * @param course The Course that the Event belongs to, selected by the user.
     */
    public void createEvent(Spinner eventType, Spinner course){
        // Getting the object information from the activity_event_create.xml layout
        EditText description = (EditText) findViewById(R.id.eventDescription);
        EditText gradeWorth = (EditText) findViewById(R.id.gradeWorth);
        final EditText eventName = (EditText) findViewById(R.id.eventName);
        EditText error = null;

        // Checking user-input to ensure proper creation
        if (gradeWorth.getText().length() == 0){
            gradeWorth.setError("Please enter a grade worth");
            error = gradeWorth;
        }else if (eventName.getText().length() == 0 ){
            eventName.setError("Please enter an event name");
            error = eventName;
        }

        // Checking if errors have been detected
        if (error != null){
            error.requestFocus();
            return;
        }

        // Setting the information entered by the user into an Event object
        event.setCourse((Course) course.getSelectedItem());
        event.setDescription(description.getText().toString());
        event.setName(eventName.getText().toString());
        event.setFinalGradeWeight(Double.parseDouble(gradeWorth.getText().toString()));
        event.setEventType((EventType) eventType.getSelectedItem());
        event.setCreator(ClassmateUser.getCurrentUser());

        // Saving the Event object to Parse
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Sends push notification
                    sendPushNotification();
                    // Schedules local notification
                    NotificationHelper.scheduleNotification(CreateEventActivity.this,
                            event.getObjectId(),
                            String.format("Deadline for '%s' is coming up!", event.getName()),
                            event.getDate());
                    finish();
                } else {
                    Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to send a push notification through Parse to others in the course the Event has been created for
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
                            ParseInstallation.getCurrentInstallation().getInstallationId()));

            //Asynchronously send the push.
            push.sendInBackground();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}