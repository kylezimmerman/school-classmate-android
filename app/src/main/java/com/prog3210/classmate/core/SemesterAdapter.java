package com.prog3210.classmate.core;

import android.content.Context;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Justin on 2015-11-22.
 */
public class SemesterAdapter extends ParseQueryAdapter<Semester> {
    public SemesterAdapter(Context context) {
        super(context, createQueryFactory());
    }

    private static QueryFactory<Semester> createQueryFactory() {
        QueryFactory<Semester> factory = new QueryFactory<Semester>() {
            @Override
            public ParseQuery<Semester> create() {
                ParseQuery<Semester> query = Semester.getQuery();

                return query;
            }
        };

        return factory;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
