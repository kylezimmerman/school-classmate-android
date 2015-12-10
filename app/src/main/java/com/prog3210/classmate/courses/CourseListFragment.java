/*
    CourseListFragment.java

    displays a list of all the courses the current user is part of

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import com.prog3210.classmate.R;
import com.prog3210.classmate.core.BaseFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
    */
    public class CourseListFragment extends BaseFragment {

        private CourseAdapter courseAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_course_list, container, false);

            final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.loading);
            ListView courseList = (ListView)view.findViewById(R.id.course_list);
            FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.join_course_button);
            final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pull_to_refresh);

            //create a courseAdapter the retrieves only courses the user is part of
            courseAdapter = new CourseAdapter(getActivity(), CourseAdapter.FilterMode.Joined);
            courseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Course>() {
                @Override
                public void onLoading() {
                }

                @Override
                //sets a refresh action that allows user to pull down on screen to refresh their list of courses
                public void onLoaded(List<Course> objects, Exception e) {
                    progressBar.setVisibility(View.GONE);
                    pullToRefresh.setRefreshing(false);
                }
            });

            //listener for refresh
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    courseAdapter.loadObjects();
                }
            });

            courseList.setEmptyView(view.findViewById(R.id.empty_list_view));

            //takes user to course details page
            courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Course course = courseAdapter.getItem(position);

                    Intent viewCourseIntent = new Intent(getActivity(), CourseViewActivity.class);
                    viewCourseIntent.putExtra("course_id", course.getObjectId());
                    startActivity(viewCourseIntent);
                }
            });
            //allows user to leave a course
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
                                                course.join(new SaveCallback() {
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

            //listener of fab that will take users to a new activity to join a new course
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

                //retrieves the course the user just joined before being returned to this fragment
                Course.getQuery().getInBackground(data.getStringExtra("courseJoined"), new GetCallback<Course>() {
                    @Override
                    public void done(final Course course, ParseException e) {
                        if (e == null) {
                            //shows a snackback displaying the course the user just joined and give them the ability
                                //to undo their action if it was clicked by accident
                            Snackbar.make(getView(), "You have Joined " + course.getCourseCode(), Snackbar.LENGTH_LONG)
                                    //click event for the undo button on the snackback
                                    .setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            course.leave(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        courseAdapter.loadObjects();
                                                    } else {
                                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                                                    }
                                                }
                                            });
                                        }
                                    }).show();
                            courseAdapter.loadObjects();
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        }
    }
}
