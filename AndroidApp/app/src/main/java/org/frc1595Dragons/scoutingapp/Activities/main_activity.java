package org.frc1595Dragons.scoutingapp.Activities;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.frc1595Dragons.scoutingapp.BlueFiles.Bluetooth;
import org.frc1595Dragons.scoutingapp.R;

import java.io.IOException;

import static java.util.Locale.ENGLISH;

/**
 * Created by Stephen Ogden on 3/23/17.
 * FRC 1595
 */
public class main_activity extends android.support.v7.app.AppCompatActivity {

	android.widget.Button StartScouting, Disconnect;

	android.widget.TextView ServerName;

	protected void onCreate(android.os.Bundle savedInstance) {
		super.onCreate(savedInstance);

		// Set the view to the main_activity layout
		this.setContentView(R.layout.main_activity);

		// Find and add a listener to the connect button
		this.findViewById(R.id.connect).setOnClickListener((event) -> this.enterMACAddress().show());

		// Find the server name text view
		ServerName = this.findViewById(R.id.remoteDeviceName);

		// Find and add a listener to the start button
		StartScouting = this.findViewById(R.id.start);
		StartScouting.setOnClickListener((event) -> {
			if (Bluetooth.MAC != null) {
				if (!Bluetooth.MAC.equals("")) {
					if (Bluetooth.matchData != null) {
						this.startScouting().show();
					}
				}
			}
		});

		// Find and add a listener to the disconnect button
		Disconnect = this.findViewById(R.id.disconnect);
		Disconnect.setOnClickListener((event) -> {
			// Disconnect from the server
			try {
				Bluetooth.close();
			} catch (IOException e) {
				new error_activity().new CatchError().Catch(this, e);
			}

			// Hide the start and disconnect buttons
			StartScouting.setVisibility(View.GONE);
			Disconnect.setVisibility(View.GONE);
			ServerName.setVisibility(View.GONE);
		});

		// Set the start button, disconnect button, and server name to be visible if connected
		if (Bluetooth.bluetoothConnection != null && Bluetooth.bluetoothConnection.isAlive()) {
			Disconnect.setVisibility(View.VISIBLE);
			StartScouting.setVisibility(View.VISIBLE);
			ServerName.setText(String.format(ENGLISH, "Connected to server: %s", Bluetooth.bluetoothConnection.deviceName));
			ServerName.setVisibility(View.VISIBLE);
		} else {
			Disconnect.setVisibility(View.GONE);
			StartScouting.setVisibility(View.GONE);
			ServerName.setVisibility(View.GONE);
		}

	}

	// https://developer.android.com/guide/topics/ui/dialogs#java
	private Dialog startScouting() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final View teamNumberDialog = inflater.inflate(R.layout.teamselection, null);

		// Set the view, along with the positive and negative button actions
		alertDialogBuilder.setView(teamNumberDialog);
		alertDialogBuilder.setPositiveButton("Start scouting", (dialog, id) -> {

			String userInput = ((EditText) teamNumberDialog.findViewById(R.id.editTextDialogUserInput)).getText().toString();
			if (!userInput.equals("")) {
				// When the teamNumber entered is valid, we can set the teamNumber to the entered value, and then start the data collection activity
				try {
					data_collection.teamNumber = Integer.parseInt(userInput);
				} catch (NumberFormatException e) {
					dialog.cancel();
				}
				this.startActivity(new android.content.Intent(main_activity.this, data_collection.class));
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

		// Return the dialog
		return alertDialogBuilder.create();


	}

	// https://developer.android.com/guide/topics/ui/dialogs#java
	private Dialog enterMACAddress() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final View enterMacAddressDialog = inflater.inflate(R.layout.macaddress, null);

		final EditText macAddress = enterMacAddressDialog.findViewById(R.id.macAddressInput);

		if (Bluetooth.MAC != null) {
			macAddress.setText(Bluetooth.MAC);
		}

		// Set the view, along with the positive and negative button actions
		builder.setView(enterMacAddressDialog);
		builder.setPositiveButton("Connect", (dialog, id) -> {
			Bluetooth.MAC = macAddress.getText().toString().toUpperCase();

			// Check if the MAC matches a regex
			if (java.util.regex.Pattern.compile("((\\d|[A-F]){2}:){5}(\\d|[A-F]){2}").matcher(Bluetooth.MAC).matches()) {
				// Try to connect with the given MAC address
				this.establishConnection();
			} else {
				Toast.makeText(this, "Invalid MAC address", Toast.LENGTH_LONG).show();
				Bluetooth.MAC = "";
			}
		});
		builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

		// Return the dialog
		return builder.create();
	}

	// Function to establish a connection with the receiver. The context provided is simply for error catching
	private void establishConnection() {
		try {
			if (Bluetooth.bluetoothConnection != null && Bluetooth.bluetoothConnection.isAlive()) {
				Bluetooth.close();
			}

			// Try to set the receiver based on the MAC address entered
			try {
				Bluetooth.device = Bluetooth.btAdapter.getRemoteDevice(Bluetooth.MAC);
			} catch (NullPointerException invalidMac) {
				Toast.makeText(this, "A known error occurred while connecting to the server", Toast.LENGTH_LONG).show();
				return;
			}

			// Well known SPP UUID
			android.bluetooth.BluetoothSocket btSocket = Bluetooth.device.createRfcommSocketToServiceRecord(java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

			// Make sure that discovery is off, as its fairly resource intensive
			Bluetooth.btAdapter.cancelDiscovery();

			// Start the bluetooth communication server (Bluethread)
			Bluetooth.bluetoothConnection = new org.frc1595Dragons.scoutingapp.BlueFiles.Bluethread(btSocket);
			Bluetooth.bluetoothConnection.start();

			// Show the start button, disconnect button, and server name to be visible if connected
			Disconnect.setVisibility(View.VISIBLE);
			StartScouting.setVisibility(View.VISIBLE);
			ServerName.setText(String.format(ENGLISH, "Connected to server: %s", Bluetooth.bluetoothConnection.deviceName));
			ServerName.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			new error_activity().new CatchError().Catch(this, e);
		}
	}

}
