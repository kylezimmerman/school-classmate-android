package com.prog3210.classmate.courses;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

            final CourseAdapter courseAdapter = new CourseAdapter(getActivity());

        ListView courseList = (ListView)view.findViewById(R.id.course_list);

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Course course = courseAdapter.getItem(position);

                Intent viewCourseIntent = new Intent(getActivity(), CourseViewActivity.class);
                viewCourseIntent.putExtra("course_id", course.getObjectId());
                startActivity(viewCourseIntent);
            }
        });

        courseList.setAdapter(courseAdapter);

        return view;
    }


}
