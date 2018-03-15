package org._1595dragons.Dragons._1595scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static org._1595dragons.Dragons._1595scoutingapp.main_activity.data;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class data_collection extends AppCompatActivity {

    CheckBox basicAuto, switchAuto, scaleAuto;
    TextView teleSwitchHeaderText, teleSwitchNumberText, teleScaleHeaderText, teleScaleNumberText, exchangeNumber;
    Button teleSwitchSubtract, teleSwitchAdd, teleScaleSubtract, teleScaleAdd, exchangeSubtract, exchangeAdd;
    RadioButton noC, oneC, twoC, threeC;
    EditText comment;
    Button back_btn;
    Button send_btn;

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

        noC = (RadioButton) findViewById(R.id.noClimb);
        oneC = (RadioButton) findViewById(R.id.oneClimb);
        twoC = (RadioButton) findViewById(R.id.twoClimb);
        threeC = (RadioButton) findViewById(R.id.threeClimb);

        comment = (EditText) findViewById(R.id.comment);

        teleSwitchHeaderText = (TextView) findViewById(R.id.);

        basicAuto = (CheckBox) findViewById(R.id.autoCheck);

        switchAuto = (CheckBox) findViewById(R.id.autoBalanceCheck);
        switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        send_btn = (Button) findViewById(R.id.submit);
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int climbValue = 0;
                if (noC.isChecked()) {
                    climbValue = 0;
                } else if (oneC.isChecked()) {
                    climbValue = 1;
                } else if (twoC.isChecked()) {
                    climbValue = 2;
                } else if (threeC.isChecked()) {
                    climbValue = 3;
                }

                data = (main_activity.number +  "," +
                        hasAuto + "," +
                        autoSwitch + "," +
                        autoScale + "," +
                        teleSwitch + "," +
                        teleScale + "," +
                        cubeNumber + "," +
                        climbValue).toUpperCase();

                data = data + "," + comment.getText().toString();
                Log.d("Output",data);


                startActivity(new Intent(data_collection.this, transmit.class));
                finish();
            }
        });
    }
}
