package com.prog3210.classmate.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;

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
    public View getItemView(EventType eventType, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.spinner_list_item, null);
        }

        super.getItemView(eventType, view, parent);

        TextView listItem = (TextView)view.findViewById(R.id.item_name);
        listItem.setText(eventType.getTypeName());

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}