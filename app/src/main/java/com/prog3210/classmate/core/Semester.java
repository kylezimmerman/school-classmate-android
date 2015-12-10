/*
    Semester.java

    Represents a semester (E.g. Spring, Summer, Fall)

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Semester")
public class Semester extends ParseObject{
    //All of the ParseObject getXXXX() return null instead of throw an exception
    // so no need for try/catch

    /***
     * Gets the name of the semester
     * @return The name of the semester
     */
    public String getSemesterName() {
        return getString("semesterName");
    }

    /***
     * Sets the name of the semseter
     * @param semesterName The new name of the semester
     */
    public void setSemesterName(String semesterName) {
        put("semesterName", semesterName);
    }

    /***
     * Gets a query that is set up for the Semester class.
     * This avoids having to deal with the non-generic ParseQuery.
     * @return A query on the Semester class.
     */
    public static ParseQuery<Semester> getQuery(){
        return new ParseQuery(Semester.class);
    }
}