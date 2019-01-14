package org.frc1595Dragons.scoutingapp;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Stephen Ogden on 6/25/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

// FIXME
public class CatchError {

    // Declare the strings for the type of error, and its stack trace
    public static String errorType = null, errorStackTrace = null;

    // Function to display the error page (hopefully rather than crashing)
    public static void caughtError(Context context, String Type, String StackTrace) {
        // Set the errorType to the type argument
        errorType = Type;
        // Set the errorStackTrace to the stackTrace argument, but also replace the enclosing brackets, as it comes out as an array
        errorStackTrace = StackTrace.replace("[", "").replace("]", "");
        // Start the error activity page
        context.startActivity(new Intent(context, error_activity.class));
    }

}
