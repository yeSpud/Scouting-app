package org.frc1595Dragons.scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 6/23/2018.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Bluetooth {

    // Bluetooth adapter (think of it as the specific microchip on the phone)
    public static BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    public static String MAC;

    // Bluetooth device (The receiver that the phone connects and sends data to)
    public static BluetoothDevice device;

    Bluetooth() {
        // Make sure bluetooth is on
        Log.d("BluetoothEnabled?", Boolean.toString(this.isBluetoothOn()));
    }

    // Function to check if bluetooth is enabled
    private boolean isBluetoothOn() {
        // For starters, set it to false, as a fail-safe
        boolean isOn = false;
        try {
            // Check of the adapter is enabled or not
            isOn = btAdapter.isEnabled();
        } catch (Exception NullPointerException) {
            // If its null, one cause is that its just turned off, so try re-enabling it
            // Prompt user to turn on Bluetooth
            AppCompatActivity activity = new AppCompatActivity();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }
        return isOn;
    }

    public class SSP extends Thread {

        public int latency;

        private BufferedReader input;
        private BufferedWriter output;
        private BluetoothSocket socket;

        public SSP(BluetoothSocket socket) throws IOException {
            this.socket = socket;
            this.socket.connect();
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        }

        public void run() {

            while (this.socket.isConnected() && (!Bluetooth.MAC.equals(""))) {

                // Do whatever it is to communicate with the server

            }
        }


    }

}
