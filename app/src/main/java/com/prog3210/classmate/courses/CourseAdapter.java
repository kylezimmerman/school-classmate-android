package com.prog3210.classmate.courses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseAdapter extends ParseQueryAdapter<Course> {

    public CourseAdapter(Context context) {
        this(context, false);
    }

    public CourseAdapter(Context context, boolean showOnlyJoinedCourses) {
        super(context, createQueryFactory(showOnlyJoinedCourses));
    }

    @Override
    public View getItemView(Course course, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.course_list_item, null);
        }

        super.getItemView(course, view, parent);

        ((CourseItemView)view).updateValues(course);

        return view;
    }

    private static QueryFactory<Course> createQueryFactory(final boolean showOnlyJoinedCourses) {
        QueryFactory<Course> factory = new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                ParseQuery<Course> query = Course.getQuery();
                query.include("semester");

                if (showOnlyJoinedCourses) {
                    query.whereEqualTo("members", ParseUser.getCurrentUser());
                }

                return query;
            }
        };

        return factory;
    }
}
