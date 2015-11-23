package com.prog3210.classmate.courses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        courseAdapter = new CourseAdapter(this);

        ListView courseList = (ListView) findViewById(R.id.joinCourseList);
        courseList.setAdapter(courseAdapter);

        courseList.setOnItemClickListener(Selected);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.create_course_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createCourseIntent = new Intent(view.getContext(), CreateCourseActivity.class);
                startActivity(createCourseIntent);
            }
        });
    }

    private AdapterView.OnItemClickListener Selected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            final Course course = courseAdapter.getItem(position);

           course.addMember(new SaveCallback() {
               @Override
               public void done(ParseException e) {

                   if (e == null) {
                       Intent result = new Intent();
                       result.putExtra("courseJoined", course.getObjectId());
                       setResult(Activity.RESULT_OK, result);
                       finish();
                   }
                   else{
                       Toast.makeText(JoinCourseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_course, menu);
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
