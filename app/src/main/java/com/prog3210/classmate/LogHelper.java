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
     * gets the setting of showToast bool
     * @return
     *  assignment of showToast
     */
    public static boolean isShowToast() {
        return showToast;
    }

    /***
     * sets if toast should be shown or not
     * @param showToast
     *  bool that dictates if a toast should be shown
     */
    public static void setShowToast(boolean showToast) {
        LogHelper.showToast = showToast;
    }

    /***
     * logs the error and displays a toast if wanted
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
