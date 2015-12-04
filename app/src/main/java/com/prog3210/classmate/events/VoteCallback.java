package com.prog3210.classmate.events;

import com.parse.ParseException;

/**
 * Created by Justin on 2015-12-02.
 */
public interface VoteCallback  {
    public static final int UPVOTE = 1;
    public static final int DOWNVOTE = -1;
    public static final int NEUTRAL = 0;
    public void done(int voteResult, ParseException e);
}