package com.prog3210.classmate.courses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scoombes on 11/29/2015.
 */
public class CourseSpinnerAdapter extends ParseQueryAdapter<Course>{

    public CourseSpinnerAdapter(Context context) {
        super(context, createQueryFactory());
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getItemView(Course course, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.course_list_item_create_event, null);
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
                query.whereEqualTo("members", ParseUser.getCurrentUser());

                return query;
            }
        };
        return factory;
    }
}
