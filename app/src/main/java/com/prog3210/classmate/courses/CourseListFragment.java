package com.prog3210.classmate.courses;


import android.app.Activity;
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
import android.widget.Toast;


import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.parse.SaveCallback;

import com.prog3210.classmate.R;

/**
 * A simple {@link Fragment} subclass.
    */
    public class CourseListFragment extends Fragment {

        private CourseAdapter courseAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_course_list, container, false);

            courseAdapter = new CourseAdapter(getActivity(), true);

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
                public boolean onItemLongClick(AdapterView<?> adapterView, View v, int position, long id) {
                    final Course course = courseAdapter.getItem(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.confirm_leave_course_title);
                    builder.setMessage(String.format(getResources().getString(R.string.confirm_leave_course), course.getCourseCode(), course.getName()));
                    builder.setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            course.leave(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        courseAdapter.loadObjects();
                                        Snackbar.make(view, "Left course", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                course.addMember(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        courseAdapter.loadObjects();
                                                    }
                                                });
                                            }
                                        }).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Error leaving course", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
                    startActivityForResult(joinCourseIntent, 1);
                }
            });

            courseList.setAdapter(courseAdapter);

            return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){

                ParseQuery<Course> query = ParseQuery.getQuery("Course");

                query.getInBackground(data.getStringExtra("courseJoined"), new GetCallback<Course>() {
                    @Override
                    public void done(final Course course, ParseException e) {
                        if (e == null) {

                            final View coordinatorLayout = getView().findViewById(R.id.snackbar);
                            Snackbar.make(coordinatorLayout, "You have Joined " + course.getCourseCode(), Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            course.leave(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    Toast.makeText(getActivity(), "User removed from course", Toast.LENGTH_SHORT).show();
                                                    courseAdapter.loadObjects();
                                                }
                                            });
                                        }
                                    }).show();
                            courseAdapter.loadObjects();
                        } else {

                        }
                    }
                });
            }
        }

    }
}
