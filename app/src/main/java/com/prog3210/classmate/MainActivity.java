package com.prog3210.classmate;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.courses.CourseListFragment;
import com.prog3210.classmate.events.EventListFragment;

public class MainActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarBackEnabled(false);

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(new ClassmatePager(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == R.id.action_logout) {
            ClassmateUser.logOut(this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ClassmatePager extends FragmentPagerAdapter {

        private EventListFragment eventListFragment;
        private CourseListFragment courseListFragment;

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
