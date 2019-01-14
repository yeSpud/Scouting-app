package org.frc1595Dragons.scoutingapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

// Update this to take the config file sent from the server and dynamically generate data
// FIXME
public class data_collection extends AppCompatActivity {

    public static int teamNumber;

    // Since we cant store the individual widgets, just store their ids for future lookup
    private ArrayList<Integer> NodeIDs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_collection);

        // For a nice little accessibility feature, we can set the top bar to display the team teamNumber that the user is scouting
        // That way, they don't forget, or scout the wrong team :P
        this.setTitle(getResources().getString(R.string.teamToScout) + data_collection.teamNumber);

        // Get the scrollview section of the page to dynamically load the widgets for data collection
        LinearLayout contentView = findViewById(R.id.content);

        // TODO: Generate this page dynamically

        // First, add the autonomous section header
        contentView.addView(this.generateTextView("Autonomous:", 20));

        contentView.addView(this.generateTextView("TeleOp:", 20));

        contentView.addView(this.generateTextView("End game:", 20));


    }

    private TextView generateTextView(String text, float size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(size);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        return textView;
    }

}
