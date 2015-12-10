/*
    Course.java

    Helper class that simplifies getting and setting properties of the course parseObject

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.courses;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prog3210.classmate.core.ClassmateUser;
import com.prog3210.classmate.core.Semester;

@ParseClassName("Course")
public class Course extends ParseObject {
    /***
     * gets the course code for the course
     * @return the string of the coursecode
     */
    public String getCourseCode() {
        return getString("courseCode");
    }

    /***
     * sets the course code for the course
     * @param courseCode the course code to be set
     */
    public void setCourseCode(String courseCode) {
        put("courseCode", courseCode);
    }

    /***
     * gets the section of the course
     * @return the string of the section of the corse
     */
    public String getSection() {
        return getString("section");
    }

    /***
     * sets the section of the course
     * @param section the section of the course
     */
    public void setSection(String section) {
        put("section", section);
    }

    /***
     * gets the year the course is running in
     * @return int of the year
     */
    public int getYear() {
        return getInt("year");
    }

    /***
     * sets the year the course is in
     * @param year the year of the course
     */
    public void setYear(int year) {
        put("year", year);
    }

    /***
     * gets the semester the course is running in
     * @return semester parse object
     */
    public Semester getSemester() {
        return (Semester)getParseObject("semester");
    }

    /***
     * sets the semester the course is in
     * @param semester a Semester parse object
     */
    public void setSemester(Semester semester) { put("semester", semester); }

    /***
     * gets the name of the course
     * @return sring of the name of the course
     */
    public String getName() {
        return getString("name");
    }

    /***
     * sets the name of the course
     * @param name the name of the course
     */
    public void setName(String name) {
        put("name", name);
    }

    /***
     * gets the name of the teacher of the course
     * @return string of the teachers name
     */
    public String getTeacherName() {
        return getString("teacherName");
    }

    /***
     * sets the name of the teacher of the course
     * @param teacherName teachers names
     */
    public void setTeacherName(String teacherName) {
        put("teacherName", teacherName);
    }

    /***
     * gets the creator of the course
     * @return ParseUser Object of the creator
     */
    public ParseUser getCreator() {
        return getParseUser("creator");
    }

    /***
     * sets the creator of the course
     * @param creator creator ParseUser Object
     */
    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }

    /***
     * removes current user from the course
     * @param saveCallback call back that will run after calling this method
     */
    public void leave(SaveCallback saveCallback) {
        ParseRelation<ClassmateUser> members = getRelation("members");
        members.remove(ClassmateUser.getCurrentUser());

        ParsePush.unsubscribeInBackground("course_" + getObjectId());

        saveInBackground(saveCallback);
    }

    /***
     * prepare a ParseQuery for the course object
     * @return a ParseQuery object for course
     */
    public static ParseQuery<Course> getQuery() {
        return new ParseQuery<Course>(Course.class);
    }

    /***
     * adds current user to the course
     * @param saveCallback call back that will run after calling this method
     */
    public void join(SaveCallback saveCallback){

        ParseRelation<ClassmateUser> members = getRelation("members");
        members.add(ClassmateUser.getCurrentUser());

        ParsePush.subscribeInBackground("course_" + getObjectId());
        saveInBackground(saveCallback);
    }
}
