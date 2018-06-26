package org.dragons.Dragons.scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 6/23/2018.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Core {

    // Instead of the test bluetooth adapter, well use the official one here, since this will only be used when the device is shown to support bluetooth
    public static BluetoothAdapter btAdapter;

    // This is the socket for said bluetooth server.
    // We'll define what exactly that will be later, but were just creating it here
    public static BluetoothSocket btSocket;

    // String for the data that will be sent to the receiver
    public static String data;

    // The number of the team to scout
    public static int number;


    public UUID MY_UUID;

    public Core() throws BluetoothSupportError {
        try {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            // Well known SPP UUID
            MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        } catch (Exception e) {
            throw new BluetoothSupportError();
        }
    }

    public boolean isBluetoothOn() {
        boolean isOn = false;
        try {
            isOn = btAdapter.isEnabled();
        } catch (Exception NullPointerException) {
            try {
                requestBluetoothToggle();
            } catch (Exception BluetoothSupportError) {
                isOn = false;
            }
        }
        return isOn;
    }

    public static boolean enteredMac() {
        try {
            return !settings.MACADDR.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBluetoothConnected() {
        try {
            return btSocket.isConnected();
        } catch (Exception NullPointerException) {
            return false;
        }
    }

    public void requestBluetoothToggle() throws BluetoothSupportError {
        try {
            // Prompt user to turn on Bluetooth
            AppCompatActivity actvty = new AppCompatActivity();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            actvty.startActivityForResult(enableBtIntent, 1);
        } catch (Exception e) {
            throw new BluetoothSupportError();
        }
    }

    public void establishConnection(Context context) {

        CatchError error = new CatchError();

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = null;

        try {
            device = btAdapter.getRemoteDevice(settings.MACADDR);
        } catch (Exception e) {
            error.caughtError(context, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }

        // Two things are needed to make a connection:
        // A MAC address, which we got above.
        // A Service ID or UUID.  In this case we are using the UUID for SPP.
        try {
            assert device != null;
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
            error.caughtError(context, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }

        Toast.makeText(context, "Connecting to server", Toast.LENGTH_LONG).show();

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
        } catch (IOException e) {
            String msg = "An exception occurred during connection, socket closed: " + e.getMessage();
            error.caughtError(context, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

}
