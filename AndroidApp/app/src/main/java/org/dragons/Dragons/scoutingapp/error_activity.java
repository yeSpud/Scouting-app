package org.dragons.Dragons.scoutingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Stephen Ogden on 6/24/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class error_activity extends AppCompatActivity {

    TextView errorTypeText, stackTraceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_page);
        findViewById(R.id.ReturnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(error_activity.this, main_activity.class));
            }
        });
        errorTypeText = findViewById(R.id.errorType);
        stackTraceText = findViewById(R.id.stackText);
    }

    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();
        errorTypeText.setText("Type of error: " + CatchError.errorType);
        stackTraceText.setText(CatchError.errorStackTrace);
    }
}
