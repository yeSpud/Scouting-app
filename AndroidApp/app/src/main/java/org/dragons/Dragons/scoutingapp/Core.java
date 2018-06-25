package org.dragons.Dragons.scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by Stephen Ogden on 6/23/2018.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Core {

    // Instead of the test bluetooth adapter, well use the official one here, since this will only be used when the device is shown to support bluetooth
    public static BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    // String for the data that will be sent to the receiver
    public static String data;

    // The number of the team to scout
    public static int number;

    // Well known SPP UUID
    public final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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
            return settings.btSocket.isConnected();
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
// TODO Add function to set error and stacktrace

}
