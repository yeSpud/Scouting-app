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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 5/29/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

@SuppressLint("SetTextI18n")
public class bluetoothTransmiter extends AppCompatActivity {

    TextView field;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // Well known SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC address
    private static String address = Settings.MACADDR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmitter);

        field = (TextView) findViewById(R.id.Progress);
        Button back_btn = (Button) findViewById(R.id.cancel);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(bluetoothTransmiter.this, MainActivity.class));
                Toast.makeText(bluetoothTransmiter.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });

        field = (TextView) findViewById(R.id.Progress);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();
    }
    public void onResume() {
        super.onResume();

        ProgressBar progress = (ProgressBar) findViewById(R.id.loadingBar);
        progress.setProgress(14);

        field.setText("Compiling data...");
        progress.setProgress(29);
        field.setText("Connecting to PC...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //  A MAC address, which we got above.
        //  A Service ID or UUID.  In this case we are using the UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        progress.setProgress(43);
        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
            field.setText("\n...Connection established and data link opened...");
        } catch (IOException e) {
            String msg = "An exception occurred during connection, socket closed: " + e.getMessage();
            AlertBox("Fatal Error", msg);

        }

        progress.setProgress(57);
        // Create a data stream so we can talk to server.
        field.setText("\n...Sending message to server...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            AlertBox("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }

        progress.setProgress(71);
        field.setText("\nSending data...");
        scouting scouting = new scouting();
        try {
            outStream.write(scouting.data);
        } catch (IOException e) {
            String msg = "An exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00")) {
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            }
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            AlertBox("Fatal Error", msg);
        }
        try {
            btSocket.close();
            Toast.makeText(bluetoothTransmiter.this, "Success!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(bluetoothTransmiter.this, MainActivity.class));
        } catch (IOException e) {
            AlertBox("Fatal Error", "Cannot close socket: " + e.getMessage() + ".");
        }
    }
    public void onPause() {
        super.onPause();

        // Recieve good to go message
        InputStream inStream;
        BufferedReader bReader;
        String check = "Data pending...";
        try {
            inStream = btSocket.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(inStream));
            check = bReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (check.equals("Data recieved!") || check.equals("Data received")) {
            try {
                outStream.flush();
                btSocket.close();
                Toast.makeText(bluetoothTransmiter.this, "Success!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(bluetoothTransmiter.this, MainActivity.class));
            } catch (IOException e) {
                AlertBox("Fatal Error", "Cannot close streams: " + e.getMessage() + ".");
            }

        }
    }

    public void onStop() {
        super.onStop();
        Toast.makeText(bluetoothTransmiter.this, "Error: ran out of RAM!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(bluetoothTransmiter.this, MainActivity.class));
    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            AlertBox("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                field.setText("\nBluetooth is enabled");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void AlertBox(String title, String message ){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit." ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }

}
