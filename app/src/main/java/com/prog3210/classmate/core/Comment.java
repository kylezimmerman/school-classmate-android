package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
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
        return (Event) getParseObject("eventCommment");
    }
    public void setCommentEvent(Event event){
        put("eventComment", event);
    }

    public ParseUser getCreator(){
        return getParseUser("creator");
    }
    public void setCreator(ParseUser creator){
        put("creator", creator);
    }

    public void save(Event event,SaveCallback callback){
        ParseRelation<ClassmateUser> userRelation = getRelation("creator");
        userRelation.add(ClassmateUser.getCurrentUser());

        ParseRelation<Event> eventRelation = getRelation("eventComment");
        eventRelation.add(event);

        saveInBackground(callback);
    }

    public static ParseQuery<Comment> getQuery(){
        return new ParseQuery(Comment.class);
    }
}