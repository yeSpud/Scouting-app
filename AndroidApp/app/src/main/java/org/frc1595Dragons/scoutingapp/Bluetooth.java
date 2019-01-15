package org.frc1595Dragons.scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Match matchData = new Match();


    // TODO: Remove this test code and get the actual code from the server
    public static final String test = "{\n\t\"Autonomous\" : {\n\t\t\"Do the thing\" : [\"Boolean\", false]\n\t},\n\t"
            + "\"TeleOp\" : {\n\t\t\"Get the points\" : [\"Number\", 0, 0, 25, 1],\n\t\t\"Win\" : [\"Number\", 0, 0, 25, 1]\n\t},\n\t"
            + "\"Endgame\" : {\n\t\t\"Info\" : [\"Text\", \"See the readme for config documentatiuon\"]\n\t}\n}";

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


    public static void setMatchData(String json) throws JSONException {
        JSONObject fullData = new JSONObject(json);
        Log.d("fullData", fullData.toString());

        // Get the autonomous stuff
        final JSONObject rawAutonomous = fullData.optJSONObject("Autonomous");
        final int autoSize = rawAutonomous.length();
        Log.d("rawAutonomous", rawAutonomous.toString());
        Log.d("rawAutonomousSize", Integer.toString(autoSize));
        Bluetooth.matchData.autonomousData = Match.matchBaseToAutonomous(Match.getMatchData(rawAutonomous, autoSize));

        // Get the teleop stuff
        final JSONObject rawTeleOp = fullData.optJSONObject("TeleOp");
        final int teleSize = rawTeleOp.length();
        Log.d("rawTeleOp", rawTeleOp.toString());
        Log.d("teleSize", Integer.toString(teleSize));
        Bluetooth.matchData.teleopData = Match.matchBaseToTeleOp(Match.getMatchData(rawTeleOp, teleSize));


        // Get the endgame stuff
        final JSONObject rawEndgame = fullData.optJSONObject("Endgame");
        final int endSize = rawEndgame.length();
        Log.d("rawEndgame", rawEndgame.toString());
        Log.d("endSize", Integer.toString(endSize));
        Bluetooth.matchData.endgameData = Match.matchBaseToEndgame(Match.getMatchData(rawEndgame, endSize));

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
