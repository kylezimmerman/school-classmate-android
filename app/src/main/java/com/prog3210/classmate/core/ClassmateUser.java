package com.prog3210.classmate.core;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import com.prog3210.classmate.user.UserLoginActivity;

/**
 * Created by kzimmerman on 11/18/2015.
 */
@ParseClassName("_User")
public class ClassmateUser extends ParseUser {

    public String getFirstName() {
        return getString("firstName");
    }
    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    public String getLastName() {
        return getString("lastName");
    }
    public void setLastName(String firstName) {
        put("lastName", firstName);
    }

    public static void logOut(Context context) {
        ParseUser.logOut();
        Intent loginIntent = new Intent(context, UserLoginActivity.class);
        context.startActivity(loginIntent);
    }

    public static ClassmateUser getCurrentUser() {
        return (ClassmateUser)ParseUser.getCurrentUser();
    }
}
