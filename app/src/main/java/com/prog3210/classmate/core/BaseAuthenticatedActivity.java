/*
    BaseAuthenticatedActivity.java

    This is an abstract Activity that requires the user to be logged in to view.
    If the user is not logged in, it finishes the activity and forces them to go back to the login screen.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseUser;
import com.prog3210.classmate.user.UserLoginActivity;

public abstract class BaseAuthenticatedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If the user is not logged in, bump them out.
        if (ParseUser.getCurrentUser() == null) {
            //Make a note to the user
            Log.i("LOGIN REQUIRED", "User was not logged in, redirecting to Login Activity");
            Toast.makeText(this, "Please Log In", Toast.LENGTH_LONG).show();

            //Go to the Login activity
            Intent loginIntent = new Intent(this, UserLoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
