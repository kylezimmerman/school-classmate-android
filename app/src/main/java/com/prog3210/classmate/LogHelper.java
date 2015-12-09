/*
    LogHelper.java

    helper to simplify the logging and displaying of errors

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
package com.prog3210.classmate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogHelper {
    private static boolean showToast = true;

    /***
     * Gets whether a toast should be shown for errors.
     * This is likely useful during development but not for production
     * @return
     *  assignment of showToast
     */
    public static boolean isShowToast() {
        return showToast;
    }

    /***
     * Sets if toast should be shown or not for errors
     * @param showToast
     *  True if a toast should be shown
     */
    public static void setShowToast(boolean showToast) {
        LogHelper.showToast = showToast;
    }

    /***
     * Logs the error and displays a toast if wanted
     * @param context
     *  context of host activity
     * @param tag
     *  name of the class that throws
     * @param friendlyMessage
     *  friendly message to be shown to users through a toast
     * @param exceptionMessage
     *  exception message to be logged
     */
    public static void logError(Context context, String tag, String friendlyMessage, String exceptionMessage) {
        Log.e(tag, friendlyMessage + ": " + exceptionMessage);

        if (showToast
                && context != null
                && friendlyMessage != null
                && friendlyMessage.length() > 0) {
            Toast.makeText(context, friendlyMessage, Toast.LENGTH_LONG).show();
        }
    }
}
