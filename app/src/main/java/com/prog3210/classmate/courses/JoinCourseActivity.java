/*
    JoinCourseActivity.java

    displays view that will allow user to join a course

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;

import java.util.List;

public class JoinCourseActivity extends BaseAuthenticatedActivity {

    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_join);

        //set view items from activity_course_join.xml
        EditText search = (EditText) findViewById(R.id.searchListView);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading);
        courseAdapter = new CourseAdapter(this, CourseAdapter.FilterMode.Unjoined);
        ListView courseList = (ListView) findViewById(R.id.joinCourseList);

        courseList.setOnItemClickListener(selectedCourse);
        courseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Course>() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaded(List<Course> list, Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });

        courseList.setAdapter(courseAdapter);

        //adds a listener to search edit text that sends search sequence to the courseAdapter
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                courseAdapter.setSearchTerm(charSequence.toString());
                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.create_course_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createCourseIntent = new Intent(view.getContext(), CreateCourseActivity.class);
                startActivity(createCourseIntent);
                finish();
            }
        });
    }

    private final AdapterView.OnItemClickListener selectedCourse = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            final Course course = courseAdapter.getItem(position);

            //joins current user to selected course
            course.join(new SaveCallback() {
               @Override
               public void done(ParseException e) {

                   if (e == null) {
                       Intent result = new Intent();
                       result.putExtra("courseJoined", course.getObjectId());
                       setResult(Activity.RESULT_OK, result);
                       finish();
                   } else {
                       Toast.makeText(JoinCourseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });
        }
    };
}
