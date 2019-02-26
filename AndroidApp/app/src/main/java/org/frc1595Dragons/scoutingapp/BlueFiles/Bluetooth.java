package org.frc1595Dragons.scoutingapp.BlueFiles;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import org.frc1595Dragons.scoutingapp.MatchFiles.Match;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Stephen Ogden on 6/23/2018.
 * FRC 1595
 */

public class Bluetooth {

	// Bluetooth adapter (think of it as the specific microchip on the phone)
	public final static BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

	public static String MAC;

	public static Match matchData = new Match();

	public static Bluethread bluetoothConnection;

	// Bluetooth device (The receiver that the phone connects and sends data to)
	public static android.bluetooth.BluetoothDevice device;

	Bluetooth() {
		// Make sure bluetooth is on
		Log.d("Bluetooth enabled", Boolean.toString(this.isBluetoothOn()));
	}

	static void setMatchData(JSONObject json) throws org.json.JSONException {
		Log.d("fullData", json.toString());

		// Get the autonomous stuff
		final JSONObject rawAutonomous = json.optJSONObject("Autonomous");
		final int autoSize = rawAutonomous.length();
		Log.d("rawAutonomous", rawAutonomous.toString());
		Log.d("rawAutonomousSize", Integer.toString(autoSize));
		Bluetooth.matchData.autonomousData = Match.matchBaseToAutonomous(Match.getMatchData(rawAutonomous, autoSize));

		// Get the teleop stuff
		final JSONObject rawTeleOp = json.optJSONObject("TeleOp");
		final int teleSize = rawTeleOp.length();
		Log.d("rawTeleOp", rawTeleOp.toString());
		Log.d("teleSize", Integer.toString(teleSize));
		Bluetooth.matchData.teleopData = Match.matchBaseToTeleOp(Match.getMatchData(rawTeleOp, teleSize));


		// Get the endgame stuff
		final JSONObject rawEndgame = json.optJSONObject("Endgame");
		final int endSize = rawEndgame.length();
		Log.d("rawEndgame", rawEndgame.toString());
		Log.d("endSize", Integer.toString(endSize));
		Bluetooth.matchData.endgameData = Match.matchBaseToEndgame(Match.getMatchData(rawEndgame, endSize));

	}

	public static void close() throws IOException {
		Bluetooth.MAC = null;
		if (Bluetooth.bluetoothConnection != null && Bluetooth.bluetoothConnection.isAlive()) {
			Bluetooth.bluetoothConnection.close(true);
		}
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
			new android.support.v7.app.AppCompatActivity().startActivityForResult(new android.content.Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
		}
		return isOn;
	}

}
