/*
    CourseItemView.java

    handles the display of each item for any lists that show courses

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prog3210.classmate.R;

public class CourseItemView extends RelativeLayout {

    private TextView courseCodeSection;
    private TextView courseName;
    private TextView teacherName;
    private TextView semesterName;
    private TextView year;

    private View detailsView;

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

    /***
     * Gets the objects displayed in the layout for use.
     */
    private void getViews() {
        detailsView = findViewById(R.id.course_details);

        courseCodeSection = (TextView)findViewById(R.id.course_code);
        courseName = (TextView)findViewById(R.id.course_name);
        teacherName = (TextView)findViewById(R.id.teacher_name);
        semesterName = (TextView)findViewById(R.id.semester);
        year = (TextView)findViewById(R.id.year);
    }

    /***
     * Updates the Event layout to display updated information for the current Event.
     * @param course current course being used
     * @param showDetails bool
     */
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
