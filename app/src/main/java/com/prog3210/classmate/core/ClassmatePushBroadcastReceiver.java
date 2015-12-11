/*
    ClassmatePushBroadcastReceiver.java

    This broadcast receiver extends Parse's PushBroadCastReceiver.
    When a push notification is sent from parse, this listener will fire.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import android.content.Context;
import android.content.Intent;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.events.Event;
import com.prog3210.classmate.events.EventViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ClassmatePushBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(final Context context, Intent intent) {
        super.onPushReceive(context, intent);

        try {
            // Get the data out of the parse notification
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));

            // Get the 'event_id' property if it exists
            final String eventId = pushData.optString("event_id", null);

            // If we have an event, get the Event information and schedule a notification
            if (eventId != null) {
                ParseQuery<Event> query = Event.getQuery();
                query.include("course");
                query.getInBackground(eventId, new GetCallback<Event>() {
                    @Override
                    public void done(Event object, ParseException e) {
                        // On success of event query:
                        if (e == null) {
                            String message = String.format("Deadline for '%s' is coming up!", object.getName());
                            Date dueDate = object.getDate();
                            // Schedules the notification
                            NotificationHelper.scheduleNotification(context,
                                    eventId,
                                    message,
                                    dueDate);
                        }
                    }
                });
            }
        } catch (JSONException e) {
            LogHelper.logError(context, "onPushReceive", "An error occurred scheduling a notification for an event.", e.getMessage());
        }
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        try {
            //Get the data out of the parse notification
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));

            //Get the 'event_id' property if it exists
            String event = pushData.optString("event_id", null);

            //If we have an event, start an activity for the event's details directly
            if (event != null) {
                Intent viewEventIntent = new Intent(context, EventViewActivity.class);
                viewEventIntent.putExtra("event_id", event);

                //Force this to be a new task - this makes the activity come up even if the app is not currently running.
                viewEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(viewEventIntent);
            } else {
                //We don't have an event, so just do parse's default behavior
                super.onPushOpen(context, intent);
            }
        } catch (JSONException e) {
            LogHelper.logError(context, "ClassmatePushBroadcastReceiver", "Error opening to a specific activity", e.getMessage());

            //Something went wrong, so use Parse's default implementation instead
            super.onPushOpen(context, intent);
        }
    }
}
