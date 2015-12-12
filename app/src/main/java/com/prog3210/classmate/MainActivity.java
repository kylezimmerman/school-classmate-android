/*
    MainActivity.java

    First activity called when app is launched redirects to login activity if user is not
        authenticated

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.FloatingActionButtonOnClickListener;
import com.prog3210.classmate.courses.CourseListFragment;
import com.prog3210.classmate.events.EventListFragment;

public class MainActivity extends BaseAuthenticatedActivity {

    FloatingActionButton fab;
    ClassmatePager pageAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarBackEnabled(false);

        pageAdapter = new ClassmatePager(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton)findViewById(R.id.fab);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            ClassmateUser.logOut(this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            try {
                //Calculate the total height of the FAB including padding
                int totalFabHeight = fab.getMeasuredWidth() + fab.getPaddingTop() + fab.getPaddingBottom();

                //Generate a value in the range 0-1 following a quadratic curve
                //See http://www.wolframalpha.com/input/?i=1+-+4%28x-0.5%29^2+for+0+to+1 for a visual
                double verticalOffset = 1 - 4 * Math.pow(positionOffset - 0.5f, 2);

                //Set the fab's vertical transition to the offset times the size of the fab.
                //This results in a cool vertical translation while swiping
                fab.setTranslationY((int) (verticalOffset * totalFabHeight));
            } catch (Exception ex) {
                LogHelper.logError(MainActivity.this, "MainActivity",
                        "Error animating the floating action button", ex.getMessage());
            }
        }

        @Override
        public void onPageSelected(int position) {
            try {
                //Get the page's fab listener if it has one
                FloatingActionButtonOnClickListener fabListener = (FloatingActionButtonOnClickListener) pageAdapter.getItem(position);

                //Make the fab do the page's action and show it
                fab.setOnClickListener(fabListener.getFloatingActionOnClickListener());
                fab.show();
            } catch (Exception ex) {
                //Page doesn't implement FloatingACtionButtonOnClickListener.
                //This is perfectly acceptable so no need to log anything
                //just set the floating action button to do nothing and hide it
                fab.setOnClickListener(null);
                fab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Intentional no-op. The interface needs this but we don't need to use it for anything
        }
    };

    public class ClassmatePager extends FragmentPagerAdapter {

        private final EventListFragment eventListFragment;
        private final CourseListFragment courseListFragment;

        public ClassmatePager(FragmentManager fm) {
            super(fm);

            eventListFragment = new EventListFragment();
            courseListFragment = new CourseListFragment();
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return eventListFragment;
            } else if (i == 1) {
                return courseListFragment;
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Events";
            } else if (position == 1) {
                return "Courses";
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
