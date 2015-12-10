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

    /***
     * Gets the body message of the comment
     * @return The string of the body's content
     */
    public String getCommentBody() {
        return getString("commentBody");
    }

    /***
     * Sets the message body of the comment
     * @param body The message body of the comment
     */
    public void setCommentBody(String body){
        put("commentBody", body);
    }

    /***
     * Gets the Event that this comment is for
     * @return The Event object that this comment is on
     */
    public Event getCommentEvent(){
        return (Event) getParseObject("commentEvent");
    }

    /***
     * Sets the event that this comment is for
     * @param event The event that the comment is on
     */
    public void setCommentEvent(Event event){
        put("commentEvent", event);
    }

    /***
     * Gets the User the created this comment
     *
     * Note that .include("creator") must have called for the query that retrieved this comment
     *
     * @return The user that created this post.
     */
    public ParseUser getCreator(){
        return getParseUser("creator");
    }

    /***
     * Sets the User that created this comment
     * @param creator The user that created this comment
     */
    public void setCreator(ParseUser creator){
        put("creator", creator);
    }

    /***
     * Gets a query that is set up for the Comment type.
     * This avoids having to deal with the non-generic ParseQuery
     * @return A query for the Comment Class
     */
    public static ParseQuery<Comment> getQuery(){
        return new ParseQuery<>(Comment.class);
    }
}