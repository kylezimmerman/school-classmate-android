package com.prog3210.classmate.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseActivity;

import java.text.ParseException;

public class UserLoginActivity extends BaseActivity {

    Button login;
    Button register;

    private final static String NOUSERNAME = "Please enter in your username";
    private final static String NOPASSWORD = "Please enter in your password";
    private final static String BADLOGIN = "Username or Password incorrect please try again";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register_button);

        login.setOnClickListener(attemptLogin);
        register.setOnClickListener(registerUser);

    }

    private View.OnClickListener attemptLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText userName = (EditText) findViewById(R.id.userName_editview);
            EditText password = (EditText) findViewById(R.id.password_editview);

            if (userName.getText() == null && userName.getText().toString().isEmpty()){
                userName.requestFocus();
                Toast.makeText(getApplicationContext(), NOUSERNAME, Toast.LENGTH_SHORT).show();
            }
            else if(password.getText() == null && userName.getText().toString().isEmpty()){
                password.requestFocus();
                Toast.makeText(getApplicationContext(), NOPASSWORD, Toast.LENGTH_SHORT).show();
            }
            else{
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            Intent mainActivity = new Intent(UserLoginActivity.this, MainActivity.class);
                            startActivity(mainActivity);

                        } else {
                            Toast.makeText(getApplicationContext(), BADLOGIN, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    private View.OnClickListener registerUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
