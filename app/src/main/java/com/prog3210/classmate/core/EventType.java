/*
    EventType.java

    Represents a type of an event (E.g. Test, Assignment, Exam, etc.)

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("EventType")
public class EventType extends ParseObject{

    /***
     * Gets the name of the type of event (E.g. Test, Assignment, Exam)
     * @return Returns the name of this event type.
     */
    public String getTypeName() {
        return getString("typeName");
    }

    /***
     * Sets the name of this event type
     * @param typeName The new name of the event type
     */
    public void setTypeName(String typeName) {
        put("typeName", typeName);
    }


    /***
     * Gets a query that is set up for the EventType class.
     * This avoids having to deal with the non-generic ParseQuery.
     * @return A query on the EventType class.
     */
    public static ParseQuery<EventType> getQuery(){
        return new ParseQuery(EventType.class);
    }
}
