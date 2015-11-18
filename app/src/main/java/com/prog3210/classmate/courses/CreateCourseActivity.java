package com.prog3210.classmate.courses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.Semester;

public class CreateCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_create);

        ParseQueryAdapter.QueryFactory<Semester> factory =
                new ParseQueryAdapter.QueryFactory<Semester>() {
                    @Override
                    public ParseQuery<Semester> create() {
                        return Semester.getQuery();
                    }
                };
        Spinner semesterSpinner = (Spinner)findViewById(R.id.semester);
        ParseQueryAdapter<Semester> semesterAdapter = new ParseQueryAdapter<>(this, factory);
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

        Course course = new Course();
        course.setCourseCode(courseCode.getText().toString());
        course.setSection(section.getText().toString());
        course.setName(name.getText().toString());
        course.setYear(Integer.parseInt(year.getText().toString()));

        //course.setSemester();
    }
}
