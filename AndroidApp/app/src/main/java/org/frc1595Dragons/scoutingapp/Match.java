package org.frc1595Dragons.scoutingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Stephen Ogden on 12/24/18.
 * FTC 6128 | 7935
 * FRC 1595
 */
public class Match {

    public Autonomous[] autonomousData;
    public TeleOp[] teleopData;
    public Endgame[] endgameData;

    public class Autonomous extends MatchBase {

    }

    public class TeleOp extends MatchBase {

    }

    public class Endgame extends MatchBase {

    }

    public static MatchBase[] getMatchData(JSONObject rawData, int size) {

        MatchBase[] fullMatchData = new MatchBase[size];

        int i = 0;
        while (rawData.keys().hasNext()) {
            Log.d("Key: ", rawData.keys().next());

            /*
            JSONArray jsonArray = rawData.getJSONArray(rawData.keys().next());
            Log.d("Json array", jsonArray.toString());

            MatchBase match = new MatchBase();

            match.name = rawData.keys().next();
            match.datatype = MatchBase.DataType.valueOf(JSONArray.getString(0));

            final int endgameArrayValues = jsonArray.length();
            ArrayList<JSONObject> values = new ArrayList<JSONObject>();
            for (int k = 1; k < endgameArrayValues; k++) {
                values.add(jsonArray.get(k));
            }
            match.value = values;

            fullMatchData[i] = match;
            i++;
            */

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

}
