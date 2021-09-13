package org.dragons.scoutingapp.MatchFiles;

/**
 * Created by Stephen Ogden on 9/12/21.
 * FRC 1595.
 */
public class DataEntry {

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
	public String[] values;

	/**
	 * Valid data-types for anything that extends the MatchBase.
	 */
	public enum DataType {

		/**
		 * Documentation
		 */
		Text,

		/**
		 * Documentation
		 */
		Number,

		/**
		 * Documentation
		 */
		Boolean,

		/**
		 * Documentation
		 */
		BooleanGroup
	}

}
