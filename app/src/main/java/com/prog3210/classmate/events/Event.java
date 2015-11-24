package com.prog3210.classmate.events;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.prog3210.classmate.courses.Course;

@ParseClassName("Event")
public class Event extends ParseObject {
    public Course getCourse() {
        return (Course) getParseObject("course");
    }
    public void setCourse(Course course) {
        put("course", course);
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
    }
    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }

    public String getDescription() {
        return getString("description");
    }
    public void setDescription(String description) {
        put("description", description);
    }

    public int getDownvotes() {
        return getInt("downvotes");
    }
    public void incrementDownvotes() {
        increment("downvotes", 1);
    }
    public void decrementDownvotes() {
        increment("downvotes", -1);
    }
}