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

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseAdapter extends ParseQueryAdapter<Course> implements Filterable {

    private static ParseQuery<Course> query;

    @Override
    public Filter getFilter() {
        final ParseQuery<Course> querySearch = query;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                querySearch.whereContains("courseCode", charSequence.toString());

                //FilterResults results = FilterResults
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }

    public enum FilterMode {
        All,
        Joined,
        Unjoined
    }

    public CourseAdapter(Context context, FilterMode mode) {
        super(context, createQueryFactory(mode));
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

    private static QueryFactory<Course> createQueryFactory(final FilterMode filterMode) {
        QueryFactory<Course> factory = new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                query = Course.getQuery();
                query.include("semester");

                if (filterMode == FilterMode.Joined) {
                    query.whereEqualTo("members", ParseUser.getCurrentUser());
                } else if (filterMode == FilterMode.Unjoined) {
                    query.whereNotEqualTo("members", ParseUser.getCurrentUser());
                }

                return query;
            }
        };

        return factory;
    }
}
