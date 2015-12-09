package com.prog3210.classmate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kzimmerman on 12/9/2015.
 */
public class LogHelper {
    public static boolean showToast = true;

    public static boolean isShowToast() {
        return showToast;
    }

    public static void setShowToast(boolean showToast) {
        LogHelper.showToast = showToast;
    }

    public static void logError(Context context, String tag, String friendlyMessage, String exceptionMessage) {
        Log.e(tag, friendlyMessage + ": " + exceptionMessage);

        if (showToast && friendlyMessage != null && friendlyMessage.length() > 0) {
            Toast.makeText(context, friendlyMessage, Toast.LENGTH_LONG).show();
        }
    }
}
