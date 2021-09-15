package org.dragons.scoutingapp.MatchFiles;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
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

		// Comments
		this.dataEntries = new DataEntry[numberOfEntries];

		// Get and iterate over the keys in the data
		Iterator<String> keys = jsonObject.keys();
		int i = 0;
		while (keys.hasNext()) {
			String key = keys.next().replace("\\", "");
			Log.d("Key", key);

			try {
				JSONArray jsonArray = jsonObject.getJSONArray(key);
				Log.d("JsonArray", jsonArray.toString());

				switch (jsonArray.getString(0).replace("\\", "")) {

					// Comments
					case "Text":
						Text text = new Text(key);
						text.value = jsonArray.getString(1);
						this.dataEntries[i] = text;
						break;

					// Comments
					case "Number":
						Number number = new Number(key, jsonArray.getInt(2), jsonArray.getInt(3),
								jsonArray.getInt(4));
						number.value = jsonArray.getInt(1);
						this.dataEntries[i] = number;
						break;

					// Comments
					case "Boolean":
						Boolean bool = new Boolean(key);
						bool.value = jsonArray.getBoolean(1);
						this.dataEntries[i] = bool;
						break;

					// Comments
					case "BooleanGroup":
						BooleanGroup booleanGroup = new BooleanGroup(key);
						JSONObject booleanGroupJson = jsonArray.getJSONObject(1);
						booleanGroup.value = new Boolean[booleanGroupJson.length()];
						Iterator<String> entries = booleanGroupJson.keys();
						int j = 0;
						while (entries.hasNext()) {
							String entryName = entries.next();
							Boolean b = new Boolean(entryName);
							b.value = booleanGroupJson.getBoolean(entryName);
							booleanGroup.value[j] = b;
							j++;
						}
						this.dataEntries[i] = booleanGroup;
						break;
				}
				i++;
			} catch (JSONException e) {
				Log.e("MatchData", "Unable to parse json", e);
			}
		}
	}
}
