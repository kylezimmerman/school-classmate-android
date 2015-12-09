/*
    RegisterActivity.java

    This activity allows users to register an account with Classmate.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseActivity;
import com.prog3210.classmate.core.ClassmateUser;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    /***
     * Attempts to register the user with the information provided.
     */
    private void register() {
        //Get all of the editable fields
        EditText emailEditText = (EditText)findViewById(R.id.email);
        EditText userNameEditText = (EditText)findViewById(R.id.username);
        EditText passwordEditText = (EditText)findViewById(R.id.password);
        EditText firstNameEditText = (EditText)findViewById(R.id.first_name);
        EditText lastNameEditText = (EditText)findViewById(R.id.last_name);

        //Store which field produced the first error
        EditText firstError = null;

        //Validate each field was entered in as required.
        //If the validation fails, set it to the firstError and show an error message
        if (emailEditText.getText().length() == 0) {
            firstError = emailEditText;
            emailEditText.setError("Please enter your email address");
        } else if (userNameEditText.getText().length() == 0) {
            firstError = userNameEditText;
            userNameEditText.setError("Please enter a Username");
        } else if (passwordEditText.getText().length() == 0) {
            firstError = passwordEditText;
            passwordEditText.setError("Please enter a password");
        } else if (firstNameEditText.getText().length() == 0) {
            firstError = firstNameEditText;
            firstNameEditText.setError("Please enter your First Name");
        } else if (lastNameEditText.getText().length() == 0) {
            firstError = lastNameEditText;
            lastNameEditText.setError("Please enter your Last Name");
        }

        //If there was an error, focus the first field.
        if (firstError != null) {
            firstError.requestFocus();
            return;
        }

        //Create a new classmate user with the fields provided.
        ClassmateUser user = new ClassmateUser();
        user.setUsername(userNameEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.setFirstName(firstNameEditText.getText().toString());
        user.setLastName(lastNameEditText.getText().toString());

        //Attempt to sign the user up asynchronously
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                //Check if there were any errors registering
                if (e == null) {
                    //No errors, so they are logged in, send them to the main activity
                   Intent openMainIntent = new Intent(RegisterActivity.this, MainActivity.class);

                   //This starts the main activity and clears the back stack
                   openMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                   startActivity(openMainIntent);
                   finish();
                } else {
                    //There were errors, so show them.
                    //The most likely cause is that the username is taken or the password is invalid.
                   Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
