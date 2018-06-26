package org.dragons.Dragons.scoutingapp;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Stephen Ogden on 6/25/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class CatchError {

    public static String errorType = null, errorStackTrace = null;

    public void caughtError(Context context, String Type, String StackTrace) {
        errorType = Type;
        errorStackTrace = StackTrace.replace("[", "").replace("]", "");
        context.startActivity(new Intent(context, error_activity.class));
    }

}
