package org.frc1595Dragons.scoutingapp.BlueFiles;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Stephen Ogden on 1/16/19.
 * FRC 1595
 */
public class Bluethread extends Thread {

    public String deviceName;

    private BufferedReader input;
    private BufferedWriter output;
    private BluetoothSocket socket;

    private java.util.Queue<Request> blueQueue = new java.util.LinkedList<>();


    public Bluethread(BluetoothSocket socket) throws IOException {
        this.socket = socket;
        this.socket.connect();
        this.deviceName = socket.getRemoteDevice().getName();
        this.input = new BufferedReader(new java.io.InputStreamReader(this.socket.getInputStream()));
        this.output = new BufferedWriter(new java.io.OutputStreamWriter(this.socket.getOutputStream()));
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
                        switch (this.parseRequest(object)) {
                            case REQUEST_CLOSE:
                                Log.d("Validated object", "Requested closure");
                                this.close(false);
                                break;
                            case CONFIG:
                                Log.d("Validated object", "Config");
                                JSONObject config = object.getJSONObject(Request.Requests.CONFIG.name());
                                Bluetooth.setMatchData(config);
                                break;
                            case REQUEST_PING:
                                Log.d("Validated object", "Requested ping");
                                this.sendData(Request.Requests.REQUEST_PING, new JSONObject(Long.toString(System.currentTimeMillis() % 1000)));
                                break;
                            case DATA:
                                Log.d("Validated object", "Its data!");
                                Log.d("Data-dump", object.optString(Request.Requests.DATA.name()));
                                break;
                        }
                    } catch (org.json.JSONException e) {
                        Log.w("Wasn't valid json", in);
                    }

                }

                // For processing output, get whatever is in the queue
                if (!blueQueue.isEmpty()) {
                    Request request = blueQueue.poll();

                    if (request.requests.equals(Request.Requests.REQUEST_CLOSE)) {
                        this.close(true);
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
            this.close(false);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private Request.Requests parseRequest(JSONObject input) {
        if (input.has(Request.Requests.REQUEST_CLOSE.name())) {
            return Request.Requests.REQUEST_CLOSE;
        } else if (input.has(Request.Requests.REQUEST_PING.name())) {
            return Request.Requests.REQUEST_PING;
        } else if (input.has(Request.Requests.CONFIG.name())) {
            return Request.Requests.CONFIG;
        } else {
            return Request.Requests.DATA;
        }
    }


    /**
     * Close the socket/thread.
     *
     * @param isRequest Whether or not to write the close request to the stream.
     * @throws IOException For when something goes wrong...
     */
    private void close(boolean isRequest) throws IOException {
        Log.d("Bluethread", "Closing");
        if (isRequest) {
            this.output.write("Requesting close");
        }
        this.output.flush();
        this.output.close();
        this.blueQueue.clear();
        this.input.close();
        this.socket.close();
        Bluetooth.matchData = null;
        Bluetooth.MAC = null;
    }

    public void sendData(Request.Requests request, JSONObject string) {
        blueQueue.add(new Request(request, string));
    }
}