package com.prog3210.classmate.courses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseItemView extends RelativeLayout {

    TextView courseCodeSection;
    TextView courseName;
    TextView teacherName;
    TextView semesterName;
    TextView year;

    View detailsView;

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
        detailsView = findViewById(R.id.course_details);

        courseCodeSection = (TextView)findViewById(R.id.course_code);
        courseName = (TextView)findViewById(R.id.course_name);
        teacherName = (TextView)findViewById(R.id.teacher_name);
        semesterName = (TextView)findViewById(R.id.semester);
        year = (TextView)findViewById(R.id.year);
    }

    public void updateValues(Course course) {
        updateValues(course, true);
    }

    public void updateValues(Course course, boolean showDetails) {
        getViews();

        detailsView.setVisibility(showDetails ? VISIBLE : GONE);

        courseCodeSection.setText(String.format("%s - %s", course.getCourseCode(), course.getSection()));
        courseName.setText(course.getName());
        teacherName.setText(course.getTeacherName());
        year.setText(String.valueOf(course.getYear()));

        if (course.getSemester() != null) {
            semesterName.setText(course.getSemester().getSemesterName());
        }

    }
}
