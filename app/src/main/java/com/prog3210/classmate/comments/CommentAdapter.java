/*
    CommentAdapter.java

    handles the populating of the comment listviews

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate.comments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;
import com.prog3210.classmate.events.Event;

public class CommentAdapter extends ParseQueryAdapter<Comment> {
    /***
     * Creates a CommentAdapter that retrieves comments for the supplied event.
     * @param event
     *  the event that the comment is a part of
     */
    public CommentAdapter(Context context, Event event) {
        super(context, createQueryFactory(event));
    }

    /***
     *  Executes query that returns all the comments that belong to event that was passed in
     * @param event
     *  event that is used in the query to narrow down the returned comments
     * @return
     *  the list of comments
     */
    private static QueryFactory<Comment> createQueryFactory(final Event event) {
        return new QueryFactory<Comment>() {
            @Override
            public ParseQuery<Comment> create() {
                ParseQuery<Comment> query = Comment.getQuery();
                query.include("creator");
                query.whereEqualTo("commentEvent", event);

                return query;
            }
        };
    }

    @Override
    public View getItemView(Comment comment, View view, ViewGroup parent) {
        try {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.comment_list_item, null);
            }
        } catch (Exception e) {
            LogHelper.logError(getContext(), "CommentAdapter", "Error showing comments", e.getMessage());
        }

        super.getItemView(comment, view, parent);

        try {
            ((CommentItemView)view).update(comment);
        } catch (Exception e) {
            LogHelper.logError(getContext(), "CommentAdapter", "Error showing comments", e.getMessage());
        }

        return view;
    }
}
