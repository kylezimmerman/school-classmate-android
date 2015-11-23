package com.prog3210.classmate.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseActivity;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.Semester;
import com.prog3210.classmate.core.SemesterAdapter;

public class CreateCourseActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_create);

        Spinner semesterSpinner = (Spinner)findViewById(R.id.semester);
        SemesterAdapter semesterAdapter = new SemesterAdapter(this);
        semesterAdapter.setTextKey("semesterName");
        semesterSpinner.setAdapter(semesterAdapter);

        Button createCourseButton = (Button)findViewById(R.id.create_course_button);
        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCourse();
            }
        });
    }

    private void createCourse() {
        EditText courseCode = (EditText)findViewById(R.id.course_code);
        EditText section = (EditText)findViewById(R.id.section);
        EditText name = (EditText)findViewById(R.id.name);
        EditText year = (EditText)findViewById(R.id.year);
        Spinner semester = (Spinner)findViewById(R.id.semester);
        EditText teacherName = (EditText)findViewById(R.id.teacher_name);

        EditText firstError = null;

        if (courseCode.getText().length() == 0) {
            firstError = courseCode;
            courseCode.setError("Please enter the course code.");
        } else if (section.getText().length() == 0) {
            firstError = section;
            section.setError("Please enter the course section.");
        } else if (name.getText().length() == 0) {
            firstError = name;
            name.setError("Please enter the course name.");
        } else if (year.getText().length() == 0) {
            firstError = year;
            year.setError("Please enter the year in which the course takes place.");
        } else if (teacherName.getText().length() == 0) {
            firstError = teacherName;
            teacherName.setError("Please enter the name of the course's instructor.");
        }

        if (firstError != null) {
            firstError.requestFocus();
            return;
        }

        final Course course = new Course();
        course.setCourseCode(courseCode.getText().toString());
        course.setSection(section.getText().toString());
        course.setName(name.getText().toString());
        course.setYear(Integer.parseInt(year.getText().toString()));
        course.setTeacherName(teacherName.getText().toString());
        course.setSemester((Semester) semester.getSelectedItem());
        course.setCreator(ParseUser.getCurrentUser());

        course.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent openMainIntent = new Intent(CreateCourseActivity.this, MainActivity.class);
                    Toast.makeText(CreateCourseActivity.this, "Successfully created " + course.getName() + "!", Toast.LENGTH_LONG).show();
                    startActivity(openMainIntent);
                    finish();
                } else {
                    Toast.makeText(CreateCourseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}