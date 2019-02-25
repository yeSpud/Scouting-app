package org.frc1595Dragons.scoutingapp.MatchFiles;

/**
 * Created by Stephen Ogden on 12/29/18.
 * FRC 1595
 */
public class MatchBase {

    public String name;
    public DataType datatype;
    public java.util.ArrayList<String> value;

    public enum DataType {
        Text, Number, Boolean, BooleanGroup
    }

}
