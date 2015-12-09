/*
    ClassMateApplication.java

    contains initializations needed to start app

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.prog3210.classmate.comments.Comment;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.core.Semester;
import com.prog3210.classmate.courses.Course;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.events.Event;

public class ClassMateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        //Register Parse subclasses here
        ParseObject.registerSubclass(ClassmateUser.class);
        ParseObject.registerSubclass(Semester.class);
        ParseObject.registerSubclass(EventType.class);
        ParseObject.registerSubclass(Course.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));

        //Initialize 3rd Party Logins
        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize(getResources().getString(R.string.twitter_consumer_key),
                getResources().getString(R.string.twitter_consumer_secret));

        //Save the parse installation so that we can send push notifications to the device
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}