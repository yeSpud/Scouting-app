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
import android.widget.TextView;
import android.widget.Toast;

import static org.dragons.Dragons.scoutingapp.main_activity.data;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class data_collection extends AppCompatActivity {

    final int MAX_CUBE = 25;
    CheckBox basicAuto, switchAuto, scaleAuto;
    TextView teleSwitchHeaderText, teleSwitchNumberText, teleScaleHeaderText, teleScaleNumberText, exchangeNumber;
    Button teleSwitchSubtract, teleSwitchAdd, teleScaleSubtract, teleScaleAdd, exchangeSubtract, exchangeAdd;
    RadioButton noC, oneC, twoC, threeC;
    EditText comment;
    Button back_btn;
    Button send_btn;
    byte teleSwitchCube = 0, teleScaleCube = 0, exchangeCube = 0;

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

        teleSwitchHeaderText = (TextView) findViewById(R.id.teleSwitchHeader);
        teleScaleHeaderText = (TextView) findViewById(R.id.teleScaleHeader);


        basicAuto = (CheckBox) findViewById(R.id.autoCheck);

        switchAuto = (CheckBox) findViewById(R.id.autoBalanceCheck);
        switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    teleSwitchHeaderText.setText(String.format("%s (+1 from auto)", teleSwitchHeaderText.getText().toString()));
                } else {
                    teleSwitchHeaderText.setText(teleSwitchHeaderText.getText().toString().replace(" (+1 from auto)", ""));
                }
            }
        });

        scaleAuto = (CheckBox) findViewById(R.id.autoScaleCheck);
        scaleAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    teleScaleHeaderText.setText(String.format("%s (+1 from auto)", teleScaleHeaderText.getText().toString()));
                } else {
                    teleScaleHeaderText.setText(teleScaleHeaderText.getText().toString().replace(" (+1 from auto)", ""));
                }
            }
        });


        teleSwitchNumberText = (TextView) findViewById(R.id.teleSwitchNumber);
        teleSwitchSubtract = (Button) findViewById(R.id.teleSwitchSubtract);
        teleSwitchSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleSwitchCube > 0) {
                    teleSwitchCube--;
                    teleSwitchNumberText.setText(String.format("%s", teleSwitchCube));
                }
            }
        });
        teleSwitchAdd = (Button) findViewById(R.id.teleSwitchAdd);
        teleSwitchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleSwitchCube < MAX_CUBE) {
                    teleSwitchCube++;
                    teleSwitchNumberText.setText(String.format("%s", teleSwitchCube));
                }
            }
        });

        teleScaleNumberText = (TextView) findViewById(R.id.teleScaleNumber);
        teleScaleSubtract = (Button) findViewById(R.id.teleScaleSubtract);
        teleScaleSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleScaleCube > 0) {
                    teleScaleCube--;
                    teleScaleNumberText.setText(String.format("%s", teleScaleCube));
                }
            }
        });
        teleScaleAdd = (Button) findViewById(R.id.teleScaleAdd);
        teleScaleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleScaleCube < MAX_CUBE) {
                    teleScaleCube++;
                    teleScaleNumberText.setText(String.format("%s", teleScaleCube));
                }
            }
        });


        exchangeNumber = (TextView) findViewById(R.id.teleExchangeNumber);
        exchangeSubtract = (Button) findViewById(R.id.teleExchangeSubtract);
        exchangeSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchangeCube > 0) {
                    exchangeCube--;
                    exchangeNumber.setText(String.format("%s", exchangeCube));
                }
            }
        });
        exchangeAdd = (Button) findViewById(R.id.teleExchangeAdd);
        exchangeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchangeCube < MAX_CUBE) {
                    exchangeCube++;
                    exchangeNumber.setText(String.format("%s", exchangeCube));
                }
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

                data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        main_activity.number,
                        (byte) (basicAuto.isChecked() ? 1 : 0),
                        (byte) (switchAuto.isChecked() ? 1 : 0),
                        (byte) (scaleAuto.isChecked() ? 1 : 0),
                        teleSwitchCube + (byte) (switchAuto.isChecked() ? 1 : 0),
                        teleScaleCube + (byte) (scaleAuto.isChecked() ? 1 : 0),
                        exchangeCube,
                        climbValue,
                        comment.getText().toString());

                Log.e("Output", data);

                startActivity(new Intent(data_collection.this, transmit.class));
                finish();
            }
        });
    }
}
