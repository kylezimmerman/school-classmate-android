package com.prog3210.classmate.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.prog3210.classmate.R;
import com.prog3210.classmate.events.EventViewActivity;

/**
 * Created by Justin on 2015-12-06.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION_TITLE = "notification_title";
    public static String NOTIFICATION_MESSAGE = "notification_message";
    public static String EVENT_ID = "event_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Setting up the Intent to go to the Event that the notification is meant for
        Intent eventViewIntent = new Intent(context, EventViewActivity.class);
        eventViewIntent.putExtra(EVENT_ID, intent.getStringExtra(EVENT_ID));

        // Setting up the PendingIntent for the notification, once clicked on
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, eventViewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating the NotificationManager to get the Notification.Builder
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Setting up the Notification.Builder to build the notification
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(intent.getStringExtra(NOTIFICATION_TITLE));
        builder.setContentText(intent.getStringExtra(NOTIFICATION_MESSAGE));
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(pendingIntent);

        // Using deprecated method because 'builder.build()' is only supported in API 16+ and we support API 15+
        Notification notification = builder.getNotification();

        // Getting the ID for the notification
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);

        // Sending the notification
        notificationManager.notify(notificationId, notification);
    }
}
