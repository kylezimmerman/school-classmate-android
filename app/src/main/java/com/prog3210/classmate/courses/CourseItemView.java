package com.prog3210.classmate.courses;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseItemView extends LinearLayout {

    TextView courseCodeSection;
    TextView courseName;
    TextView teacherName;
    TextView semesterName;
    TextView year;

    public CourseItemView(Context context) {
        super(context);
        getViews();
    }

    public CourseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViews();
    }

    public CourseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViews();
    }

    private void getViews() {
        courseCodeSection = (TextView)findViewById(R.id.course_code);
        courseName = (TextView)findViewById(R.id.course_name);
        teacherName = (TextView)findViewById(R.id.teacher_name);
        semesterName = (TextView)findViewById(R.id.semester);
        year = (TextView)findViewById(R.id.year);
    }

    public void updateValues(Course course) {
        getViews();

        courseCodeSection.setText(course.getCourseCode() + " - " + course.getSection());
        courseName.setText(course.getName());
        teacherName.setText(course.getTeacherName());
        semesterName.setText(course.getSemester().getSemesterName());
        year.setText(String.valueOf(course.getYear()));
    }
}
