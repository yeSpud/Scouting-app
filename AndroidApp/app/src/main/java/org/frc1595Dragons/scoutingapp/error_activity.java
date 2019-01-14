package org.frc1595Dragons.scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Stephen Ogden on 6/24/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class error_activity extends AppCompatActivity {

    // Declare the errorType field and the stackTrace field
    TextView errorTypeText, stackTraceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_page);

        // Add a listener to the return button
        findViewById(R.id.ReturnButton).setOnClickListener(v -> {
            try {
                // TODO: Reset all of core
                // Try setting the team teamNumber back to a default value (0)
                data_collection.teamNumber = 0;
                // Reset the MAC address of the receiver.
                Bluetooth.MAC = null;
            } catch (Exception e) {
                // If there are any other errors, just dump it to the LogCat, (aka ignore it really)
                Log.e("Error in error handler", e.getMessage());
                return;
            }
            // Return back to the main page
            startActivity(new Intent(error_activity.this, main_activity.class));
        });

        // Define the errorTypeText
        errorTypeText = findViewById(R.id.errorType);
        // Define the stackTraceText
        stackTraceText = findViewById(R.id.stackText);
    }

    protected void onResume() {
        super.onResume();
        // Set the errorTypeText to the entered value of errorType
        errorTypeText.setText("Error message: " + CatchError.errorType);

        // Set the stackTraceText to the entered value of errorStackTraces
        stackTraceText.setText(CatchError.errorStackTrace);
    }
}
