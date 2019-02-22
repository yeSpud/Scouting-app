package org.frc1595Dragons.scoutingapp.BlueFiles;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Stephen Ogden on 1/16/19.
 * FRC 1595
 */
public class Bluethread extends Thread {

    public String deviceName;

    private BufferedReader input;
    private BufferedWriter output;
    private BluetoothSocket socket;

    private Queue<Request> blueQueue = new LinkedList<>();


    public Bluethread(BluetoothSocket socket) throws IOException {
        this.socket = socket;
        this.socket.connect();
        this.deviceName = socket.getRemoteDevice().getName();
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public void run() {

        while (this.socket.isConnected() && (!Bluetooth.MAC.equals(""))) {
            String in = null;
            try {

                // Process input
                if (input.ready()) {
                    in = input.readLine();
                }

                if (in != null && !in.equals("")) {
                    try {

                        // Parse the input to a Json object
                        JSONObject object = new JSONObject(in);
                        Log.d("Received object", object.toString());

                        // Check if the Json object is the config data
                        if (object.has(Request.Requests.CONFIG.name())) {
                            Log.d("Validated object", "Config");

                            // Load the config in order to dynamically generate the data collection page
                            JSONObject config = object.getJSONObject(Request.Requests.CONFIG.name());
                            Bluetooth.setMatchData(config);

                        } else if (object.has(Request.Requests.REQUEST_PING.name())) {
                            Log.d("Validated object", "Requested ping");

                            // Get the current time of this device in milliseconds
                            this.sendData(Request.Requests.REQUEST_PING, new JSONObject(Long.toString(System.currentTimeMillis() % 1000)));

                        } else if (object.has(Request.Requests.REQUEST_CLOSE.name())) {
                            Log.d("Validated object", "Requested closure");

                            // Close the socket/thread
                            input.close();
                            output.close();
                            blueQueue.clear();
                            socket.close();
                        } else {
                            Log.w("Unhandled object header", object.toString());
                        }
                    } catch (JSONException e) {
                        Log.w("Wasn't valid json", in);
                    }

                }

                // For processing output, get whatever is in the queue
                if (!blueQueue.isEmpty()) {
                    Request request = blueQueue.poll();

                    if (request.requests.equals(Request.Requests.REQUEST_CLOSE)) {
                        Log.d("Bluethread", "Closing");

                        // Close the socket/thread
                        output.write("Requesting close");
                        output.flush();
                        output.close();
                        blueQueue.clear();
                        input.close();
                        socket.close();
                    } else {
                        Log.d("Sending data", request.data.toString());
                        output.write(String.format("%s:%s", request.requests.name(), request.data.toString()));
                        output.flush();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Be sure to close the socket
        try {
            output.flush();
            output.close();
            blueQueue.clear();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendData(Request.Requests request, JSONObject string) {
        blueQueue.add(new Request(request, string));
    }
}