package com.example.stephenogden.a1595scoutingapp;

import android.bluetooth.BluetoothAdapter;
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

/**
 * Created by Stephen Ogden on 3/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class main_activity extends AppCompatActivity {

    // TODO: Try connecting to PC immidiatly (When this appreas), if fail, toast text warning

    public static BluetoothAdapter btAdapter;

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
                    if (settings.MACADDR.isEmpty()) {
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

    public void AlertBox(@SuppressWarnings("SameParameterValue") String title, String message ){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit." ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }
}
