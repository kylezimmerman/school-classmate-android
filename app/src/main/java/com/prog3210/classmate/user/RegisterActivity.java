package com.prog3210.classmate.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
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

    private void register() {
        EditText emailEditText = (EditText)findViewById(R.id.email);
        EditText userNameEditText = (EditText)findViewById(R.id.username);
        EditText passwordEditText = (EditText)findViewById(R.id.password);
        EditText firstNameEditText = (EditText)findViewById(R.id.first_name);
        EditText lastNameEditText = (EditText)findViewById(R.id.last_name);

        ClassmateUser user = new ClassmateUser();
        user.setUsername(userNameEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.setFirstName(firstNameEditText.getText().toString());
        user.setLastName(lastNameEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
               if (e == null) {
                   Intent openMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                   startActivity(openMainIntent);
                   finish();
               } else {
                   Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
               }
            }
        });
    }
}
