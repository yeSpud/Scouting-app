package org.frc1595Dragons.scoutingapp;

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


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

// Most of this is extremely likely subject to change depending on what data you want, so I will comment only that which is likely to remain the same throughout

public class data_collection extends AppCompatActivity {

    final int MAX_CUBE = 25;
    CheckBox basicAuto, switchAuto, scaleAuto;
    @SuppressWarnings("SpellCheckingInspection")
    TextView teleSwitchHeaderText, teleSwitchNumberText, teleScaleHeaderText, teleScaleNumberText, exchangeNumber;
    @SuppressWarnings("SpellCheckingInspection")
    Button teleSwitchSubtract, teleSwitchAdd, teleScaleSubtract, teleScaleAdd, exchangeSubtract, exchangeAdd;
    RadioButton noC, oneCS, oneCC, twoCS, twoCC, threeCS, threeCC, rampC;
    EditText comment;
    Button back_btn;
    Button send_btn;
    @SuppressWarnings("SpellCheckingInspection")
    byte teleSwitchCube = 0, teleScaleCube = 0, exchangeCube = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_collection);

        // For a nice little accessibility feature, we can set the top bar to display the team teamNumber that the user is scouting
        // That way, they don't forget, or scout the wrong team :P
        this.setTitle(getResources().getString(R.string.teamToScout) + Core.teamNumber);

        // TODO: Generate this page dynamically


        // If for what ever reason they entered the wrong teamNumber, or just need to stop scouting, we can add a back/cancel button for them
        // So, get the button from the view
        back_btn = findViewById(R.id.cancel);
        send_btn = findViewById(R.id.submit);


        // Then add a listener to that which will return the user to the main page, and display "Scouting canceled" via toast
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(data_collection.this, main_activity.class));
                    Toast.makeText(data_collection.this, getString(R.string.scoutingCanceled), Toast.LENGTH_LONG).show();
                }
            });


        noC = findViewById(R.id.noClimb);
        oneCS = findViewById(R.id.oneClimbSide);
        oneCC = findViewById(R.id.oneClimbCenter);
        twoCS = findViewById(R.id.twoClimbSide);
        twoCC = findViewById(R.id.twoClimbCenter);
        threeCS = findViewById(R.id.threeClimbSide);
        threeCC = findViewById(R.id.threeClimbCenter);
        rampC = findViewById(R.id.rampClimb);

        comment = findViewById(R.id.comment);

        teleSwitchHeaderText = findViewById(R.id.teleSwitchHeader);
        teleScaleHeaderText = findViewById(R.id.teleScaleHeader);


        basicAuto = findViewById(R.id.autoCheck);

        switchAuto = findViewById(R.id.autoBalanceCheck);

            switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        teleSwitchHeaderText.setText(String.format("%s" + getString(R.string.autoCubeReference), teleSwitchHeaderText.getText().toString()));
                    } else {
                        teleSwitchHeaderText.setText(teleSwitchHeaderText.getText().toString().replace(getString(R.string.autoCubeReference), ""));
                    }
                }
            });


        scaleAuto = findViewById(R.id.autoScaleCheck);

            scaleAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        teleScaleHeaderText.setText(String.format("%s" + getString(R.string.autoCubeReference), teleScaleHeaderText.getText().toString()));
                    } else {
                        teleScaleHeaderText.setText(teleScaleHeaderText.getText().toString().replace(getString(R.string.autoCubeReference), ""));
                    }
                }
            });



        teleSwitchNumberText = findViewById(R.id.teleSwitchNumber);
        teleSwitchSubtract = findViewById(R.id.teleSwitchSubtract);
        teleSwitchSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleSwitchCube > 0) {
                    teleSwitchCube--;
                    teleSwitchNumberText.setText(String.format("%s", teleSwitchCube));
                }
            }
        });
        teleSwitchAdd = findViewById(R.id.teleSwitchAdd);
        teleSwitchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleSwitchCube < MAX_CUBE) {
                    teleSwitchCube++;
                    teleSwitchNumberText.setText(String.format("%s", teleSwitchCube));
                }
            }
        });

        teleScaleNumberText = findViewById(R.id.teleScaleNumber);
        teleScaleSubtract = findViewById(R.id.teleScaleSubtract);
        teleScaleSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleScaleCube > 0) {
                    teleScaleCube--;
                    teleScaleNumberText.setText(String.format("%s", teleScaleCube));
                }
            }
        });
        teleScaleAdd = findViewById(R.id.teleScaleAdd);
        teleScaleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teleScaleCube < MAX_CUBE) {
                    teleScaleCube++;
                    teleScaleNumberText.setText(String.format("%s", teleScaleCube));
                }
            }
        });


        exchangeNumber = findViewById(R.id.teleExchangeNumber);
        exchangeSubtract = findViewById(R.id.teleExchangeSubtract);
        exchangeSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchangeCube > 0) {
                    exchangeCube--;
                    exchangeNumber.setText(String.format("%s", exchangeCube));
                }
            }
        });
        exchangeAdd = findViewById(R.id.teleExchangeAdd);
        exchangeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchangeCube < MAX_CUBE) {
                    exchangeCube++;
                    exchangeNumber.setText(String.format("%s", exchangeCube));
                }
            }
        });

        // Final stretch, now its time to get the data entered and submit it.
        // First get the send button from the view

        // Then add a listener to the button
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // This is where things get interesting. For this is the part where we send the string to a process that will send it to the server via bluetooth
                // First, set the string to that of which we want the receiver to parse. I HIGHLY recommend that we format it for ease of readability
                /*
                data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        Core.teamNumber,
                        (byte) (basicAuto.isChecked() ? 1 : 0),
                        (byte) (switchAuto.isChecked() ? 1 : 0),
                        (byte) (scaleAuto.isChecked() ? 1 : 0),
                        teleSwitchCube + (byte) (switchAuto.isChecked() ? 1 : 0),
                        teleScaleCube + (byte) (scaleAuto.isChecked() ? 1 : 0),
                        exchangeCube,
                        (byte) (oneCS.isChecked() ? 1 : 0),
                        (byte) (oneCC.isChecked() ? 1 : 0),
                        (byte) (twoCS.isChecked() ? 1 : 0),
                        (byte) (twoCC.isChecked() ? 1 : 0),
                        (byte) (threeCS.isChecked() ? 1 : 0),
                        (byte) (threeCC.isChecked() ? 1 : 0),
                        (byte) (rampC.isChecked() ? 1 : 0),
                        comment.getText().toString().replace(",", ".")); // Because its a CSV, we need to replace any non-essential commas with something else

                // If the phone used for testing is hooked up to a debugger, we can view the data being sent by printing it to the log
               Log.e("Output", data);
               */

                // This is how the data is transmitted, so for all the details for that, look at transmit.java
                transmit transmit = new transmit();
                try {
                    transmit.sendData();
                } catch (BluetoothSendError bluetoothSendError) {
                    bluetoothSendError.printStackTrace();
                }

                // On completion, display "Success!" via toast, and return the user to the main activity
                Toast.makeText(data_collection.this, getString(R.string.scoutingSuccess), Toast.LENGTH_LONG).show();

                startActivity(new Intent(data_collection.this, main_activity.class));
                finish();
            }
        });
    }
}