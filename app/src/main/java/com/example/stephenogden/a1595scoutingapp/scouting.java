package com.example.stephenogden.a1595scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

/*
Auto:
    Has auto
    Can place cube on balance
    Can place cube on scale
Teleop:
    Can place cube on balance
    Can place cube on scale
    Number of cubes placed in match
End game:
    Can climb
    Can assist others on climb

 */

public class scouting extends AppCompatActivity {

    public static boolean hasAuto;
    public static boolean autoSwitch;
    public static boolean autoScale;
    public static boolean teleSwitch;
    public static boolean teleScale;
    public static int cubeNumber;
    public static boolean endClimb;
    public static boolean endClimbAssist;

    TextView NumberOfCubes;
    SeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scouting);
        setTitle(getResources().getString(R.string.teamToScout) + MainActivity.number);

        Button back_btn = (Button) findViewById(R.id.cancel);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(scouting.this, MainActivity.class));
                Toast.makeText(scouting.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });

        NumberOfCubes = (TextView) findViewById(R.id.cubeNumber);
        bar = (SeekBar) findViewById(R.id.numberPicker);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                NumberOfCubes.setText("Number of cubes placed: " + String.valueOf((int) Math.round(progress/2)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button send_btn = (Button) findViewById(R.id.submit);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cubeNumber = (int) Math.round(bar.getProgress()/2);
                startActivity(new Intent(scouting.this, bluetoothTransmiter.class));
            }
        });
    }

}
