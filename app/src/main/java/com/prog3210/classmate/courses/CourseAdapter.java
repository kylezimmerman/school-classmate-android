package com.prog3210.classmate.courses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseAdapter extends ParseQueryAdapter<Course> {

    public CourseAdapter(Context context) {
        super(context, createQueryFactory());
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

    private static QueryFactory<Course> createQueryFactory() {
        QueryFactory<Course> factory = new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                ParseQuery<Course> query = Course.getQuery();
                query.include("semester");

                return query;
            }
        };

        return factory;
    }
}
