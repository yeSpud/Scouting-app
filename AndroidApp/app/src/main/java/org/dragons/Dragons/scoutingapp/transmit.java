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

public class transmit extends AppCompatActivity {

    public static OutputStream outStream;


    public void AlertBox(@SuppressWarnings("SameParameterValue") String title, String message ) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message + " Press OK to exit." ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }


    public void sendData(String data) {
        try {
            outStream = settings.btSocket.getOutputStream();
            outStream.write(main_activity.data.getBytes(), 0, main_activity.data.getBytes().length);
            outStream.flush();
            Log.e("outString", main_activity.data);
            Log.e("outBytes", Arrays.toString(main_activity.data.getBytes()));
            //outStream.close();
        } catch (Exception e) {
            String msg = "An exception occurred during write: " + e.getMessage();
            if (settings.MACADDR.equals("00:00:00:00:00:00")) {
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            }
            msg = msg + ".\n\nCheck that the SPP UUID exists on server.\n\n";
            AlertBox("Error", msg);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        main_activity.data = null;
        Thread.yield();

        try {
            outStream.close();
        } catch (IOException e) {
            Toast.makeText(transmit.this, "Error closing stream!", Toast.LENGTH_LONG).show();
        }
    }

}
