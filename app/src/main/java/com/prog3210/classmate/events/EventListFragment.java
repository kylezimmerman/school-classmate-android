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
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends BaseFragment {


    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pull_to_refresh);
        ListView eventList = (ListView)view.findViewById(R.id.event_list);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.add_event_button);


        final EventAdapter eventAdapter = new EventAdapter(getActivity(), ParseUser.getCurrentUser());
        eventAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Event>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Event> list, Exception e) {
                pullToRefresh.setRefreshing(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventActivityIntent = new Intent(getActivity(), CreateEventActivity.class);

                startActivity(eventActivityIntent);
            }
        });

        eventList.setAdapter(eventAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent viewEventDetailsIntent = new Intent(getActivity(), EventViewActivity.class);
                viewEventDetailsIntent.putExtra("event_id", eventAdapter.getItem(position).getObjectId());
                startActivity(viewEventDetailsIntent);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventAdapter.loadObjects();
            }
        });

        eventAdapter.loadObjects();
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(addEventIntent);
            }
        });

        return view;
    }


}
