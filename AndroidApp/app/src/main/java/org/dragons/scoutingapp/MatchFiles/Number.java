package org.dragons.scoutingapp.MatchFiles;

/**
 * Created by Stephen Ogden on 9/14/21.
 * FRC 1595.
 */
public class Number extends DataEntry<Integer, com.shawnlin.numberpicker.NumberPicker> {

	/**
	 * Documentation
	 */
	public final int minimumValue;

	/**
	 * Documentation
	 */
	public final int maximumValue;

	/**
	 * Documentation
	 */
	public final int stepValue;

	/**
	 * Documentation
	 * @param name
	 * @param minimumValue
	 * @param maximumValue
	 * @param stepValue
	 */
	public Number(String name, int minimumValue, int maximumValue, int stepValue) {
		super(name);
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.stepValue = stepValue;
	}
}
