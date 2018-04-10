package org.dragons.Dragons.scoutingapp;

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
import android.widget.RadioGroup;
import android.widget.Toast;

import static org.dragons.Dragons.scoutingapp.main_activity.data;

/**
 * Created by Stephen Ogden on 3/6/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class pitscouting extends AppCompatActivity {

    Button cancel, submit;

    CheckBox noAuto, basicAuto, switchAuto, scaleAuto, noTele, switchTele, scaleTele;

    EditText teamNumber, teamName, wheelSize, wheelSpace, grndClearance, comments;

    RadioGroup DTC, Climber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitscouting);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(pitscouting.this, main_activity.class));
                Toast.makeText(pitscouting.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });

        noAuto = findViewById(R.id.pitNoAuto);
        basicAuto = findViewById(R.id.pitBasicAuto);
        switchAuto = findViewById(R.id.pitSwitchAuto);
        scaleAuto =  findViewById(R.id.pitScaleAuto);


        noAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                basicAuto.setClickable(!isChecked);
                switchAuto.setClickable(!isChecked);
                scaleAuto.setClickable(!isChecked);
            }
        });

        basicAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noAuto.setClickable(!isChecked);
            }
        });

        switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noAuto.setClickable(!isChecked);
            }
        });

        scaleAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noAuto.setClickable(!isChecked);
            }
        });

        noTele = findViewById(R.id.pitNoTele);
        switchTele = findViewById(R.id.pitSwitchTele);
        scaleTele = findViewById(R.id.pitScaleTele);

        noTele.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchTele.setClickable(!isChecked);
                scaleTele.setClickable(!isChecked);
            }
        });

        switchTele.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noTele.setClickable(!isChecked);
            }
        });

        scaleTele.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noTele.setClickable(!isChecked);
            }
        });


        submit = findViewById(R.id.submit);

        teamNumber = findViewById(R.id.teamNumber);
        teamName = findViewById(R.id.teamName);
        DTC = findViewById(R.id.DTConfig);
        wheelSize = findViewById(R.id.wheelSizeText);
        wheelSpace = findViewById(R.id.wheelSpaceText);
        grndClearance = findViewById(R.id.groundClearance);
        Climber = findViewById(R.id.climbType);
        comments = findViewById(R.id.commentsText);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                data = String.format("%s,%s,%s,%s,%s,%s,%s",
                        "pit",
                        teamNumber.getText().toString(),
                        teamName.getText().toString(),
                        ((RadioButton) findViewById(DTC.getCheckedRadioButtonId())).getText().toString(),
                        wheelSize.getText().toString(),
                        wheelSpace.getText().toString(),
                        grndClearance.getText().toString());

                String autoString = null;
                if (noAuto.isChecked()) {
                    autoString = "No auto";
                } else {
                    if (basicAuto.isChecked()) {
                        autoString = "Can cross baseline";
                    }
                    if (switchAuto.isChecked()) {
                        autoString = autoString + "|can place cube on switch";
                    }
                    if (scaleAuto.isChecked()) {
                        autoString = autoString + "|can place cube on scale";
                    }
                }

                data = String.format("%s,%s", data, autoString);

                String teleString = null;
                if (noTele.isChecked()) {
                    teleString = "No teleOp.... how?";
                } else {
                    if (switchTele.isChecked() && scaleTele.isChecked()) {
                        teleString = "Can place cube on switch and scale";
                    }
                    if (switchTele.isChecked()) {
                        teleString = "Can place cube on switch";
                    }
                    if (scaleTele.isChecked()) {
                        teleString = "Can place cube on scale";
                    }
                }

                data = String.format("%s,%s,%s,%s",
                        data,
                        teleString,
                        ((RadioButton) findViewById(Climber.getCheckedRadioButtonId())).getText().toString(),
                        comments.getText().toString());

                Log.e("Output", data);
                transmit transmit = new transmit();
                transmit.sendData();
                Toast.makeText(pitscouting.this, "Success!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(pitscouting.this, main_activity.class));
                finish();
            }
        });


        }
}
