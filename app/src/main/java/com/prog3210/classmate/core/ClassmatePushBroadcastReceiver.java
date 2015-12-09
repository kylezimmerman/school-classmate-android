package com.prog3210.classmate.core;

import android.content.Context;
import android.content.Intent;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.prog3210.classmate.events.Event;
import com.prog3210.classmate.events.EventViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by kzimmerman on 12/4/2015.
 */
public class ClassmatePushBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(final Context context, Intent intent) {
        super.onPushReceive(context, intent);



        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));

            final String eventId = pushData.optString("event_id", null);

            if (eventId != null) {
                ParseQuery<Event> query = Event.getQuery();
                query.include("course");
                query.getInBackground(eventId, new GetCallback<Event>() {
                    @Override
                    public void done(Event object, ParseException e) {
                        if (e == null) {
                            String message = String.format("Deadline for '%s' is coming up!", object.getName());
                            Date dueDate = object.getDate();
                            NotificationHelper.scheduleNotification(context,
                                    eventId,
                                    message,
                                    dueDate);
                        }
                    }
                });
            }
        } catch (JSONException e) {

        }
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {

        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));

            String event = pushData.optString("event_id", null);

            if (event != null) {
                Intent viewEventIntent = new Intent(context, EventViewActivity.class);
                viewEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                viewEventIntent.putExtra("event_id", event);
                context.startActivity(viewEventIntent);
            } else {
                super.onPushOpen(context, intent);
            }
        } catch (JSONException e) {
            super.onPushOpen(context, intent);
        }
    }
}
