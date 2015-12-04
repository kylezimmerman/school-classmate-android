package com.prog3210.classmate.core;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;
import com.prog3210.classmate.events.EventViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kzimmerman on 12/4/2015.
 */
public class ClassmatePushBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
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
