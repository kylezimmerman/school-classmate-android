package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Semester")
public class Semester extends ParseObject{
    public String getSemesterName() {
        return getString("semesterName");
    }
    public void setSemesterName(String semesterName) {
        put("semesterName", semesterName);
    }
}