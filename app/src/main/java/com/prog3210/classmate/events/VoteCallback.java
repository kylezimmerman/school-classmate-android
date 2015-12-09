/*
    VoteCallback.java

    This interface is used to help manage the cases for voting on an Event.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.events;

import com.parse.ParseException;

/**
 * Created by Justin on 2015-12-02.
 */
public interface VoteCallback  {
    int UPVOTE = 1;
    int DOWNVOTE = -1;
    int NEUTRAL = 0;

    /***
     * Base method for help in handling the voting actions on an Event.
     * @param voteResult The differential result on the user's voting action.
     * @param e ParseException for callbacks.
     */
    void done(int voteResult, ParseException e);
}