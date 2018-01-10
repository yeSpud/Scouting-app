package com.example.stephenogden.a1595scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
//import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter;


/**
 * Created by Stephen Ogden on 5/29/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class bluetoothTransmiter extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmitter);

        TextView field = (TextView) findViewById(R.id.Progress);
        ProgressBar progress = (ProgressBar) findViewById(R.id.loadingBar);

        field.setText("Gathering data (hasAuto)");
        boolean hasAuto = scouting.hasAuto;
        progress.setProgress(10);

        field.setText("Gathering data (autoSwitch)");
        boolean autoSwitch = scouting.autoSwitch;
        progress.setProgress(20);

        field.setText("Gathering data (autoScale)");
        boolean autoScale = scouting.autoScale;
        progress.setProgress(30);

        field.setText("Gathering data (teleSwitch)");
        boolean teleSwitch = scouting.teleSwitch;
        progress.setProgress(40);

        field.setText("Gathering data (teleScale)");
        boolean teleScale = scouting.teleScale;
        progress.setProgress(50);

        field.setText("Gathering data (cubeNumber)");
        int cubeNumber = scouting.cubeNumber;
        progress.setProgress(60);

        field.setText("Gathering data (endClimb)");
        boolean endClimb = scouting.endClimb;
        progress.setProgress(70);

        field.setText("Gathering data (endClimbAssist)");
        boolean endClimbAssist = scouting.endClimbAssist;
        progress.setProgress(80);

        field.setText("Connecting to PC...");
        // BluTooth connection to desktop
        progress.setProgress(90);

        field.setText("Compiling data...");
        // Make file in ram, do not actually store it
        progress.setProgress(95);

        field.setText("Sending data...");
        // Send that to the receiver
        progress.setProgress(100);

        // Close the window, and report success


        Button back_btn = (Button) findViewById(R.id.cancel);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(bluetoothTransmiter.this, MainActivity.class));
                Toast.makeText(bluetoothTransmiter.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
