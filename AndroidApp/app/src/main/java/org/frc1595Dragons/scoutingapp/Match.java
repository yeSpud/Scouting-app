package org.frc1595Dragons.scoutingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stephen Ogden on 12/24/18.
 * FTC 6128 | 7935
 * FRC 1595
 */
public class Match {

    public Autonomous[] autonomousData;
    public TeleOp[] teleopData;
    public Endgame[] endgameData;

    public static MatchBase[] getMatchData(JSONObject rawData, int size) throws JSONException, NullPointerException {

        MatchBase[] fullMatchData = new MatchBase[size];

        String[] keys = rawData.names().toString().replace("[", "").replace("]", "").replace("\"", "").split(",");
        Log.d("Keys", Arrays.toString(keys));
        for (int i = 0; i < size; i++) {
            String key = keys[i];
            Log.d("Key", key);

            JSONArray jsonArray = rawData.optJSONArray(key);
            Log.d("JsonArray", rawData.getJSONArray(key).toString());

            MatchBase match = new MatchBase();

            match.name = key;
            match.datatype = MatchBase.DataType.valueOf(jsonArray.getString(0));

            final int jsonArraySize = jsonArray.length();
            Log.d("JsonArraySize", Integer.toString(jsonArraySize));

            ArrayList<String> values = new ArrayList<>();
            for (int k = 1; k < jsonArraySize; k++) {
                values.add(jsonArray.getString(k));
                Log.d("Value", jsonArray.getString(k));
            }
            match.value = values;

            fullMatchData[i] = match;

        }

        return fullMatchData;
    }

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

    public static TeleOp[] matchBaseToTeleOp(MatchBase[] matchBase) {
        TeleOp[] teleops = new TeleOp[matchBase.length];

        for (int i = 0; i < matchBase.length; i++) {
            TeleOp teleop = new Match().new TeleOp();
            teleop.name = matchBase[i].name;
            teleop.datatype = matchBase[i].datatype;
            teleop.value = matchBase[i].value;
            teleops[i] = teleop;
        }

        return teleops;
    }

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

    public class Autonomous extends MatchBase {

    }

    public class TeleOp extends MatchBase {

    }

    public class Endgame extends MatchBase {

    }

}
