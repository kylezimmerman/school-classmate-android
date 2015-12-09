/*
    UserLoginActivity.java

    This activity allows a user to log in with an existing account, or with Facebook or Twitter

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.twitter.Twitter;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseActivity;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.courses.Course;

import java.util.ArrayList;
import java.util.List;

public class UserLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Button login = (Button) findViewById(R.id.login_button);
        Button register = (Button) findViewById(R.id.register_button);

        login.setOnClickListener(attemptLogin);
        register.setOnClickListener(registerUser);

        Button facebookLoginButton = (Button)findViewById(R.id.facebook_login);
        facebookLoginButton.setOnClickListener(facebookLogin);

        Button twitterLoginButton = (Button)findViewById(R.id.twitter_login);
        twitterLoginButton.setOnClickListener(twitterLogin);
    }

    //When clicked, attempts to log in using a classmate account
    private View.OnClickListener attemptLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Get the username and password fields
            EditText userName = (EditText) findViewById(R.id.userName_editview);
            EditText password = (EditText) findViewById(R.id.password_editview);

            if (userName.getText() == null || userName.getText().length() == 0) {
                //The username was not entered, so set focus to it and display a message
                userName.requestFocus();
                userName.setError(getString(R.string.missingUserName));
            } else if (password.getText() == null || password.getText().length() == 0) {
                //The password was not entered, so set focus to it and display a message
                password.requestFocus();
                password.setError(getString(R.string.missingPassword));
            } else {
                //The credentials were entered, so try to log in using them.
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            //The login was successful, so join channels to listen to Push Notifications for their courses
                            joinCourseChannels();
                            goToMainActivity();
                        } else {
                            //There was an error logging in, so display an error message.
                            //The most likely reason is that the credentials were incorrect or there was no internet connection
                            Toast.makeText(getApplicationContext(), getString(R.string.failedLogin), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    //When clicked, attempts to log in using Facebook
    private View.OnClickListener facebookLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Check if we already have a facebook access token
            AccessToken token = AccessToken.getCurrentAccessToken();

            if (token != null) {
                //We have a token, so just log in using it.
                ParseFacebookUtils.logInInBackground(token);
            } else {
                //No existing token, so ask the user to log in using facebook and provide us with read access to profile info
                ParseFacebookUtils.logInWithReadPermissionsInBackground(UserLoginActivity.this, null, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            //No errors
                            if (user != null) {
                                //User was logged in successfully

                                //If the user is new, save their First and last name to our database
                                if (user.isNew()) {
                                    ClassmateUser classmateUser = (ClassmateUser) user;
                                    classmateUser.setFirstName(Profile.getCurrentProfile().getFirstName());
                                    classmateUser.setLastName(Profile.getCurrentProfile().getLastName());
                                    classmateUser.saveInBackground();
                                }

                                //Now that we're logged in, join course channels for push notifications
                                joinCourseChannels();
                                goToMainActivity();
                            }
                        } else {
                            ///An error occurred, so let the use know using a toast
                            Toast.makeText(UserLoginActivity.this,
                                    "An error occurred logging in to Facebook. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    private View.OnClickListener twitterLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ParseTwitterUtils.logIn(UserLoginActivity.this, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        if (user != null) {
                            if (user.isNew()) {
                                ClassmateUser classmateUser = (ClassmateUser) user;
                                classmateUser.setFirstName(ParseTwitterUtils.getTwitter().getScreenName());
                                classmateUser.setLastName("");
                                classmateUser.saveInBackground();
                            }
                            
                            joinCourseChannels();
                            goToMainActivity();
                        }
                    } else {
                        Toast.makeText(UserLoginActivity.this,
                                "An error occurred logging in to Twitter. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener registerUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent registerIntent = new Intent(UserLoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        }
    };


    //Goes to the main activity, clearing the stack so that hitting back doesn't log the user out
    private void goToMainActivity() {
        Intent mainActivity = new Intent(UserLoginActivity.this, MainActivity.class);

        //This starts the main activity and clears the back stack
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

    //Joins the channels for each course a user is in so that they can get parse push notifications
    private void joinCourseChannels() {
        ParseQuery<Course> courseQuery = Course.getQuery();
        courseQuery.whereEqualTo("members", ParseUser.getCurrentUser());

        //Query for all of the coures the user is in
        courseQuery.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> courses, ParseException e) {

                //Build a list of all their courses by course object ID
                ArrayList<String> courseChannels = new ArrayList<String>();
                for (Course course : courses) {
                    courseChannels.add("course_" + course.getObjectId());
                }

                //Join all of the channels
                ParseInstallation.getCurrentInstallation().put("channels", courseChannels);
                ParseInstallation.getCurrentInstallation().saveInBackground();
            }
        });
    }
}
