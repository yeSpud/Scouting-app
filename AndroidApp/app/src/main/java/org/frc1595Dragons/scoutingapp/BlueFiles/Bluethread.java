package org.frc1595Dragons.scoutingapp.BlueFiles;

import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Stephen Ogden on 1/16/19.
 * FTC 6128 | 7935
 * FRC 1595
 */
public class Bluethread extends Thread {

    public int latency;

    private BufferedReader input;
    private BufferedWriter output;
    private BluetoothSocket socket;

    public Bluethread(BluetoothSocket socket) throws IOException {
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