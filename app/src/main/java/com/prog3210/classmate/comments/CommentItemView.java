/*
    CommentItemView.java

    Defines the view items that are contained in the comments and assigns them values

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */

package com.prog3210.classmate.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;
import com.prog3210.classmate.core.ClassmateUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentItemView extends LinearLayout{

    //Keep a reference to views within this ItemView.
    private TextView userName;
    private TextView dateTime;
    private TextView body;
    
    public CommentItemView(Context context) {
        super(context);
        getViews();
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViews();
    }

    public CommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViews();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViews();
    }

    /***
     * Assigns values to the views text properties
     * @param comment
     *  the comment object to be used to populate the view items
     */
    public void update(Comment comment){
        Date createdDate = comment.getCreatedAt();
        ClassmateUser user = (ClassmateUser)comment.getCreator();

        try {
            dateTime.setText(String.format("%s @ %s",
                    new SimpleDateFormat("EEE MMM F", Locale.CANADA).format(createdDate),
                    new SimpleDateFormat("h:mm a", Locale.CANADA).format(createdDate)));
            userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            body.setText(comment.getCommentBody());
        } catch (Exception e) {
            LogHelper.logError(getContext(),
                    "CommentItemView","There was an error preparing comments",
                    e.getMessage());
        }
    }

    /***
     * Retrieves view objects from the comment_view
     */
    private void getViews(){
        userName = (TextView) findViewById(R.id.comment_user);
        dateTime = (TextView) findViewById(R.id.comment_datetime);
        body = (TextView) findViewById(R.id.comment_body);
    }
}
