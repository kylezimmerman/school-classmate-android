/*
    BaseActivity.java

    This is an abstract base class for an Activity.
    It allows us to have shared logic that doesn't need to be repeated in EVERY Activity.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;

public abstract class BaseActivity extends AppCompatActivity {

    private boolean toolbarBackButtonEnabled = true;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        try {
            //Set up the Material toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                ActionBar supportActionbar = getSupportActionBar();

                if (toolbarBackButtonEnabled && supportActionbar != null) {
                    supportActionbar.setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(actionbarNavigiationOnClickedListener);
                }
            }
        } catch (Exception ex) {
            //There's not much we can do to recover from this so log it.
            //The app just won't have an actionbar
            LogHelper.logError(this, "BaseActivity", "An error occurred creating the Toolbar.", ex.getMessage());
        }

    }

    private final View.OnClickListener actionbarNavigiationOnClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Note: finish() can't throw an exception, so no try/catch

            //When the back button is clicked, finish the current activity.
            finish();
        }
    };

    /***
     * Gets whether this activity has the back button enabled in the action bar
     * @return True if the back button is enabled
     */
    protected boolean getToolbarBackEnabled () {
        return toolbarBackButtonEnabled;
    }

    /***
     * Sets whether this act
     * @param enabled True if you want the activity to have a back button
     */
    protected void setToolbarBackEnabled (boolean enabled) {
        toolbarBackButtonEnabled = enabled;
    }
}
