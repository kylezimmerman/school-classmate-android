package com.prog3210.classmate.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.prog3210.classmate.R;

public class CourseViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        Intent k = getIntent();

        String courseId = k.getStringExtra("course_id");

        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.getInBackground(courseId, new GetCallback<Course>() {
            @Override
            public void done(Course object, ParseException e) {
                if (e == null) {
                    displayCourseInfo(object);
                } else {
                    Toast.makeText(CourseViewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
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
        courseDate.setText(course.getSemester().getSemesterName() + ' ' + course.getYear());
    }
}
