package org.dragons.scoutingapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
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
		this.binder.setActivity(new MainActivityViewModel());

		this.binder.connect.setOnClickListener((event) -> this.startActivity(new Intent(this, EnterMACAddress.class)));

		// Find and add a listener to the start button
		this.binder.start.setOnClickListener((event) -> {
			if (BlueThread.INSTANCE.getRunning()) {
				if (BlueThread.INSTANCE.getHasMatchData()) {
					this.startScouting().show();
				} else {
					Toast.makeText(this, "Config still being loaded. Try again in a few seconds.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// Find and add a listener to the disconnect button
		this.binder.disconnect.setOnClickListener((event) -> {
				try {
					// Disconnect from the server
					BlueThread.INSTANCE.close(true);
				} catch (IOException e) {
					this.log("Could not close connection to bluethread server! " + e.getCause());
				}
			}
		);
	}

	@Override
	protected void onResume() {
		Log.v("onResume", "Called onResume");
		super.onResume();

		this.binder.consoleView.removeAllViews();

		this.binder.getActivity().setupCoroutine(this).start();

		Log.v("onResume", "Finished onResume");
	}

	/**
	 * Documentation
	 *
	 * @param message
	 */
	void log(String message) {
		TextView textView = new TextView(this.getApplication());
		textView.setText(message);
		textView.setTextSize(12);
		textView.setTextColor(this.getApplication().getResources().getColor(R.color.white));
		this.binder.consoleView.addView(textView);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == this.hashCode()) {
			for (int grantResult : grantResults) {
				if (grantResult == PackageManager.PERMISSION_DENIED) { this.finish(); }
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == BluetoothAdapter.STATE_ON) {

			if (resultCode == RESULT_OK) {
				this.onResume();
			}
		}
	}

	/**
	 * Create the popup menu for entering in the team number, in order to start scouting a team.
	 *
	 * @return The <a href='https://developer.android.com/guide/topics/ui/dialogs#java'>dialog box</a>.
	 */
	@NonNull
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

				int teamNumber = 0;

				// When the teamNumber entered is valid, we can set the teamNumber to the entered value, and then start the data collection activity
				try {
					teamNumber = Integer.parseInt(userInput);
				} catch (NumberFormatException e) {
					// TODO Log error
					dialog.cancel();
				}

				Intent collectionIntent = new Intent(this, DataCollection.class);
				collectionIntent.putExtra("Team number", teamNumber);

				this.startActivity(collectionIntent);
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

		// Return the dialog
		return alertDialogBuilder.create();
	}
}
