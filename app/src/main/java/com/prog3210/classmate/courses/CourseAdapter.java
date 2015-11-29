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
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseAdapter extends ParseQueryAdapter<Course> implements Filterable {

    boolean showDetails = true;

    private List<Course> courseList;

    public void setShowDetails(boolean showDetails) {
        this.showDetails = showDetails;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                List<Course> resultCourseList = new ArrayList<>();

                for (Course course: courseList) {
                    if (course.getCourseCode().contains(charSequence) || course.getName().contains(charSequence)){
                        resultCourseList.add(course);
                    }

                }
                results.values = resultCourseList;

                return results;
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

        this.addOnQueryLoadListener(new OnQueryLoadListener<Course>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Course> list, Exception e) {
                courseList = list;
            }
        });
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getItemView(Course course, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.course_list_item, null);
        }

        super.getItemView(course, view, parent);

        CourseItemView courseItemView = (CourseItemView)view;
        courseItemView.updateValues(course, showDetails);

        return view;
    }

    private static QueryFactory<Course> createQueryFactory(final FilterMode filterMode) {
        QueryFactory<Course> factory = new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                ParseQuery<Course> query = Course.getQuery();
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
