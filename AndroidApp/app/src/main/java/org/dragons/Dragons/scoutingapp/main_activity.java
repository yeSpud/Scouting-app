package org.dragons.Dragons.scoutingapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;

import static org.dragons.Dragons.scoutingapp.Core.isSetInChinese;

/**
 * Created by Stephen Ogden on 3/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class main_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Find and then add a listener to the settings button
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Replace setting page with popup dialog, or add more options
                // Once clicked, launch the settings page
                startActivity(new Intent(main_activity.this, settings.class));
            }
        });

        // Find and then add a listener to the start button
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Once clicked, start bring up the team number prompt, and then start scouting
                startScouting();
            }
        });

        // Check if the language is chinese
        if (isSetInChinese()) {
            // Change the title
            setTitle(getResources().getString(R.string.app_name_CN));
            // Change the app banner
            ((ImageView) (findViewById(R.id.imageView))).setImageResource(R.mipmap.scoutingappbanner_cn);
            // Change the description of the icon (banner)
            findViewById(R.id.imageView).setContentDescription(getString(R.string.logo_CN));
            // Change the text on the start button
            ((Button) findViewById(R.id.start)).setText(R.string.startScoutCN);
            // Change the text on the settings button
            ((Button) findViewById(R.id.settings)).setText(R.string.settingsCN);
        }
    }

    public void onResume() {
        super.onResume();

        CatchError error = new CatchError();
        Core core = null;
        try {
            core = new Core();
        } catch (BluetoothSupportError bluetoothSupportError) {
            error.caughtError(main_activity.this, bluetoothSupportError.getMessage(), Arrays.toString(bluetoothSupportError.getStackTrace()));
        }

        assert core != null;
        // Check if bluetooth is not enabled
        if (!core.isBluetoothOn()) {
            // Attempt to prompt the user to enable bluetooth
            core.requestBluetoothToggle();
        }

        // Check if bluetooth is on
        if (core.isBluetoothOn()) {
            // Check if there is a MAC address entered
            if (Core.enteredMac()) {
                // Check if the device is not connected to the receiver
                if (!core.isBluetoothConnected()) {
                    // Establish a connection with the receiver
                    core.establishConnection(main_activity.this);
                }
                // Change the start button to be visible
                findViewById(R.id.start).setVisibility(View.VISIBLE);
            } else {
                // Hide the start button
                findViewById(R.id.start).setVisibility(View.GONE);
            }
        }
    }

    void startScouting() {
        try {
            if (!Core.enteredMac()) {
                // If there isn't a MAC address entered, use Toast to notify the user in the respective language
                if (isSetInChinese()) {
                    Toast.makeText(main_activity.this, getString(R.string.macWarning_CN), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(main_activity.this, getString(R.string.macWarning), Toast.LENGTH_LONG).show();
                }
            } else {
                // Prompt the user for the team number in the respective language
                if (isSetInChinese()) {
                    // To get a popup window for entering the team number to scout, we'll start by making a LayoutInflater, and then creating a view off of that
                    LayoutInflater li = LayoutInflater.from(main_activity.this);
                    @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.teamselection_cn, null);

                    // After that, we'll use an AlertDialog builder to create
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(main_activity.this);
                    alertDialogBuilder.setView(promptsView);

                    // In order to get the text entered by the user, we'll can to use the EditText prompt
                    final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput_cn);

                    // Few more things to set up for the dialog builder, such as setting the start button to say "Start scouting", and adding a listener to the start button

                    alertDialogBuilder.setCancelable(false).setPositiveButton(getString(R.string.startScoutButton_CN), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Lets add some checks to see if there was anything entered in the first place
                            // If there wasn't, Toast should display "Please enter a team number to scout"
                            if (userInput.getText().length() == 0) {
                                Toast.makeText(main_activity.this, getString(R.string.scoutWarning_CN), Toast.LENGTH_LONG).show();
                            } else {

                                // When the number entered is valid, we can set the number to the entered value, and then start the data collection activity
                                Core.number = Integer.parseInt(userInput.getText().toString());
                                startActivity(new Intent(main_activity.this, data_collection.class));
                            }
                        }
                    }).setNegativeButton(getString(R.string.CancelCN), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // This is where we set up actions and behaviors for when the user clicks "Cancel"
                            dialog.cancel();
                        }
                    });

                    // All those steps to create a simple popup window, welcome to the life of a back end developer :P
                    // Anyways, after all that, you can then create and then show the dialog box for user input
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    // To get a popup window for entering the team number to scout, we'll start by making a LayoutInflater, and then creating a view off of that
                    LayoutInflater li = LayoutInflater.from(main_activity.this);
                    @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.teamselection, null);

                    // After that, we'll use an AlertDialog builder to create
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(main_activity.this);
                    alertDialogBuilder.setView(promptsView);

                    // In order to get the text entered by the user, we'll can to use the EditText prompt
                    final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);

                    // Few more things to set up for the dialog builder, such as setting the start button to say "Start scouting", and adding a listener to the start button
                    alertDialogBuilder.setCancelable(false).setPositiveButton(getString(R.string.startScoutButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Lets add some checks to see if there was anything entered in the first place
                            // If there wasn't, Toast should display "Please enter a team number to scout"
                            if (userInput.getText().length() == 0) {
                                Toast.makeText(main_activity.this, getString(R.string.scoutWarning), Toast.LENGTH_LONG).show();
                            } else {

                                // When the number entered is valid, we can set the number to the entered value, and then start the data collection activity
                                Core.number = Integer.parseInt(userInput.getText().toString());
                                startActivity(new Intent(main_activity.this, data_collection.class));
                            }
                        }
                    }).setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // This is where we set up actions and behaviors for when the user clicks "Cancel"
                            dialog.cancel();
                        }
                    });

                    // All those steps to create a simple popup window, welcome to the life of a back end developer :P
                    // Anyways, after all that, you can then create and then show the dialog box for user input
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        } catch (Exception ex) {
            CatchError err = new CatchError();
            err.caughtError(main_activity.this, ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }
}
