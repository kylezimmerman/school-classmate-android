/*
    NotificationHelper.java

    This class is a helper class to schedule notifications for Event due dates.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */

package com.prog3210.classmate.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.prog3210.classmate.LogHelper;

import java.util.Date;

public class NotificationHelper {
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_BEFORE_EVENT = 36;

    /***
     * Static method to schedule a notification for Event due dates.
     * @param context Current activity's context.
     * @param eventId The Parse ObjectId of the Event that was created.
     * @param message The message to display in the notification.
     * @param dueDate The due date of the Event.
     */
    public static void scheduleNotification(Context context, String eventId, String message, Date dueDate) {
        try {
            // Getting the Notification ID form the SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            int notificationId = sharedPreferences.getInt("event_notification_id", 0);

            // Incrementing the Notification ID in the SharedPreferences
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putInt("classmate_notification_id", ++notificationId);
            spEditor.apply();

            // Setting up intent to put into the PendingIntent for the AlarmReceiver
            Intent alarmIntent = new Intent("com.prog3210.classmate.EVENT_ALARM");
            alarmIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId);
            alarmIntent.putExtra(AlarmReceiver.NOTIFICATION_TITLE, "Event Due");
            alarmIntent.putExtra(AlarmReceiver.NOTIFICATION_MESSAGE, message);
            alarmIntent.putExtra(AlarmReceiver.EVENT_ID, eventId);

            // Creating the PendingIntent for the AlarmReceiver
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Getting the time for the notification in milliseconds
            long eventDateMillis = dueDate.getTime();
            long millisBeforeEvent = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_BEFORE_EVENT;
            long futureInMillis = eventDateMillis - millisBeforeEvent;

            // Scheduling the notification
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        } catch (Exception ex) {
            LogHelper.logError(context, "NotificationHelper", "Error showing an event notification", ex.getMessage());
        }
    }
}
