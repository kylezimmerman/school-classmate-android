package com.prog3210.classmate.events;

import com.parse.ParseException;

/**
 * Created by Justin on 2015-12-02.
 */
public interface VoteCallback  {
    public void done(int voteDifferential, ParseException e);
}