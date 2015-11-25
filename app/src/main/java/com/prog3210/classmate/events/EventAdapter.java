package com.prog3210.classmate.events;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.courses.Course;

/**
 * Created by kzimmerman on 11/25/2015.
 */
public class EventAdapter extends ParseQueryAdapter<Event> {

    public EventAdapter(Context context, ParseUser user) {
        super(context, createQueryFactoryForUser(user));
    }

    public EventAdapter(Context context, Course course) {
        super(context, createQueryFactoryForCourse(course));
    }

    @Override
    public View getItemView(Event event, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.event_list_item, null);
        }

        super.getItemView(event, view, parent);

        ((EventItemView)view).update(event);

        return view;
    }

    private static QueryFactory<Event> createQueryFactoryForUser(final ParseUser user) {
        QueryFactory<Event> factory = new QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                ParseQuery<Event> query = Event.getQuery();
                query.whereEqualTo("members", user);
                query.include("course");
                return query;
            }
        };

        return factory;
    }

    private static QueryFactory<Event> createQueryFactoryForCourse(final Course course) {
        QueryFactory<Event> factory = new QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                ParseQuery<Event> query = Event.getQuery();
                query.whereEqualTo("course", course.getObjectId());
                query.include("course");
                return query;
            }
        };

        return factory;
    }
}
