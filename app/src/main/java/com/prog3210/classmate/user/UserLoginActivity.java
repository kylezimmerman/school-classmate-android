package com.prog3210.classmate.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseActivity;
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
    }

    private View.OnClickListener attemptLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText userName = (EditText) findViewById(R.id.userName_editview);
            EditText password = (EditText) findViewById(R.id.password_editview);

            if (userName.getText() == null || userName.getText().length() == 0){
                userName.requestFocus();
                userName.setError(getString(R.string.missingUserName));
            }
            else if(password.getText() == null || password.getText().length() == 0){
                password.requestFocus();
                password.setError(getString(R.string.missingPassword));
            }
            else{
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            joinCourseChannels();

                            Intent mainActivity = new Intent(UserLoginActivity.this, MainActivity.class);

                            //This starts the main activity and clears the back stack
                            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainActivity);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.failedLogin), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    private View.OnClickListener registerUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent registerIntent = new Intent(UserLoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        }
    };

    private void joinCourseChannels() {
        ParseQuery<Course> courseQuery = Course.getQuery();
        courseQuery.whereEqualTo("members", ParseUser.getCurrentUser());

        courseQuery.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> courses, ParseException e) {
                ArrayList<String> courseChannels = new ArrayList<String>();
                for (Course course : courses) {
                    courseChannels.add("course_" + course.getObjectId());
                }

                ParseInstallation.getCurrentInstallation().put("channels", courseChannels);
                ParseInstallation.getCurrentInstallation().saveInBackground();
            }
        });
    }
}
