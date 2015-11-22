package com.prog3210.classmate.courses;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;
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

            courseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    final Course course = courseAdapter.getItem(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.confirm_leave_course_title);
                    builder.setMessage(String.format(getResources().getString(R.string.confirm_leave_course), course.getCourseCode(), course.getName()));
                    builder.setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            course.leave(null);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, null);
                    builder.show();

                    return true;
                }
            });

            FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.join_course_button);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent joinCourseIntent = new Intent(getActivity(), JoinCourseActivity.class);
                    startActivity(joinCourseIntent);
                }
            });

            courseList.setAdapter(courseAdapter);

            return view;
    }


}
