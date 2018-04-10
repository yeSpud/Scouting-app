package org.dragons.Dragons.scoutingapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 3/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class main_activity extends AppCompatActivity {

    // Then, lets create a string to for the data that will be sent to the receiver
    public static String data;
    // There's also the number of the team to scout that needs to be defined here too
    public static int number;
    // Well known SPP UUID
    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // There are a few buttons used on the main activity page, so we are going to start defining them here
    public Button start_button;
    public Button settings_btn;
    public Button pit_button;
    // First, lets create a test to see if this device supports bluetooth
    BluetoothAdapter TestBT = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // One of the buttons to finish setting up is the button for the settings page, or where the user will enter the MAC address of the server
        // So when the button is clicked, se want to start the settings activity
        settings_btn = findViewById(R.id.settings);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_activity.this, settings.class));
            }
        });

        // Yet another button. This ones for pit-scouting though, and as such has different actions to be executed
        // So do the usual: get the button from the activity, and add a listener
        pit_button = findViewById(R.id.pitScout);
        pit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This is where things get interesting. We want to check if there is a MAC address entered or not
                if (enteredMac()) {

                    // If there isn't a MAC address entered, use Toast to notify the user that they need to enter one
                    Toast.makeText(main_activity.this, "Please enter a MAC address first!", Toast.LENGTH_LONG).show();
                } else {

                    // If there was a MAC address entered however, then the app can precede and start the pit scouting activity
                    startActivity(new Intent(main_activity.this, pitscouting.class));
                }
            }
        });

        // Final button, this one is the important one.
        // For scouting a few more things need to happen, but first, we need to get the button itself, and then add a listener
        start_button = findViewById(R.id.start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Like for the pit-scouting button, we need a try-catch method here for checking the MAC address
                if (!enteredMac()) {

                    // If there isn't a MAC address entered, use Toast to notify the user that they need to enter one
                    Toast.makeText(main_activity.this, "Please enter a MAC address first!", Toast.LENGTH_LONG).show();
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
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Start scouting!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Lets add some checks to see if there was anything entered in the first place
                            // If there wasn't, Toast should display "Please enter a team number to scout"
                            if (userInput.getText().length() == 0) {
                                Toast.makeText(main_activity.this, "Please enter a team number to scout", Toast.LENGTH_LONG).show();
                            } else {

                                // When the number entered is valid, we can set the number to the entered value, and then start the data collection activity
                                number = Integer.parseInt(userInput.getText().toString());
                                startActivity(new Intent(main_activity.this, data_collection.class));
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        });
    }

    public void onResume() {
        super.onResume();

        // This is where the actual adapter gets setup for use
        settings.btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Attempt to activate bluetooth, but will obviously fail if bluetooth is not supported
        CheckBTState();

        // Checks if the bluetooth device is supported and on
        if (isSupportedandOn()) {

            // Checks if there is a MAC address entered
            if (enteredMac()) {

                // Checks if it is connected to a bluetooth SPP server
                if (isConnected()) {

                    // If all of these checks pass, then let the user know via toast
                    Toast.makeText(main_activity.this, "Already connected to server", Toast.LENGTH_LONG).show();
                } else {

                    // If its not connected, then attempt to establish a connection with the server
                    establishConnection();
                }
            } else {
                // If there isn't a MAC address, or its invalid, let the user know via toast
                Toast.makeText(main_activity.this, "Invalid MAC", Toast.LENGTH_LONG).show();
            }
        } else {

            // If bluetooth isn't supported in the first place (Probably an emulator lol), let the user know via toast
            Toast.makeText(main_activity.this, "Bluetooth not supported/on", Toast.LENGTH_LONG).show();
        }

    }

    @SuppressWarnings("SpellCheckingInspection")
    private boolean isSupportedandOn() {
        try {
            return settings.btAdapter.isEnabled();
        } catch (Exception NullPointerException) {
            return false;
        }
    }

    private boolean enteredMac() {
        try {
            return !settings.MACADDR.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isConnected() {
        try {
            return settings.btSocket.isConnected();
        } catch (Exception NullPointerException) {
            return false;
        }
    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (TestBT == null) {
            AlertBox("Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (!isSupportedandOn()) {

                // Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void establishConnection() {

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = null;

        try {
            device = settings.btAdapter.getRemoteDevice(settings.MACADDR);
        } catch (Exception e) {
            AlertBox("MAC Invalid!", e.getMessage());
        }

        // Two things are needed to make a connection:
        // A MAC address, which we got above.
        // A Service ID or UUID.  In this case we are using the UUID for SPP.
        try {
            assert device != null;
            settings.btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
            AlertBox("Fatal Error", "Socket create failed: " + e.getMessage() + ".");
        }

        Toast.makeText(main_activity.this, "Connecting to server", Toast.LENGTH_LONG).show();

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        settings.btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            settings.btSocket.connect();
        } catch (IOException e) {
            String msg = "An exception occurred during connection, socket closed: " + e.getMessage();
            AlertBox("Error", msg);
        }
    }

    public void AlertBox(@SuppressWarnings("SameParameterValue") String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }
}
