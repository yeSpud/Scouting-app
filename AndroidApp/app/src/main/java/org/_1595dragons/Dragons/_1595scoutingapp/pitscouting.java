package org._1595dragons.Dragons._1595scoutingapp;

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

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(pitscouting.this, main_activity.class));
                Toast.makeText(pitscouting.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });

        noAuto = (CheckBox) findViewById(R.id.pitNoAuto);
        basicAuto = (CheckBox) findViewById(R.id.pitBasicAuto);
        switchAuto = (CheckBox) findViewById(R.id.pitSwitchAuto);
        scaleAuto = (CheckBox) findViewById(R.id.pitScaleAuto);


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

        noTele = (CheckBox) findViewById(R.id.pitNoTele);
        switchTele = (CheckBox) findViewById(R.id.pitSwitchTele);
        scaleTele = (CheckBox) findViewById(R.id.pitScaleTele);

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


        submit = (Button) findViewById(R.id.submit);

        teamNumber = (EditText) findViewById(R.id.teamNumber);
        teamName = (EditText) findViewById(R.id.teamName);
        DTC = (RadioGroup) findViewById(R.id.DTConfig);
        wheelSize = (EditText) findViewById(R.id.wheelSizeText);
        wheelSpace = (EditText) findViewById(R.id.wheelSpaceText);
        grndClearance = (EditText) findViewById(R.id.groundClearance);
        Climber = (RadioGroup) findViewById(R.id.climbType);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main_activity.data = teamNumber.getText().toString();

                if ((teamName.getText() != null) && ((teamName.getText().toString().replace(" ", "")) != "")) {
                    main_activity.data = String.format("%s,%s", main_activity.data, teamName.getText().toString());
                } else {
                    main_activity.data = String.format("%s,%s", main_activity.data, " ");
                }

                main_activity.data = String.format("%s,%s", main_activity.data, ((RadioButton) findViewById(DTC.getCheckedRadioButtonId())).getText());

                Log.e("Output", main_activity.data);
            }
        });


        }
}
