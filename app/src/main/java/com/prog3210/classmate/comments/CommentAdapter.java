package com.prog3210.classmate.comments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.prog3210.classmate.R;
import com.prog3210.classmate.comments.Comment;
import com.prog3210.classmate.events.Event;

/**
 * Created by seanc on 12/4/2015.
 */
public class CommentAdapter extends ParseQueryAdapter<Comment> {
    public CommentAdapter(Context context, Event event) {
        super(context, createQueryFactory(event));
    }

    private static QueryFactory<Comment> createQueryFactory(final Event event) {
        QueryFactory<Comment> factory = new QueryFactory<Comment>() {
            @Override
            public ParseQuery<Comment> create() {
                ParseQuery<Comment> query = Comment.getQuery();

                query.include("creator");
                query.whereEqualTo("commentEvent", event);

                return query;
            }
        };
        return factory;
    }

    @Override
    public View getItemView(Comment comment, View view, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.comment_list_item, null);
        }

        super.getItemView(comment, view, parent);

        ((CommentItemView)view).update(comment);
        
        return view;
    }
}
