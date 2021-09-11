package org.dragons.scoutingapp.BlueFiles;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import org.dragons.scoutingapp.MatchFiles.Match;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Stephen Ogden on 6/23/2018.
 * FRC 1595
 */

public class Bluetooth {

	/**
	 * Bluetooth adapter (think of it as the specific microchip on the phone).
	 */
	public static BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

	public static String MAC;

	public static Match matchData = new Match();

	public static boolean hasMatchData = false;

	public static Bluethread bluetoothConnection;

	/**
	 * Bluetooth device (The server that the phone connects and sends data to).
	 */
	public static android.bluetooth.BluetoothDevice device;

	/**
	 * Sets the matchData (config file) based on the JSON that was provided.
	 *
	 * @param json The match (config) data to parse.
	 * @throws org.json.JSONException Will throw a JSONException in the event that some of the JSON is malformed.
	 */
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

	/**
	 * Closes the bluetooth connection.
	 *
	 * @throws IOException Will throw an IOException in the event that something goes wrong during the process.
	 */
	public static void close() throws IOException {
		Bluetooth.MAC = null;
		if (Bluetooth.bluetoothConnection != null && Bluetooth.bluetoothConnection.isAlive()) {
			Bluetooth.bluetoothConnection.close(true);
		}
	}

	/**
	 * Checks if bluetooth is enabled on this device.
	 *
	 * @return Whether or not bluetooth is enabled on this device.
	 */
	public static boolean isBluetoothOn() {
		try {
			// Check of the adapter is enabled or not
			return Bluetooth.btAdapter.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

}
