package com.example.stephenogden.a1595scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class scouting extends AppCompatActivity {

    public byte[] data;

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
        NumberOfCubes.setText("Number of cubes placed: ");
        bar = (SeekBar) findViewById(R.id.numberPicker);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                NumberOfCubes.setText("Number of cubes placed: " + String.valueOf((int) Math.round(progress/4)));
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

                Boolean hasAuto = findViewById(R.id.autoCheck).isActivated();
                Boolean autoSwitch = findViewById(R.id.autoBalanceCheck).isActivated();
                Boolean autoScale = findViewById(R.id.autoScaleCheck).isActivated();
                Boolean teleSwitch = findViewById(R.id.teleCheck).isActivated();
                Boolean teleScale = findViewById(R.id.teleCheckScale).isActivated();
                int cubeNumber = (int) Math.round(bar.getProgress()/4);
                Boolean endClimb = findViewById(R.id.endGameCheck).isActivated();
                Boolean endClimbAssist = findViewById(R.id.endGameCheckAssist).isActivated();

                data = (MainActivity.number +  ", " +
                        hasAuto + ", " +
                        autoSwitch + ", " +
                        autoScale + ", " +
                        autoSwitch + ", " +
                        teleSwitch + ", " +
                        teleScale + ", " +
                        cubeNumber + ", " +
                        endClimb + ", " +
                        endClimbAssist).toUpperCase().getBytes();

                startActivity(new Intent(scouting.this, bluetoothTransmiter.class));
            }
        });
    }

}
