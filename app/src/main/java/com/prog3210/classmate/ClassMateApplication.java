package com.prog3210.classmate;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.Semester;
import com.prog3210.classmate.courses.Course;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.events.Event;

public class ClassMateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register Parse subclasses here
        ParseObject.registerSubclass(ClassmateUser.class);

        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Semester.class);
        ParseObject.registerSubclass(EventType.class);
        ParseObject.registerSubclass(Course.class);
        ParseObject.registerSubclass(Event.class);

        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
    }
}