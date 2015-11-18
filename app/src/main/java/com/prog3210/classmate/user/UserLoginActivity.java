package com.prog3210.classmate.user;

import android.content.Intent;
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
                Toast.makeText(getApplicationContext(), R.string.missingUserName, Toast.LENGTH_SHORT).show();
            }
            else if(password.getText() == null || password.getText().length() == 0){
                password.requestFocus();
                Toast.makeText(getApplicationContext(), R.string.missingPassword, Toast.LENGTH_SHORT).show();
            }
            else{
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            Intent mainActivity = new Intent(UserLoginActivity.this, MainActivity.class);
                            startActivity(mainActivity);

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.failedLogin, Toast.LENGTH_SHORT).show();
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
