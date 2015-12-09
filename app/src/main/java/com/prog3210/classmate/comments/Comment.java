/*
    Comment.java

    Helper class that simplifies getting and setting properties of the comment parseObject

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */

package com.prog3210.classmate.comments;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prog3210.classmate.events.Event;

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