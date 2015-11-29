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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        Intent k = getIntent();

        final String courseId = k.getStringExtra("course_id");

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
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

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.add_event_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(view.getContext(), CreateEventActivity.class);
                addEventIntent.putExtra("sending_course_id", courseId);
                startActivity(addEventIntent);
            }
        });
    }

    private void displayCourseInfo(Course course) {
        TextView courseCode = (TextView)findViewById(R.id.course_code);
        courseCode.setText(course.getCourseCode());

        TextView courseName = (TextView)findViewById(R.id.course_name);
        courseName.setText(course.getName());

        TextView courseSection = (TextView)findViewById(R.id.course_section);
        courseSection.setText(course.getSection());

        TextView courseInstructor = (TextView)findViewById(R.id.course_instructor);
        courseInstructor.setText(course.getTeacherName());

        TextView courseDate = (TextView)findViewById(R.id.course_date);
        courseDate.setText(String.format("%s %s", course.getSemester().getSemesterName(), course.getYear()));

        View courseView = findViewById(R.id.course_view_layout);
        courseView.setVisibility(View.VISIBLE);

        ListView eventList = (ListView)findViewById(R.id.event_list);
        final EventAdapter eventAdapter = new EventAdapter(this, course);
        eventList.setAdapter(eventAdapter);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent viewEventIntent = new Intent(CourseViewActivity.this, EventViewActivity.class);
                viewEventIntent.putExtra("event_id", eventAdapter.getItem(position).getObjectId());
                startActivity(viewEventIntent);
            }
        });

        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.pull_to_refresh);
        eventAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Event>() {
            @Override
            public void onLoading() {

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
    }
}