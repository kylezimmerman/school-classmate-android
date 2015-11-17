package com.prog3210.classmate;

import android.app.Application;

import com.parse.Parse;

public class ClassMateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register Parse subclasses here

        Parse.enableLocalDatastore(this);

        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
    }
}
