package com.prog3210.classmate.courses;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.prog3210.classmate.core.Semester;

@ParseClassName("Course")
public class Course extends ParseObject {
    public String getCourseCode() {
        return getString("courseCode");
    }
    public void setCourseCode(String courseCode) {
        put("courseCode", courseCode);
    }

    public String getSection() {
        return getString("section");
    }
    public void setSection(String section) {
        put("section", section);
    }

    public int getYear() {
        return getInt("year");
    }
    public void setYear(int year) {
        put("year", year);
    }

    public Semester getSemester() {
        return (Semester)getParseObject("semester");
    }
    public void setSemester(Semester semester) { put("semester", semester); }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
    }
    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }
}
