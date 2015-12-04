package com.prog3210.classmate.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;

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

                query.orderByAscending("semesterName");

                return query;
            }
        };

        return factory;
    }

    @Override
    public View getItemView(Semester semester, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.spinner_list_item, null);
        }

        super.getItemView(semester, view, parent);

        TextView listItem = (TextView)view.findViewById(R.id.item_name);
        listItem.setText(semester.getSemesterName());

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
