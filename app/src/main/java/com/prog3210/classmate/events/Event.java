/*
    Event.java

    This is the class for the Event object in the Parse database.
    It has the getters and setters for all fields of the Event object, methods to build
    an Event query when needed, and methods for handling voting on an Event.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.events;

import com.parse.CountCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.courses.Course;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Event")
public class Event extends ParseObject {
    //Note: Not all of the ParseObject.getXXXX() methods return null if the key is not found
    //      So no need for try/catch in each method

    /***
     * Gets the Course the Event belongs to.
     * @return Returns the Course object the Event belongs to.
     */
    public Course getCourse() {
        return (Course) getParseObject("course");
    }
    /***
     * Sets the Course the Event belongs to.
     * @param course The Course the Event belongs to.
     */
    public void setCourse(Course course) {
        put("course", course);
    }

    /***
     * Gets the ParseUser Creator of the Event.
     * @return Returns the ParseUser Creator of an Event.
     */
    public ParseUser getCreator() {
        return getParseUser("creator");
    }

    /***
     * Sets the ParseUser Creator of the Event.
     * @param creator The ParseUser that created the Event.
     */
    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }

    /***
     * Gets the description of the Event.
     * @return Returns the String description of the Event.
     */
    public String getDescription() {
        return getString("description");
    }

    /***
     * Sets the description of the Event.
     * @param description The description of the Event.
     */
    public void setDescription(String description) {
        put("description", description);
    }

    /***
     * Gets the name of the Event.
     * @return Returns the String name of the Event.
     */
    public String getName() {
        return getString("name");
    }

    /***
     * Sets the name of the Event.
     * @param name The name of the event.
     */
    public void setName(String name) {
        put("name", name);
    }

    // Method to create the SaveCallback when upvoting
    private SaveCallback createUpvoteCallback(final VoteCallback callback) {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                try {
                    callback.done(VoteCallback.UPVOTE, e);
                } catch (Exception ex) {
                    LogHelper.logError(null, "Event", "Error upvoting event.", ex.getMessage());
                }
            }
        };
    }

    // Method to create the SaveCallback when undoing a vote
    private SaveCallback createNeutralCallback(final VoteCallback callback) {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                try {
                    callback.done(VoteCallback.NEUTRAL, e);
                } catch (Exception ex) {
                    LogHelper.logError(null, "Event", "Error neutralizing votes.", ex.getMessage());
                }
            }
        };
    }

    //Method to creeate the SaveCallback when downvoting
    private SaveCallback createDownvoteCallback(final VoteCallback callback) {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                try {
                    callback.done(VoteCallback.DOWNVOTE, e);
                } catch (Exception ex) {
                    LogHelper.logError(null, "Event", "Error downvoting event.", ex.getMessage());
                }
            }
        };
    }

    /***
     * Gets the number of Upvotes for an Event.
     * @return Returns the integer Upvotes for an event.
     */
    public int getUpvotes() { return getInt("upvotes"); }

    /***
     * Passes through the upvoteQuery results for if the user has upvoted the Event.
     * @param callback The CountCallback used when a user has upvoted the Event.
     */
    public void hasUpvoted(CountCallback callback) {
        // Nothing here throw an Exception, so no try-catch needed
        ParseRelation<ParseUser> upvoters = getRelation("upvoters");
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> upvoteQuery = upvoters.getQuery();
        upvoteQuery.whereEqualTo("objectId", currentUser.getObjectId());
        upvoteQuery.countInBackground(callback);
    }

    /***
     * When the user upvotes an Event.
     * @param callback The VoteCallback used when a user has upvoted.
     */
    public void upvote(final VoteCallback callback) {
        final ParseRelation<ParseUser> upvoters = getRelation("upvoters");
        final ParseRelation<ParseUser> downvoters = getRelation("downvoters");

        hasUpvoted(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                try {
                    if (count == 1) {
                        increment("upvotes", -1);
                        upvoters.remove(ParseUser.getCurrentUser());

                        saveInBackground(createNeutralCallback(callback));
                    } else {
                        hasDownvoted(new CountCallback() {
                            @Override
                            public void done(int count, ParseException e) {
                                if (count == 1) {
                                    increment("downvotes", -1);
                                    downvoters.remove(ParseUser.getCurrentUser());
                                }
                                increment("upvotes", 1);
                                upvoters.add(ParseUser.getCurrentUser());

                                saveInBackground(createUpvoteCallback(callback));
                            }
                        });
                    }
                } catch (Exception ex) {
                    LogHelper.logError(null, "Event", "Error upvoting event.", ex.getMessage());
                }
            }
        });
    }

    /***
     * Gets the number of Downvotes for an Event.
     * @return Returns the integer Downvotes for an Event.
     */
    public int getDownvotes() { return getInt("downvotes"); }

    /***
     * Passes through the downvoteQuery results for if the user has downvoted the Event.
     * @param callback The CountCallback used when a user has downvoted the Event.
     */
    public void hasDownvoted(CountCallback callback) {
        // Nothing here throw an Exception, so no try-catch needed
        ParseRelation<ParseUser> downvoters = getRelation("downvoters");
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> downvoteQuery = downvoters.getQuery();
        downvoteQuery.whereEqualTo("objectId", currentUser.getObjectId());
        downvoteQuery.countInBackground(callback);
    }

    /***
     * When a user downvotes an Event.
     * @param callback he VoteCallback used when a user has downvoted.
     */
    public void downvote(final VoteCallback callback) {
        final ParseRelation<ParseUser> upvoters = getRelation("upvoters");
        final ParseRelation<ParseUser> downvoters = getRelation("downvoters");

        hasDownvoted(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                try {
                    if (count == 1) {
                        increment("downvotes", -1);
                        downvoters.remove(ParseUser.getCurrentUser());

                        saveInBackground(createNeutralCallback(callback));
                    } else {
                        hasUpvoted(new CountCallback() {
                            @Override
                            public void done(int count, ParseException e) {
                                if (count == 1) {
                                    increment("upvotes", -1);
                                    upvoters.remove(ParseUser.getCurrentUser());
                                }
                                increment("downvotes", 1);
                                downvoters.add(ParseUser.getCurrentUser());

                                saveInBackground(createDownvoteCallback(callback));
                            }
                        });
                    }
                } catch (Exception ex) {
                    LogHelper.logError(null, "Event", "Error downvoting event.", ex.getMessage());
                }
            }
        });
    }

    /***
     * Gets the FinalGradeWeight of the Event.
     * @return Returns the FinalGradeWeight of the Event as a double.
     */
    public double getFinalGradeWeight() {
        return getDouble("finalGradeWeight");
    }
    /***
     * Sets the FinalGradeWeight of the Event.
     * @param gradeWeight The double grade weighting of the Event.
     */
    public void setFinalGradeWeight(double gradeWeight) {
        put("finalGradeWeight", gradeWeight);
    }

    /***
     * Gets the DueDate of the Event.
     * @return Returns the Date due date of the Event.
     */
    public Date getDate() {
        return getDate("dueDate");
    }
    /***
     * Sets the due date of the Event.
     * @param date The due date of the Event.
     */
    public void setDate(Date date) {
        put("dueDate", date);
    }

    /***
     * Gets String formatted due date for the Event.
     * @return
     */
    public String getDateString() {
        DateFormat format = new SimpleDateFormat("EEE MMM d", Locale.CANADA);

        try {
            return format.format(getDate());
        } catch (Exception e) {
            LogHelper.logError(null, "Event", "Error getting date.", e.getMessage());
            return "";
        }
    }

    /***
     * Gets the Event query to get Events from Parse.
     * @return Returns a ParseQuery for getting Events.
     */
    public static ParseQuery<Event> getQuery() {
        return new ParseQuery<Event>(Event.class);
    }

    /***
     * Gets the EventType of the Event.
     * @return Returns the EventType of the Event.
     */
    public EventType getEventType() {
        return (EventType)getParseObject("eventtype");
    }

    /***
     * Sets the EventType of the Event.
     * @param eventType The EventType of the Event.
     */
    public void setEventType(EventType eventType) { put("eventtype", eventType); }
}