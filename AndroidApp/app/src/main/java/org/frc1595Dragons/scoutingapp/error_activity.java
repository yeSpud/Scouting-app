package org.frc1595Dragons.scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static org.frc1595Dragons.scoutingapp.Core.isSetInChinese;

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
        findViewById(R.id.ReturnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Try setting the team number back to a default value (0)
                    Core.number = 0;
                    // Try setting the data that would have been sent to the receiver back to nothing
                    Core.data = null;
                    // Reset the MAC address of the receiver.
                    settings.MACADDR = null;
                } catch (Exception e) {
                    // If there are any other errors, just dump it to the LogCat, (aka ignore it really)
                    Log.e("Error in error handler", e.getMessage());
                    return;
                }
                // Return back to the main page
                startActivity(new Intent(error_activity.this, main_activity.class));
            }
        });

        // Check if language needs to be changed
        if (isSetInChinese()) {
            // Change the top title
            setTitle(getResources().getString(R.string.error_CN));
            // Change the error icon
            ((ImageView) (findViewById(R.id.ErrorImage))).setImageResource(R.mipmap.erroricon_cn);
            // Change the icon description
            ((findViewById(R.id.ErrorImage))).setContentDescription(getString(R.string.error_icon_CN));
            // Change the stacktrace text
            ((TextView) (findViewById(R.id.fillStackHeader))).setText(getResources().getString(R.string.full_stack_trace_CN));
        }

        // Define the errorTypeText
        errorTypeText = findViewById(R.id.errorType);
        // Define the stackTraceText
        stackTraceText = findViewById(R.id.stackText);
    }

    protected void onResume() {
        super.onResume();
        // Set the errorTypeText to the entered value of errorType
        if (Core.isSetInChinese()) {
            errorTypeText.setText("错误信息：" + CatchError.errorType);
        } else {
            errorTypeText.setText("Error message: " + CatchError.errorType);
        }

        // Set the stackTraceText to the entered value of errorStackTraces
        stackTraceText.setText(CatchError.errorStackTrace);
    }
}
