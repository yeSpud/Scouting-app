package org.dragons.Dragons.scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
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

    // TODO: Try connecting to PC immediately (When this appear), if fail, toast text warning

    public static BluetoothAdapter btAdapter;
    public static BluetoothSocket btSocket;
    public static String data;

    // Well known SPP UUID
    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //final UUID SPP = UUID.fromString();

    final Context context = this;

    public Button start_button;
    public Button settings_btn;
    public Button pit_button;

    public static int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settings_btn = (Button)findViewById(R.id.settings);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_activity.this, settings.class));
            }
        });


        pit_button = (Button)findViewById(R.id.pitScout);
        pit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (settings.MACADDR.isEmpty()) {
                        Toast.makeText(main_activity.this, "Please enter a MAC address first!", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(main_activity.this, pitscouting.class));
                    }
                } catch (Exception e) {
                    Toast.makeText(main_activity.this, "Please enter a MAC address first!", Toast.LENGTH_LONG).show();
                }
            }
        });


        start_button = (Button)findViewById(R.id.start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (btAdapter==null) {
                        Toast.makeText(main_activity.this, "Device does not support bluetooth!", Toast.LENGTH_LONG).show();
                    } else if (btSocket==null || !btSocket.isConnected()) {
                        Toast.makeText(main_activity.this, "Device not connected to server", Toast.LENGTH_LONG).show();
                    } else if (settings.MACADDR.isEmpty()) {
                        Toast.makeText(main_activity.this, "Please enter a MAC address first!", Toast.LENGTH_LONG).show();
                    } else {
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.teamselection, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                        alertDialogBuilder.setCancelable(false).setPositiveButton("Start scouting!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (userInput.getText().length() == 0) {
                                    Toast.makeText(main_activity.this, "Please enter a team number to scout", Toast.LENGTH_LONG).show();
                                } else {
                                    number = Integer.parseInt(userInput.getText().toString());
                                    startActivity(new Intent(main_activity.this, data_collection.class));
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    Toast.makeText(main_activity.this, "Please enter a MAC address first!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void onResume() {
        super.onResume();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();

        if (btAdapter != null) {
            if (btAdapter.isEnabled() && !btSocket.isConnected() && !settings.MACADDR.isEmpty()) {
                establishConnection();
            } else {
                Toast.makeText(main_activity.this, "Already connected (Hopefully)", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            AlertBox("Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (!btAdapter.isEnabled()) {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void establishConnection() {
            Toast.makeText(main_activity.this, "Establishing connection", Toast.LENGTH_LONG).show();
            // Set up a pointer to the remote node using it's address.
            BluetoothDevice device = null;
            try {
                device = btAdapter.getRemoteDevice(settings.MACADDR);
            } catch (Exception e) {
                AlertBox("MAC Invalid!", e.getMessage());
            }
            // Two things are needed to make a connection:
            // A MAC address, which we got above.
            // A Service ID or UUID.  In this case we are using the UUID for SPP.
            try {
                assert device != null;
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                AlertBox("Fatal Error", "Socket create failed: " + e.getMessage() + ".");
            }
            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            main_activity.btAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            try {
                btSocket.connect();
            } catch (IOException e) {
                String msg = "An exception occurred during connection, socket closed: " + e.getMessage();
                AlertBox("Error", msg);
            }
    }

    public void AlertBox(@SuppressWarnings("SameParameterValue") String title, String message ){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit." ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }
}
