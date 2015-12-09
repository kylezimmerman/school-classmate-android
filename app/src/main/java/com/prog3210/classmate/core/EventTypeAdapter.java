/*
    EventTypeAdapter.java

    An adapter that gets all of the EventTypes from parse and can be used as an adapter on a ListView

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;

public class EventTypeAdapter extends ParseQueryAdapter<EventType> {

    /***
     * Creates an EventTypeAdapter that queries for ALL event types
     */
    public EventTypeAdapter(Context context) {
        super(context, createQueryFactory());
    }

    //Helper function to create a EventType Query Factory that can be re-used to refresh the list
    private static QueryFactory<EventType> createQueryFactory() {
        QueryFactory<EventType> factory = new QueryFactory<EventType>() {
            @Override
            public ParseQuery<EventType> create() {

                //Query for a list of EventTypes ordered alphabetically
                ParseQuery<EventType> query = EventType.getQuery();
                query.orderByAscending("typeName");

                return query;
            }
        };
        return factory;
    }

    @Override
    public View getItemView(EventType eventType, View view, ViewGroup parent) {
        // If there was no view that already existed, create a new one
        if (view == null) {
            view = View.inflate(getContext(), R.layout.spinner_list_item, null);
        }

        super.getItemView(eventType, view, parent);

        //Sets the list item text to the event item name
        TextView listItem = (TextView)view.findViewById(R.id.item_name);
        listItem.setText(eventType.getTypeName());

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}