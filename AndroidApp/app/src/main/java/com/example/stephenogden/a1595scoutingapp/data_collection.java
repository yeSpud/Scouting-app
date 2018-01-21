package com.example.stephenogden.a1595scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class data_collection extends AppCompatActivity {

    public static String data = null;

    TextView NumberOfCubes;
    Button back_btn;
    Button send_btn;
    SeekBar bar;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_collection);
        setTitle(getResources().getString(R.string.teamToScout) + main_activity.number);

        back_btn = (Button) findViewById(R.id.cancel);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(data_collection.this, main_activity.class));
                Toast.makeText(data_collection.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });

        NumberOfCubes = (TextView) findViewById(R.id.cubeNumber);
        NumberOfCubes.setText("Number of cubes placed: ");
        bar = (SeekBar) findViewById(R.id.numberPicker);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                NumberOfCubes.setText("Number of cubes placed: " + String.valueOf(Math.round(progress/4)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        send_btn = (Button) findViewById(R.id.submit);
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Boolean hasAuto = ((CheckBox)findViewById(R.id.autoCheck)).isChecked();
                Boolean autoSwitch = ((CheckBox)findViewById(R.id.autoBalanceCheck)).isChecked();
                Boolean autoScale = ((CheckBox)findViewById(R.id.autoScaleCheck)).isChecked();
                Boolean teleSwitch = ((CheckBox)findViewById(R.id.teleCheck)).isChecked();
                Boolean teleScale = ((CheckBox)findViewById(R.id.teleCheckScale)).isChecked();
                int cubeNumber = Math.round(bar.getProgress()/4);
                Boolean endClimb = ((CheckBox)findViewById(R.id.endGameCheck)).isChecked();
                Boolean endClimbAssist = ((CheckBox)findViewById(R.id.endGameCheckAssist)).isChecked();

                data = (main_activity.number +  ", " +
                        hasAuto + ", " +
                        autoSwitch + ", " +
                        autoScale + ", " +
                        teleSwitch + ", " +
                        teleScale + ", " +
                        cubeNumber + ", " +
                        endClimb + ", " +
                        endClimbAssist).toUpperCase();


                startActivity(new Intent(data_collection.this, transmit.class));
                finish();
            }
        });
    }
    public void onDestroy() {
        super.onDestroy();
        bar.setOnSeekBarChangeListener(null);
        back_btn.setOnClickListener(null);
        send_btn.setOnClickListener(null);

    }

}
