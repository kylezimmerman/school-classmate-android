/*
    SemesterAdapter.java

    An adapter that gets all of the Semester from parse and can be used as an adapter on a ListView

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;

public class SemesterAdapter extends ParseQueryAdapter<Semester> {

    /***
     * Creates an EventTypeAdapter that queries for ALL Semesters
     */
    public SemesterAdapter(Context context) {
        super(context, createQueryFactory());
    }

    //Helper function to create a Semester Query Factory that can be re-used to refresh the list
    private static QueryFactory<Semester> createQueryFactory() {
        //This can't throw an exception, so no try/catch. It just creates the factory, it doesn't use it

        return new QueryFactory<Semester>() {
            @Override
            public ParseQuery<Semester> create() {
                ParseQuery<Semester> query = Semester.getQuery();

                query.orderByAscending("semesterName");

                return query;
            }
        };
    }

    @Override
    public View getItemView(Semester semester, View view, ViewGroup parent) {
        try {
            //If the view doesn't already exist, create a new one
            if (view == null) {
                view = View.inflate(getContext(), R.layout.spinner_list_item, null);
            }

            super.getItemView(semester, view, parent);

            //Set the item's text to the semester's name
            TextView listItem = (TextView) view.findViewById(R.id.item_name);
            listItem.setText(semester.getSemesterName());
        } catch (Exception ex) {
            LogHelper.logError(getContext(), "SemesterAdapter", "Error displaying a semeseter", ex.getMessage());
        }

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
