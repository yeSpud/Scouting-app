package org.dragons.scoutingapp.MatchFiles;

import android.view.View;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Stephen Ogden on 9/14/21.
 * FRC 1595.
 */
public class DataEntry<T> {

	/**
	 * Documentation
	 */
	public final String name;

	/**
	 * Documentation
	 */
	public T value;

	/**
	 * Documentation
	 */
	@Nullable
	public View view;

	/**
	 * Documentation
	 * @param name
	 */
	@Contract(pure = true)
	public DataEntry(String name) {
		this.name = name;
	}
}
