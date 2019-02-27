package org.frc1595Dragons.scoutingapp.MatchFiles;

/**
 * Created by Stephen Ogden on 12/29/18.
 * FRC 1595
 */
public class MatchBase {

	/**
	 * The name of the match data.
	 */
	public String name;

	/**
	 * The DataType of the match data.
	 */
	public DataType datatype;

	/**
	 * The value of the match data.
	 */
	public java.util.ArrayList<String> value;

	/**
	 * Valid data-types for anything that extends the MatchBase.
	 */
	public enum DataType {
		Text, Number, Boolean, BooleanGroup
	}

}
