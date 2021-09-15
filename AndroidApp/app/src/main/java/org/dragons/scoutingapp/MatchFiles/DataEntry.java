package org.dragons.scoutingapp.MatchFiles;

import androidx.annotation.Nullable;

/**
 * Created by Stephen Ogden on 9/14/21.
 * FRC 1595.
 */
public class DataEntry<T, V extends android.view.View> {

	/**
	 * Documentation
	 */
	public final String name;

	/**
	 * Documentation
	 */
	@Nullable
	public T value;

	/**
	 * Documentation
	 */
	@Nullable
	public V view;

	/**
	 * Documentation
	 * @param name
	 */
	@org.jetbrains.annotations.Contract(pure = true)
	public DataEntry(String name) {
		this.name = name;
	}
}
