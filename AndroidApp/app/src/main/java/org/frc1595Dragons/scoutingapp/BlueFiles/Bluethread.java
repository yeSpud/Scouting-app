package org.frc1595Dragons.scoutingapp.BlueFiles;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Stephen Ogden on 1/16/19.
 * FRC 1595
 */
public class Bluethread extends Thread {

	public String deviceName;

	private BufferedReader input;
	private OutputStream output;
	private BluetoothSocket socket;

	public Bluethread(BluetoothSocket socket) throws IOException {
		this.socket = socket;
		this.socket.connect();
		this.deviceName = socket.getRemoteDevice().getName();
		this.input = new BufferedReader(new java.io.InputStreamReader(this.socket.getInputStream()));
		this.output = this.socket.getOutputStream();
	}

	public void run() {
		Log.i("Bluethread", "Running!");
		while (this.socket != null && this.socket.isConnected()) {
			String in = null;
			try {
				// Process input
				if (input.ready()) {
					in = input.readLine();
					Log.d("Current in", in);
				}
			} catch (IOException e) {
				if (!e.toString().equals("java.io.IOException: BufferedReader is closed")) {
					e.printStackTrace();
				}
			}

			if (in != null && !in.equals("")) {
				try {
					// Parse the input to a Json object
					JSONObject object = new JSONObject(in);
					Log.d("Received object", object.toString());
					switch (this.parseRequest(object)) {
						case REQUEST_CLOSE:
							Log.d("Validated object", "Requested closure");
							try {
								this.close(false);
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						case CONFIG:
							Log.d("Validated object", "Config");
							JSONObject config = object.getJSONObject(Request.Requests.CONFIG.name());
							Bluetooth.setMatchData(config);
							break;
						case REQUEST_PING:
							Log.d("Validated object", "Requested ping");
							this.sendData(new Request(Request.Requests.REQUEST_PING, new JSONObject(Long.toString(System.currentTimeMillis() % 1000))));
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
		}

		// Be sure to close the socket
		try {
			this.close(false);
		} catch (IOException e) {
			if (!e.toString().equals("java.io.IOException: socket closed")) {
				e.printStackTrace();
			}
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
	public void close(boolean isRequest) throws IOException {
		Log.d("Bluethread", "Closing");
		if (isRequest) {
			this.sendData(new Request(Request.Requests.REQUEST_CLOSE, null));
		}
		this.output.flush();
		this.output.close();
		this.input.close();
		this.socket.close();
		Bluetooth.matchData = null;
		Bluetooth.MAC = null;
	}

	public void sendData(Request request) {
		Log.d("Out", "Sending data");
		// Check for nulls in the data
		request.data = request.data == null ? request.data = new JSONObject() : request.data;
		try {
			this.output.write(String.format("{\"%s\":%s}\n", request.requests.name(), request.data.toString()).getBytes());
			this.output.flush();
		} catch (IOException e) {
			if (!e.toString().equals("java.io.IOException: socket closed")) {
				e.printStackTrace();
			}
		}
	}
}