package com.prog3210.classmate.comments;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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
        return (Event) getParseObject("commentEvent");
    }
    public void setCommentEvent(Event event){
        put("commentEvent", event);
    }

    public ParseUser getCreator(){
        return getParseUser("creator");
    }
    public void setCreator(ParseUser creator){
        put("creator", creator);
    }

    public static ParseQuery<Comment> getQuery(){
        return new ParseQuery(Comment.class);
    }
}