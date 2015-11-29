package com.prog3210.classmate.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.prog3210.classmate.R;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    boolean toolbarBackButtonEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (toolbarBackButtonEnabled) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }
    }

    protected boolean getToolbarBackEnabled () {
        return toolbarBackButtonEnabled;
    }
    protected void setToolbarBackEnabled (boolean enabled) {
        toolbarBackButtonEnabled = enabled;
    }
}
