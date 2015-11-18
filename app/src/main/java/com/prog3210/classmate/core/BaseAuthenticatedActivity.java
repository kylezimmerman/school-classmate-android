package com.prog3210.classmate.core;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseUser;
import com.prog3210.classmate.user.UserLoginActivity;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class BaseAuthenticatedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() == null) {
            Log.i("LOGIN REQUIRED", "User was not logged in, redirecting to Login Activity");
            Toast.makeText(this, "Please Log In", Toast.LENGTH_LONG).show();

            Intent loginIntent = new Intent(this, UserLoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
