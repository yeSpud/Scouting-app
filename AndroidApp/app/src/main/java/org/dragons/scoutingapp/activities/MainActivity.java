package org.dragons.scoutingapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.dragons.scoutingapp.bluefiles.BlueThread;
import org.dragons.scoutingapp.R;
import org.dragons.scoutingapp.databinding.MainActivityBinding;

import java.io.IOException;

/**
 * Created by Stephen Ogden on 3/23/17.
 * FRC 1595
 */
public class MainActivity extends AppCompatActivity {

	/**
	 * Documentation
	 */
	MainActivityBinding binder;

	protected void onCreate(android.os.Bundle savedInstance) {
		super.onCreate(savedInstance);

		this.binder = MainActivityBinding.inflate(this.getLayoutInflater());

		// Make sure bluetooth is enabled - TODO Make this launch an intent to enable bluetooth if not enabled.
		boolean bluetoothEnabled = BluetoothAdapter.getDefaultAdapter().isEnabled();
		android.util.Log.d("Bluetooth enabled", Boolean.toString(bluetoothEnabled));
		if (!bluetoothEnabled) {
			Toast.makeText(this, "Bluetooth is not currently enabled. Please enable Bluetooth and restart the app.", Toast.LENGTH_LONG).show();
			//this.binder.connect.setVisibility(View.GONE);
		}

		//this.binder.connect.setOnClickListener((event) -> this.enterMACAddress().show());

		// Find and add a listener to the start button
		/*
		this.binder.start.setOnClickListener((event) -> {
			try {
				if (!BlueThread.INSTANCE.getMACAddress().equals("")) {
					if (BlueThread.INSTANCE.getHasMatchData()) {
						this.startScouting().show();
					} else {
						Toast.makeText(this, "Config still being loaded. Try again in a few seconds.", Toast.LENGTH_LONG).show();
					}
				}
			} catch (NullPointerException NPE) {
				Toast.makeText(this, "Config still needs to be sent. Try again in a few seconds.", Toast.LENGTH_LONG).show();
			}
		});

		// Find and add a listener to the disconnect button
		this.binder.disconnect.setOnClickListener((event) -> {

			// Disconnect from the server and close the app
			try {
				BlueThread.INSTANCE.close(true);
			} catch (IOException e) {
				new ErrorActivity().new CatchError().Catch(this, e);
			}

			/*
			// Hide the start and disconnect buttons
			this.binder.start.setVisibility(View.GONE);
			this.binder.disconnect.setVisibility(View.GONE);
			this.binder.remoteDeviceName.setVisibility(View.GONE);
			 */
		//});

		// Set the start button, disconnect button, and server name to be visible if connected
		/*
		if (BlueThread.INSTANCE.getRunning()) {
			this.binder.disconnect.setVisibility(View.VISIBLE);
			this.binder.start.setVisibility(View.VISIBLE);
			this.binder.connect.setVisibility(View.GONE);
		} else {
			this.binder.disconnect.setVisibility(View.GONE);
			this.binder.start.setVisibility(View.GONE);
			this.binder.connect.setVisibility(View.VISIBLE);
		}
		 */
	}

	/**
	 * Create the popup menu for entering in the team number, in order to start scouting a team.
	 *
	 * @return The <a href='https://developer.android.com/guide/topics/ui/dialogs#java'>dialog box</a>.
	 */
	private Dialog startScouting() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		@SuppressLint("InflateParams") final View teamNumberDialog = inflater.inflate(R.layout.teamselection, null);

		// Set the view, along with the positive and negative button actions
		alertDialogBuilder.setView(teamNumberDialog);
		alertDialogBuilder.setPositiveButton("Start scouting", (dialog, id) -> {

			String userInput = ((EditText) teamNumberDialog.findViewById(R.id.editTextDialogUserInput)).getText().toString();
			if (!userInput.equals("")) {
				// When the teamNumber entered is valid, we can set the teamNumber to the entered value, and then start the data collection activity
				try {
					DataCollection.teamNumber = Integer.parseInt(userInput);
				} catch (NumberFormatException e) {
					dialog.cancel();
				}
				this.startActivity(new android.content.Intent(MainActivity.this, DataCollection.class));
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

		// Return the dialog
		return alertDialogBuilder.create();
	}

	/**
	 * Create the popup menu for entering in the server's MAC address, in order to connect to that server.
	 *
	 * @return The <a href='https://developer.android.com/guide/topics/ui/dialogs#java'>dialog box</a>.
	 */
	private Dialog enterMACAddress() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		@SuppressLint("InflateParams") final View enterMacAddressDialog = inflater.inflate(R.layout.macaddress, null);

		final EditText macAddress = enterMacAddressDialog.findViewById(R.id.macAddressInput);

		if (!BlueThread.INSTANCE.getMACAddress().equals("")) {
			macAddress.setText(BlueThread.INSTANCE.getMACAddress());
		}

		// Set the view, along with the positive and negative button actions
		builder.setView(enterMacAddressDialog);
		builder.setPositiveButton("Connect", (dialog, id) -> {
			String MACAddress = macAddress.getText().toString().toUpperCase();

			// Check if the MAC matches a regex (to see if its a valid format)
			if (java.util.regex.Pattern.compile("((\\d|[A-F]){2}:){5}(\\d|[A-F]){2}").matcher(MACAddress).matches()) {
				// Try to connect with the given MAC address
				//this.establishConnection(MACAddress);
				BlueThread.INSTANCE.start(MACAddress);

			} else {
				Toast.makeText(this, "Invalid MAC address", Toast.LENGTH_LONG).show();
			}
		});
		builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

		// Return the dialog
		return builder.create();
	}

	/*
	 * Establish a connection with the server.
	 */
	/*
	private void establishConnection(String macAddress) {
		try {
			// Show the start button, disconnect button, and server name to be visible if connected
			/*
			this.binder.disconnect.setVisibility(View.VISIBLE);
			this.binder.start.setVisibility(View.VISIBLE);
			this.binder.remoteDeviceName.setText(String.format(ENGLISH, "Connected to server: %s",
					BlueThread.INSTANCE.getRemoteDeviceName()));
			this.binder.remoteDeviceName.setVisibility(View.VISIBLE);
			this.binder.connect.setVisibility(View.GONE);


		} catch (Exception e) {
			new error_activity().new CatchError().Catch(this, e);
		}
	}
	 */

}
