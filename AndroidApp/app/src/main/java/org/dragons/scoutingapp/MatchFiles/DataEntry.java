package org.dragons.scoutingapp.MatchFiles;

import org.jetbrains.annotations.Contract;

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
	 * @param name
	 */
	@Contract(pure = true)
	public DataEntry(String name) {
		this.name = name;
	}
}
