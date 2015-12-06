package com.prog3210.classmate.events;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
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
import com.prog3210.classmate.core.AlarmReceiver;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.EventTypeAdapter;
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
        eventTypeSpinner.setAdapter(eventTypeAdapter);


        final String sendingCourseId = getIntent().getStringExtra("sending_course_id");

        CourseAdapter courseAdapter = new CourseAdapter(this, CourseAdapter.FilterMode.Joined);
        courseAdapter.setShowDetails(false);
        courseAdapter.enableSpinnerSupport();
        courseSpinner.setAdapter(courseAdapter);
        
        courseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Course>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Course> objects, Exception e) {
                for (int i = 0; i < objects.size(); i++) {
                    Course course = objects.get(i);
                    if (course.getObjectId().equals(sendingCourseId)) {
                        courseSpinner.setSelection(i);
                    }
                }
            }
        });
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
        final EditText eventName = (EditText) findViewById(R.id.eventName);
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
                    generateLocalNotification();
                    scheduleNotification(String.format("%s is coming up!", event.getName()),
                            String.format("Don't forget about '%s', it's almost due!", event.getName()),
                            event.getDate());
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
                            ParseInstallation.getCurrentInstallation().getInstallationId()));

            //Asynchronously send the push.
            push.sendInBackground();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private void generateLocalNotification() {
        String notificationTitle = String.format("%s is coming up!",
                event.getName());

        String notificationText = String.format("The deadline for '%s' for %s is coming up! Don't forget!",
                event.getName(),
                event.getCourse().getCourseCode());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText);

        Intent viewEventIntent = new Intent(this, EventViewActivity.class);
        viewEventIntent.putExtra("event_id", event.getObjectId());

        PendingIntent notificationIntent = PendingIntent.getActivity(
                this, 0, viewEventIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(notificationIntent);

        int notificationId = 101001;

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    //TODO: decide on what the NOTIFICATION_ID and NOTIFICATION things should be
    // got this from https://gist.github.com/BrandonSmith/6679223
    private void scheduleNotification(String title, String message, Date date) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_notification);

        // API 16+
        //Notification eventNotification = builder.build();
        // API 11-15
        Notification eventNotification = builder.getNotification();


        //TODO: correct pending intent
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, eventNotification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //TODO: change back to date.getTIme() when finished (this is for testing now)
        //long futureInMillis = date.getTime();
        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}