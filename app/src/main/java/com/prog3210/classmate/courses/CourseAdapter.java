/*
    CourseAdapter.java

    handle populating any lists that need to show courses

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.R;

public class CourseAdapter extends ParseQueryAdapter<Course> {

    private boolean showDetails = true;
    private boolean spinnerSupportEnabled = false;
    private String searchTerm;

    /***
     * allows user to decide to show details or not
     * @param showDetails True if the details (E.g. year, semester, teacher)
     */
    public void setShowDetails(boolean showDetails) {
        this.showDetails = showDetails;
    }

    /***
     * used to filter queries
     */
    public enum FilterMode {
        Joined,
        Unjoined
    }

    /***
     * Constructs CourseAdapter with a query factory
     * @param context Current activity context.
     * @param mode filterMode for query
     */
    public CourseAdapter(Context context, FilterMode mode) {
        super(context, createQueryFactory(mode));
    }

    /***
     * prepares search string to be used in query
     * @param query string that contains search query
     */
    public void setSearchTerm(String query) {
        searchTerm = query;

        if (searchTerm != null) {
            searchTerm = searchTerm.toLowerCase();
        }
    }

    /**
     * set spinnerSupport to true
     */
    public void enableSpinnerSupport() {
        spinnerSupportEnabled = true;
    }

    @Override
    public int getViewTypeCount() {
        return spinnerSupportEnabled ? 1 : 2;
    }

    @Override
    public int getItemViewType(int position) {
        Course course = getItem(position);

        boolean matchesFilter = (searchTerm == null || searchTerm.length() == 0)
            || course.getName().toLowerCase().contains(searchTerm)
            || course.getCourseCode().toLowerCase().contains(searchTerm)
            || course.getTeacherName().toLowerCase().contains(searchTerm);

        if (matchesFilter) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public View getItemView(Course course, View view, ViewGroup parent) {

        if (view == null) {
            view = View.inflate(getContext(), R.layout.course_list_item, null);
        }

        super.getItemView(course, view, parent);

        CourseItemView courseItemView = (CourseItemView) view;
        courseItemView.updateValues(course, showDetails);

        return view;
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.empty_view, null);
        }

        return v;
    }

    /***
     * queries the Course Parse object and filters it to only return courses that match the
     *  filter mode
     * @param filterMode enum that contains joined or unjoined
     * @return QueryFactory<Course> for any lists containing courses
     */
    private static QueryFactory<Course> createQueryFactory(final FilterMode filterMode) {
        return new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                ParseQuery<Course> query = Course.getQuery();
                query.include("semester");

                if (filterMode == FilterMode.Joined) {
                    query.whereEqualTo("members", ParseUser.getCurrentUser());
                } else if (filterMode == FilterMode.Unjoined) {
                    query.whereNotEqualTo("members", ParseUser.getCurrentUser());
                }

                query.orderByAscending("courseCode");

                return query;
            }
        };
    }
}
