package com.prog3210.classmate.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prog3210.classmate.R;
import com.prog3210.classmate.core.ClassmateUser;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by seanc on 12/4/2015.
 */
public class CommentItemView extends LinearLayout{

    TextView userName;
    TextView dateTime;
    TextView body;


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

    public void update(Comment comment){
        getViews();

        Date createdDate = comment.getCreatedAt();
        ClassmateUser user = (ClassmateUser)comment.getCreator();

        dateTime.setText(String.format("%s @ %s",
                new SimpleDateFormat("EEE MMM F").format(createdDate),
                new SimpleDateFormat("h:m a").format(createdDate)));
        userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        body.setText(comment.getCommentBody());

    }


    private void getViews(){
        userName = (TextView) findViewById(R.id.comment_user);
        dateTime = (TextView) findViewById(R.id.comment_datetime);
        body = (TextView) findViewById(R.id.comment_body);
    }
}
