package org.dragons.Dragons.scoutingapp;


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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 5/29/17.
 * FTC 6128 | 7935
 * FRC 1595
 */
// TODO: Error windows pause activity until OK
public class transmit extends AppCompatActivity {

    //public BluetoothAdapter btAdapter;
    //public BluetoothSocket btSocket;

    TextView field;
    ProgressBar progress;



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
            /*
            // Set up a pointer to the remote node using it's address.
            BluetoothDevice device = main_activity.btAdapter.getRemoteDevice(settings.MACADDR);
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
            main_activity.btAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            try {
                btSocket.connect();
            } catch (IOException e) {
                String msg = "An exception occurred during connection, socket closed: " + e.getMessage();
                AlertBox("Error", msg);
            }
            */

            progress.setProgress(75);
            field.setText(R.string.datapending);
            // Create a data stream so we can talk to server.
            OutputStream outStream;
            try {
                outStream = main_activity.btSocket.getOutputStream();
                outStream.flush();
                outStream.write(main_activity.data.getBytes(), 0, main_activity.data.getBytes().length);
                Log.e("outString", main_activity.data);
                Log.e("outBytes", Arrays.toString(main_activity.data.getBytes()));
                progress.setProgress(100);
                outStream.close();
            } catch (Exception e) {
                String msg = "An exception occurred during write: " + e.getMessage();
                if (settings.MACADDR.equals("00:00:00:00:00:00")) {
                    msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
                }
                msg = msg + ".\n\nCheck that the SPP UUID exists on server.\n\n";
                AlertBox("Error", msg);
            }

            //try {
                //btSocket.close();
                main_activity.data = null;
                Thread.yield();
                Toast.makeText(transmit.this, "Success!", Toast.LENGTH_LONG).show();
                finish();
            //} catch (Exception e) {
                //AlertBox("Fatal Error", "Cannot close socket: " + e.getMessage());
            //}

            // TODO: Dont disconnect from the device!!!

        }
    };

    public void AlertBox(@SuppressWarnings("SameParameterValue") String title, String message ) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit." ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }
}
