package com.prog3210.classmate.courses;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prog3210.classmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        CourseAdapter courseAdapter = new CourseAdapter(getActivity());

        ListView courseList = (ListView)view.findViewById(R.id.course_list);
        courseList.setAdapter(courseAdapter);

        return view;
    }


}
