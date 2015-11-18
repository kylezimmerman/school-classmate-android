package com.prog3210.classmate.courses;

import android.content.Context;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseAdapter extends ParseQueryAdapter<Course> {

    public CourseAdapter(Context context) {
        super(context, createQueryFactory(), R.layout.course_list_item);
    }

    private static QueryFactory<Course> createQueryFactory() {
        QueryFactory<Course> factory = new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                return null;
            }
        };

        return factory;
    }
}
