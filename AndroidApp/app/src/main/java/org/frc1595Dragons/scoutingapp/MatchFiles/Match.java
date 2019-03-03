package org.frc1595Dragons.scoutingapp.MatchFiles;

import android.util.Log;

import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Stephen Ogden on 12/24/18.
 * FRC 1595
 */
public class Match {

	/**
	 * The array of the autonomous data in a match.
	 */
	public Autonomous[] autonomousData;

	/**
	 * The array of the teleop data in a match.
	 */
	public TeleOp[] teleopData;

	/**
	 * The array of the endgame data in a match.
	 */
	public Endgame[] endgameData;

	/**
	 * Gets the match data from a given JSON object.
	 *
	 * @param rawData The JSON object data.
	 * @param size    The size of the data.
	 * @return The match data. This still needs to be converted to either Autonomous, TeleOp, or Endgame data.
	 */
	public static MatchBase[] getMatchData(org.json.JSONObject rawData, int size) {

		MatchBase[] fullMatchData = new MatchBase[size];

		// Get and iterate over the keys in the data
		Iterator<String> keys = rawData.keys();
		int i = 0;
		while (keys.hasNext()) {
			String key = keys.next().replace("\\", "");
			Log.d("Key", key);

			org.json.JSONArray jsonArray = rawData.optJSONArray(key);
			Log.d("JsonArray", jsonArray.toString());

			MatchBase match = new MatchBase();

			match.name = key;
			match.datatype = MatchBase.DataType.valueOf(jsonArray.optString(0).replace("\\", ""));

			ArrayList<String> values = new ArrayList<>();
			for (int k = 1; k < jsonArray.length(); k++) {
				String ad = jsonArray.optString(k).replace("\\", "");
				Log.d("Adding value", ad);
				values.add(ad);
			}
			match.value = values;

			fullMatchData[i] = match;
			i++;
		}
		return fullMatchData;
	}

	/**
	 * Helper function that converts MatchBase data to Autonomous data.
	 *
	 * @param matchBase The MatchBase data to convert.
	 * @return The resulting Autonomous data.
	 */
	public static Autonomous[] matchBaseToAutonomous(MatchBase[] matchBase) {
		Autonomous[] autonomous = new Autonomous[matchBase.length];
		for (int i = 0; i < matchBase.length; i++) {
			Autonomous auto = new Match().new Autonomous();
			auto.name = "(Autonomous) " + matchBase[i].name;
			auto.datatype = matchBase[i].datatype;
			auto.value = matchBase[i].value;
			autonomous[i] = auto;
		}
		return autonomous;
	}

	/**
	 * Helper function that converts MatchBase data to TeleOp data.
	 *
	 * @param matchBase The MatchBase data to convert.
	 * @return The resulting TeleOp data.
	 */
	public static TeleOp[] matchBaseToTeleOp(MatchBase[] matchBase) {
		TeleOp[] t = new TeleOp[matchBase.length];
		for (int i = 0; i < matchBase.length; i++) {
			TeleOp teleop = new Match().new TeleOp();
			Log.d("TeleOpName", matchBase[i].name);
			teleop.name = matchBase[i].name;
			teleop.datatype = matchBase[i].datatype;
			teleop.value = matchBase[i].value;
			t[i] = teleop;
		}
		return t;
	}

	/**
	 * Helper function that converts MatchBase data to Autonomous data.
	 *
	 * @param matchBase The MatchBase data to convert.
	 * @return The resulting Autonomous data.
	 */
	public static Endgame[] matchBaseToEndgame(MatchBase[] matchBase) {
		Endgame[] endgames = new Endgame[matchBase.length];
		for (int i = 0; i < matchBase.length; i++) {
			Endgame endgame = new Match().new Endgame();
			endgame.name = matchBase[i].name;
			endgame.datatype = matchBase[i].datatype;
			endgame.value = matchBase[i].value;
			endgames[i] = endgame;
		}
		return endgames;
	}

	/**
	 * The Autonomous class that extends the MatchBase class.
	 */
	public class Autonomous extends MatchBase {
	}

	/**
	 * The TeleOp class that extends the MatchBase class.
	 */
	public class TeleOp extends MatchBase {
	}

	/**
	 * The Endgame class that extends the MatchBase class.
	 */
	public class Endgame extends MatchBase {
	}
}
