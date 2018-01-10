package com.example.stephenogden.a1595scoutingapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 5/29/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class bluetoothTransmiter extends AppCompatActivity {

    TextView out;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // Well known SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC address
    private static String address = "00:10:60:AA:B9:B2";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmitter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();

        TextView field = (TextView) findViewById(R.id.Progress);
        ProgressBar progress = (ProgressBar) findViewById(R.id.loadingBar);

        field.setText("Gathering data (hasAuto)");
        boolean hasAuto = scouting.hasAuto;
        progress.setProgress(10);

        field.setText("Gathering data (autoSwitch)");
        boolean autoSwitch = scouting.autoSwitch;
        progress.setProgress(20);

        field.setText("Gathering data (autoScale)");
        boolean autoScale = scouting.autoScale;
        progress.setProgress(30);

        field.setText("Gathering data (teleSwitch)");
        boolean teleSwitch = scouting.teleSwitch;
        progress.setProgress(40);

        field.setText("Gathering data (teleScale)");
        boolean teleScale = scouting.teleScale;
        progress.setProgress(50);

        field.setText("Gathering data (cubeNumber)");
        int cubeNumber = scouting.cubeNumber;
        progress.setProgress(60);

        field.setText("Gathering data (endClimb)");
        boolean endClimb = scouting.endClimb;
        progress.setProgress(70);

        field.setText("Gathering data (endClimbAssist)");
        boolean endClimbAssist = scouting.endClimbAssist;
        progress.setProgress(80);

        field.setText("Connecting to PC...");
        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
            out.append("\n...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                AlertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }
        progress.setProgress(90);

        field.setText("Compiling data...");
        // Create a data stream so we can talk to server.
        String message = "Hello from Android.\n";
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            AlertBox("Fatal Error", "Output stream creation failed:" + e.getMessage() + ".");
        }
        progress.setProgress(95);

        field.setText("Sending data...");
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "An exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            AlertBox("Fatal Error", msg);
        }
        progress.setProgress(100);

        // Close the window, and report success


        Button back_btn = (Button) findViewById(R.id.cancel);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(bluetoothTransmiter.this, MainActivity.class));
                Toast.makeText(bluetoothTransmiter.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            AlertBox("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                out.append("\n...Bluetooth is enabled...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void AlertBox( String title, String message ){
        new AlertDialog.Builder(this)
                .setTitle( title )
                .setMessage( message + " Press OK to exit." )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).show();
    }

}
