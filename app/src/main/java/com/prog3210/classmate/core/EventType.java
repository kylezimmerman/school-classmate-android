package com.prog3210.classmate.core;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("EventType")
public class EventType extends ParseObject{
    public String getTypeName() {
        return getString("typeName");
    }
    public void setTypeName(String typeName) {
        put("typeName", typeName);
    }
}
