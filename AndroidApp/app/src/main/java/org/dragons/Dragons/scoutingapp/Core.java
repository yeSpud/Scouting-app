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
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Stephen Ogden on 6/23/2018.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Core {

    // String for the data that will be sent to the receiver
    public static String data;
    // The number of the team to scout
    public static int number;
    // Bluetooth socket (think of this as a sort of web based server connection)
    static BluetoothSocket btSocket;
    // Bluetooth adapter (think of it as the specific microchip on the phone)
    private static BluetoothAdapter btAdapter;

    // Bluetooth device (The receiver that the phone connects and sends data to)
    private static BluetoothDevice device;

    // The declared, but uninitialized UUID for the server
    private UUID MY_UUID;

    Core() throws BluetoothSupportError {
        try {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            // Well known SPP UUID
            MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        } catch (Exception e) {
            // The only reason why this should fail, is if bluetooth is not supported on the device
            throw new BluetoothSupportError();
        }
    }

    // Function that checks if a MAC address has been entered
    static boolean enteredMac() {
        try {
            // Try to return if its NOT empty
            return !settings.MACADDR.isEmpty();
        } catch (Exception e) {
            // If that fails, then it is not entered, so return false
            return false;
        }
    }

    // Function that checks if the current phone language is set to chinese
    static boolean isSetInChinese() {
        // 你好？
        return Locale.getDefault().getDisplayLanguage().equals(Locale.CHINESE.getDisplayLanguage());
    }

    // Function to check if bluetooth is enabled
    boolean isBluetoothOn() {
        // For starters, set it to false, as a fail-safe
        boolean isOn = false;
        try {
            // Check of the adapter is enabled or not
            isOn = btAdapter.isEnabled();
        } catch (Exception NullPointerException) {
            try {
                // If its null, one cause is that its just turned off, so try re-enabling it
                requestBluetoothToggle();
            } catch (Exception BluetoothSupportError) {
                // The other cause is that its not supported on the device, in which case, it says false
                isOn = false;
            }
        }
        return isOn;
    }

    // Function that checks if the bluetooth socket is connect to the receiver
    boolean isBluetoothConnected() {
        try {
            // Simply try to return whether or not the socket is connected
            return btSocket.isConnected();
        } catch (NullPointerException NPE) {
            // If it throws a NPE, then its not connected, so return false
            return false;
        }
    }

    // Function that prompts the user to turn on bluetooth
    void requestBluetoothToggle() {
        try {
            // Prompt user to turn on Bluetooth
            AppCompatActivity activity = new AppCompatActivity();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
            // TODO: Simply close the app if the user denies the request
        } catch (Exception e) {
            // If that fails, then throw an error, as the device does not support bluetooth
            System.exit(-1);
            ;
        }
    }

    // Function to establish a connection with the receiver. The context provided is simply for error catching
    void establishConnection(Context context) {

        CatchError error = new CatchError();

        try {
            // Try to set the receiver based on the MAC address entered
            device = btAdapter.getRemoteDevice(settings.MACADDR);
        } catch (Exception e) {
            error.caughtError(context, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }

        try {
            assert device != null;
            // Try setting the bluetooth socket that of the receiver (the device)
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException | NullPointerException e) {
            error.caughtError(context, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }

        // Make sure that discovery is off, as its fairly resource intensive
        btAdapter.cancelDiscovery();

        // TODO: Replace with a better loading overlay
        // Give the user a heads up that the connection is being established
        Toast.makeText(context, "Connecting to server", Toast.LENGTH_LONG).show();

        // Establish the connection with the receiver. This will block until it connects.
        try {
            btSocket.connect();
        } catch (IOException | NullPointerException e) {
            error.caughtError(context, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

}
