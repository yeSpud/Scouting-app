package com.example.stephenogden.a1595scoutingapp;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import static com.example.stephenogden.a1595scoutingapp.data_collection.data;
import static com.example.stephenogden.a1595scoutingapp.main_activity.btAdapter;

/**
 * Created by Stephen Ogden on 5/29/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class transmit extends AppCompatActivity {

    //public BluetoothAdapter btAdapter;
    public BluetoothSocket btSocket;

    TextView field;
    ProgressBar progress;

    // Well known SPP UUID
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmit);
        field = (TextView) findViewById(R.id.Progress);
        progress = (ProgressBar) findViewById(R.id.loadingBar);

        Button back_btn = (Button) findViewById(R.id.cancel);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(transmit.this, main_activity.class));
                Toast.makeText(transmit.this, "Scouting canceled.", Toast.LENGTH_LONG).show();
            }
        });

        progress.setProgress(25);
        field.setText(R.string.connecting);
        Handler handler = new Handler();
        handler.postDelayed(task, 500);
        Thread.yield();
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            // Set up a pointer to the remote node using it's address.
            BluetoothDevice device = btAdapter.getRemoteDevice(settings.MACADDR);
            // Two things are needed to make a connection:
            // A MAC address, which we got above.
            // A Service ID or UUID.  In this case we are using the UUID for SPP.
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                AlertBox("Fatal Error", "Socket create failed: " + e.getMessage() + ".");
            }
            progress.setProgress(50);
            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            btAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            try {
                btSocket.connect();
            } catch (IOException e) {
                String msg = "An exception occurred during connection, socket closed: " + e.getMessage();
                AlertBox("Error", msg);
            }

            // Good up until here!

            progress.setProgress(75);
            field.setText(R.string.datapending);
            // Create a data stream so we can talk to server.
            // Close it once sent data
            try {
                OutputStream outStream = btSocket.getOutputStream();
                outStream.write(data.getBytes(), 0, data.getBytes().length);
                outStream.close();
                btSocket.close();
                progress.setProgress(100);
                Thread.yield();
                Thread.sleep(1000);
                Toast.makeText(transmit.this, "Success!", Toast.LENGTH_LONG).show();
                finish();
            } catch (IOException e) {
                String msg = "An exception occurred during write: " + e.getMessage();
                if (settings.MACADDR.equals("00:00:00:00:00:00")) {
                    msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
                }
                msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
                AlertBox("Error", msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        /*
        Thread.yield();
        // Todo: Error with receiving verification (BufferReader seems to overflow?)
        progress.setProgress(80);
        try {
            Log.e("Before", "InputStream");
            InputStream inStream = btSocket.getInputStream();
            Log.e("Before", "BufferedReader");
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream), 14);
            Log.e("Output of inStream", bReader.readLine());
            if (bReader.readLine().equals("Data received!")) {
                progress.setProgress(100);
                bReader.close();
                inStream.close();
                btSocket.close();
                Toast.makeText(transmit.this, "Success!", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (IOException e) {
            AlertBox("Fatal Error", "Cannot receive message or close socket: " + e.getMessage());
        }
        */

        }
    };

    public void AlertBox(@SuppressWarnings("SameParameterValue") String title, String message ){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit." ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }
}
