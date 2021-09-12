package org.dragons.scoutingapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.bluetooth.BluetoothAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.dragons.scoutingapp.bluefiles.BlueThread;
import org.dragons.scoutingapp.R;
import org.dragons.scoutingapp.databinding.MainActivityBinding;

import java.io.IOException;


/**
 * Created by Stephen Ogden on 3/23/17.
 * FRC 1595
 */
public class MainActivity extends Activity {

	/**
	 * Documentation
	 */
	MainActivityBinding binder;

	protected void onCreate(android.os.Bundle savedInstance) {
		super.onCreate(savedInstance);

		this.binder = DataBindingUtil.setContentView(this, R.layout.main_activity);

		this.binder.connect.setOnClickListener((event) -> this.enterMACAddress().show());

		// Find and add a listener to the start button
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
					this.log("Could not close connection to bluethread server! " + e.getCause());
				}
			}
		);
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.log("Checking if bluetooth is enabled...");
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			this.log("Bluetooth is enabled");
			return;
		} else {
			this.log("Bluetooth is disabled");
		}

		this.log("Asking user to turn on bluetooth...");
		// TODO

	}

	/**
	 * Documentation
	 *
	 * @param message
	 */
	private void log(String message) {

		TextView textView = new TextView(this);
		textView.setText(message);
		textView.setTextSize(12);
		textView.setTextColor(this.getResources().getColor(R.color.white));

		this.binder.consoleView.addView(textView);
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
	private Dialog enterMACAddress() { // TODO Move this to its own class (and integrate a QR code reader).
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
}
