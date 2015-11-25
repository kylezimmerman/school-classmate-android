package com.prog3210.classmate.core;

import android.content.Context;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by seanc on 11/25/2015.
 */
public class EventTypeAdapter extends ParseQueryAdapter<EventType> {
    public EventTypeAdapter(Context context) {
        super(context, createQueryFactory());
    }

    private static QueryFactory<EventType> createQueryFactory() {
        QueryFactory<EventType> factory = new QueryFactory<EventType>() {
            @Override
            public ParseQuery<EventType> create() {
                ParseQuery<EventType> query = EventType.getQuery();

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