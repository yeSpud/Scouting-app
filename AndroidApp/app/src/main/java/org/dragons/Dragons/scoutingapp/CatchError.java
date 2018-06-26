package org.dragons.Dragons.scoutingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CatchError {

    public static String errorType = null, errorStackTrace = null;

// TODO Add function to set error and stacktrace

    public void caughtError(Context context, String Type, String StackTrace) {
        errorType = Type;
        errorStackTrace = StackTrace.replace("[", "").replace("]", "");
        //Log.e("What is null?", Boolean.toString((boolean) (this.getClass() == null)));
        context.startActivity(new Intent(context, error_activity.class));
    }

}
