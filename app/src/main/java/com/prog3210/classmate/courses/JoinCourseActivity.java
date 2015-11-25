package com.prog3210.classmate.courses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;

public class JoinCourseActivity extends BaseAuthenticatedActivity {

    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_join);
        EditText search = (EditText) findViewById(R.id.searchListView);

        courseAdapter = new CourseAdapter(this, CourseAdapter.FilterMode.Unjoined);
        ListView courseList = (ListView) findViewById(R.id.joinCourseList);
        courseList.setAdapter(courseAdapter);
        courseList.setOnItemClickListener(selectedCourse);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                courseAdapter.getFilter().filter(charSequence);
                courseAdapter.loadObjects();

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

    private AdapterView.OnItemClickListener selectedCourse = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            final Course course = courseAdapter.getItem(position);

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
