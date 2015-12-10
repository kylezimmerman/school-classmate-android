/*
    EventAdapter.java

    This class generates the ParseQueryAdapters for the Event object for querying an Event in
    different ways.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.events;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;
import com.prog3210.classmate.courses.Course;

/**
 * Created by kzimmerman on 11/25/2015.
 */
public class EventAdapter extends ParseQueryAdapter<Event> {

    /***
     * Constructs EventAdapter with a query factory for ParseUser's Event feed.
     * @param context Current activity context.
     * @param user Current ParseUser.
     */
    public EventAdapter(Context context, ParseUser user) {
        // Nothing here throw an Exception, so no try-catch needed
        super(context, createQueryFactoryForUser(user));
        setPaginationEnabled(false);
    }

    /***
     * Constructs EventAdapter with a query factory for Course's Event feed.
     * @param context Current activity context.
     * @param course Current viewed Course.
     */
    public EventAdapter(Context context, Course course) {
        // Nothing here throw an Exception, so no try-catch needed
        super(context, createQueryFactoryForCourse(course));
        setPaginationEnabled(false);
    }

    /***
     * Override getItemView(...) for custom displaying an item.
     * @param event Event to generate display view for.
     * @param view View to display Event information in.
     * @param parent View to contain the display view.
     * @return Returns generated display View.
     */
    @Override
    public View getItemView(Event event, View view, ViewGroup parent) {
        try {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.event_list_item, null);
            }

            super.getItemView(event, view, parent);

            ((EventItemView) view).update(event);
        } catch (Exception e) {
            LogHelper.logError(null, "EventAdapter", "Error getting item view.", e.getMessage());
        }

        return view;
    }

    // Creates QueryFactory<Event> for a user to view in their feed.
    private static QueryFactory<Event> createQueryFactoryForUser(final ParseUser user) {
        // Nothing here throw an Exception, so no try-catch needed
        QueryFactory<Event> factory = new QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                ParseQuery<Event> query = Event.getQuery();
                query.include("course");
                query.whereMatchesKeyInQuery("course", "objectId", Course.getQuery().whereEqualTo("members", user));
                query.orderByAscending("dueDate");
                return query;
            }
        };

        return factory;
    }

    // Creates QueryFactory<Event> for a Course to display in the Event feed for that Course.
    private static QueryFactory<Event> createQueryFactoryForCourse(final Course course) {
        // Nothing here throw an Exception, so no try-catch needed
        QueryFactory<Event> factory = new QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                ParseQuery<Event> query = Event.getQuery();
                query.include("course");
                query.whereEqualTo("course", course);
                query.orderByAscending("dueDate");
                return query;
            }
        };

        return factory;
    }
}
