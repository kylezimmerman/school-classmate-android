/*
    CourseViewActivity.java

    displays details of a selected course

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.events.CreateEventActivity;
import com.prog3210.classmate.events.Event;
import com.prog3210.classmate.events.EventAdapter;
import com.prog3210.classmate.events.EventViewActivity;

import java.util.List;

public class CourseViewActivity extends BaseAuthenticatedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_course_view);

            //grab spinner and fab from activity_course_view.xm;
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_event_button);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

            Intent sendingIntent = getIntent();
            final String courseId = sendingIntent.getStringExtra("course_id");

            progressBar.setVisibility(View.VISIBLE);

            ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
            query.include("semester");
            query.getInBackground(courseId, new GetCallback<Course>() {
                @Override
                public void done(Course object, ParseException e) {
                    progressBar.setVisibility(View.GONE);
                    if (e == null) {
                        displayCourseInfo(object);
                    } else {
                        Toast.makeText(CourseViewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addEventIntent = new Intent(view.getContext(), CreateEventActivity.class);
                    addEventIntent.putExtra("sending_course_id", courseId);
                    startActivity(addEventIntent);
                }
            });
        } catch (Exception e) {
            LogHelper.logError(this, "CourseViewActivity", "Error displaying course view.", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();

            Intent sendingIntent = getIntent();
            final String courseId = sendingIntent.getStringExtra("course_id");
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.VISIBLE);

            ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
            query.include("semester");
            query.getInBackground(courseId, new GetCallback<Course>() {
                @Override
                public void done(Course object, ParseException e) {
                    progressBar.setVisibility(View.GONE);
                    if (e == null) {
                        displayCourseInfo(object);
                    } else {
                        Toast.makeText(CourseViewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            LogHelper.logError(this, "CourseViewActivity", "Error returning to course.", e.getMessage());
        }
    }

    private void displayCourseInfo(Course course) {
        try {
            //grabbing TextView items from course_view_layout
            TextView courseCode = (TextView) findViewById(R.id.course_code);
            TextView courseName = (TextView) findViewById(R.id.course_name);
            TextView courseSection = (TextView) findViewById(R.id.course_section);
            TextView courseInstructor = (TextView) findViewById(R.id.course_instructor);
            TextView courseDate = (TextView) findViewById(R.id.course_date);

            //sets text of TextViews from course parseObject
            courseCode.setText(course.getCourseCode());
            courseName.setText(course.getName());
            courseSection.setText(course.getSection());
            courseInstructor.setText(course.getTeacherName());
            courseDate.setText(String.format("%s %s", course.getSemester().getSemesterName(), course.getYear()));

            //grab the course_view_layout and make it visible
            View courseView = findViewById(R.id.course_view_layout);
            courseView.setVisibility(View.VISIBLE);

            //populate event list with all event for that course
            ListView eventList = (ListView) findViewById(R.id.event_list);
            final EventAdapter eventAdapter = new EventAdapter(this, course);
            eventList.setAdapter(eventAdapter);
            eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    try {
                        Intent viewEventIntent = new Intent(CourseViewActivity.this, EventViewActivity.class);
                        viewEventIntent.putExtra("event_id", eventAdapter.getItem(position).getObjectId());
                        startActivity(viewEventIntent);
                    } catch (Exception ex) {
                        LogHelper.logError(CourseViewActivity.this, "CourseViewActivity", "Showing event details", ex.getMessage());
                    }
                }
            });

            eventList.setEmptyView(findViewById(R.id.empty_list_view));

            //set pull to refresh on event list for course
            final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
            eventAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Event>() {
                @Override
                public void onLoading() {
                    //Intentional no-op. Interface defines it but we don't need it
                }

                @Override
                public void onLoaded(List<Event> list, Exception e) {
                    pullToRefresh.setRefreshing(false);
                }
            });

            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    eventAdapter.loadObjects();
                }
            });
        } catch (Exception e) {
            LogHelper.logError(this, "CourseViewActivity", "Error displaying course information.", e.getMessage());
        }
    }
}