package org.frc1595Dragons.scoutingapp.Activities;

import android.widget.TextView;

import org.frc1595Dragons.scoutingapp.R;

import java.io.StringWriter;

/**
 * Created by Stephen Ogden on 6/24/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class error_activity extends android.support.v7.app.AppCompatActivity {

    private static String errorMessage, stackTraceText;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.error_page);

        // Define the errorMessage
        ((TextView) this.findViewById(R.id.errorType)).setText(errorMessage);

        // Define the stackTraceText
        ((TextView) this.findViewById(R.id.stackText)).setText(stackTraceText);

        // Add a listener to the return button
        this.findViewById(R.id.ReturnButton).setOnClickListener(v -> {
            try {
                data_collection.teamNumber = 0;
                // Reset the MAC address of the receiver.
                org.frc1595Dragons.scoutingapp.BlueFiles.Bluetooth.close();
            } catch (Exception e) {
                // If there are any other errors, just dump it to the LogCat, (aka ignore it really)
                android.util.Log.e("Error in error handler", e.getMessage());
            }
            // TODO: Restart app
            System.exit(0);
        });
    }

    public class CatchError {

        public void Catch(android.content.Context context, Throwable e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            errorMessage = e.getMessage();
            stackTraceText = sw.toString();

            // Start the error activity page
            context.startActivity(new android.content.Intent(context, error_activity.class));
        }
    }
}
