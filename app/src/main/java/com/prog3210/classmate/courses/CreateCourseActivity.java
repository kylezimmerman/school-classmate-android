/*
    CreateCourseActivity.java

    displays view that will allow user to create a course

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.MainActivity;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseAuthenticatedActivity;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.Semester;
import com.prog3210.classmate.core.SemesterAdapter;

public class CreateCourseActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_course_create);

            //prepares the spinner with all the smesters
            Spinner semesterSpinner = (Spinner) findViewById(R.id.semester);
            SemesterAdapter semesterAdapter = new SemesterAdapter(this);
            semesterAdapter.setTextKey("semesterName");
            semesterSpinner.setAdapter(semesterAdapter);

            //sets the create course buttons to a click event that will start the course creation
            Button createCourseButton = (Button) findViewById(R.id.create_course_button);
            createCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createCourse();
                }
            });
        } catch (Exception e) {
            LogHelper.logError(this, "CreateCourseActivity", "Error generating view.", e.getMessage());
        }
    }

    /***
     * tries to create the course and save it to parse if there are no errors
     */
    private void createCourse() {
        try {
            //declares and initializes view controls
            EditText courseCode = (EditText) findViewById(R.id.course_code);
            EditText section = (EditText) findViewById(R.id.section);
            EditText name = (EditText) findViewById(R.id.name);
            EditText year = (EditText) findViewById(R.id.year);
            Spinner semester = (Spinner) findViewById(R.id.semester);
            EditText teacherName = (EditText) findViewById(R.id.teacher_name);

            EditText firstError = null;

            //checks the lengths of all reqired fields and sets an error if any of them are empty
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

            //checks if error was set and if so sets the focus to the error and ends method
            if (firstError != null) {
                firstError.requestFocus();
                return;
            }

            //sets all the contents of the view to the corresponding course property
            final Course course = new Course();
            course.setCourseCode(courseCode.getText().toString());
            course.setSection(section.getText().toString());
            course.setName(name.getText().toString());
            course.setYear(Integer.parseInt(year.getText().toString()));
            course.setTeacherName(teacherName.getText().toString());
            course.setSemester((Semester) semester.getSelectedItem());
            course.setCreator(ParseUser.getCurrentUser());

            ParseRelation<ClassmateUser> members = course.getRelation("members");
            members.add(ClassmateUser.getCurrentUser());

            //saves the course to the parse database
            course.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Intent openMainIntent = new Intent(CreateCourseActivity.this, MainActivity.class);
                        startActivity(openMainIntent);
                        finish();
                    } else {
                        Toast.makeText(CreateCourseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            LogHelper.logError(this, "CreateCourseActivity", "Error creating course.", e.getMessage());
        }
    }
}