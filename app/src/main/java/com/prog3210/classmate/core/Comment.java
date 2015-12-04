package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.prog3210.classmate.events.Event;

/**
 * Created by seanc on 12/3/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {
    public String getCommentBody() {
        return getString("commentBody");
    }
    public void setCommentBody(String body){
        put("commentBody", body);
    }

    public Event getCommentEvent(){
    }



    public static ParseQuery<Comment> getQuery(){
        return new ParseQuery(Comment.class);
    }
}