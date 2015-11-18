package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by kzimmerman on 11/18/2015.
 */
@ParseClassName("_User")
public class ClassmateUser extends ParseUser {

    public String getFirstName() {
        return getString("firstName");
    }
    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    public String getLastName() {
        return getString("lastName");
    }
    public void setLastName(String firstName) {
        put("lastName", firstName);
    }
}
