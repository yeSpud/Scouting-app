package org.dragons.scoutingapp.MatchFiles;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Stephen Ogden on 12/29/18.
 * FRC 1595
 */
public class MatchData {

	/**
	 * Documentation
	 */
	public final DataEntry[] dataEntries;

	/**
	 * Documentation
	 * @param jsonObject
	 * @param numberOfEntries
	 */
	@Contract(pure = true)
	public MatchData(@NonNull JSONObject jsonObject, int numberOfEntries) {

		this.dataEntries = new DataEntry[numberOfEntries];

		// Get and iterate over the keys in the data
		Iterator<String> keys = jsonObject.keys();
		int i = 0;
		while (keys.hasNext()) {
			String key = keys.next().replace("\\", "");
			Log.d("Key", key);

			JSONArray jsonArray = jsonObject.optJSONArray(key);
			Log.d("JsonArray", jsonArray.toString());

			DataEntry dataEntry = new DataEntry();

			dataEntry.name = key;
			dataEntry.datatype = DataEntry.DataType.valueOf(jsonArray.optString(0).replace("\\", ""));

			String[] values = new String[jsonArray.length()];
			for (int j = 1; j < jsonArray.length(); j++) {
				String value = jsonArray.optString(j).replace("\\", "");
				Log.d("Adding value", value);
				values[j] = value;
			}
			dataEntry.values = values;

			this.dataEntries[i] = dataEntry;
			i++;
		}
	}
}
