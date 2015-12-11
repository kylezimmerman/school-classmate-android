/*
    EventListFragment.java

    This class handles the actions and display of the Event feed on the MainActivity view.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.events;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends BaseFragment {
    private EventAdapter eventAdapter;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_event_list, container, false);

            // Gets view objects for current layout
            final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
            ListView eventList = (ListView) view.findViewById(R.id.event_list);
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_event_button);

            // Sets the OnQueryLoadListener to properly display the refresh animation
            eventAdapter = new EventAdapter(getActivity(), ParseUser.getCurrentUser());
            eventAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Event>() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(List<Event> list, Exception e) {
                    pullToRefresh.setRefreshing(false);
                }
            });

            // Sets the EventAdapter
            eventList.setAdapter(eventAdapter);

            // Sets OnItemClickListener for the Event feed items
            eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent viewEventDetailsIntent = new Intent(getActivity(), EventViewActivity.class);
                    viewEventDetailsIntent.putExtra("event_id", eventAdapter.getItem(position).getObjectId());
                    startActivity(viewEventDetailsIntent);
                }
            });

            // Enables pull-to-refresh action on the Event feed
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    eventAdapter.loadObjects();
                }
            });

            // Loads the objects for display in the Event feed
            eventAdapter.loadObjects();

            // Sets OnClickListener action for creating a new Event from the Event feed
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addEventIntent = new Intent(getActivity(), CreateEventActivity.class);
                    startActivity(addEventIntent);
                }
            });

            return view;
        } catch (Exception e) {
            LogHelper.logError(getContext(), "EventListFragment", "Error generating Event feed.", e.getMessage());
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Loads the objects for display in the Event feed
        eventAdapter.loadObjects();
    }
}
