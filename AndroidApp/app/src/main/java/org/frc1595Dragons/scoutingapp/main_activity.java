package org.frc1595Dragons.scoutingapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Stephen Ogden on 3/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class main_activity extends AppCompatActivity {

    public Core core;

    private Button start;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        // Set the view to the main_activity layout
        this.setContentView(R.layout.main_activity);

        // Find and then add a listener to the settings button
        findViewById(R.id.settings).setOnClickListener((event) -> this.enterMACAddress().show());

        // Find and then add a listener to the start button
        start = findViewById(R.id.start);
        start.setOnClickListener((event) -> this.startScouting().show());

    }

    protected void onResume() {
        super.onResume();

        start.setVisibility(Core.MAC != null && !Core.MAC.equals("") ? View.VISIBLE : View.GONE);

        // TODO: Check if the connection to the bluetooth server still exists
    }

    // https://developer.android.com/guide/topics/ui/dialogs#java
    private Dialog startScouting() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View teamNumberDialog = inflater.inflate(R.layout.teamselection, null);

        // Set the view, along with the positive and negative button actions
        alertDialogBuilder.setView(teamNumberDialog);
        alertDialogBuilder.setPositiveButton("Start scouting", (dialog, id) -> {

            String userInput = ((EditText) teamNumberDialog.findViewById(R.id.editTextDialogUserInput)).getText().toString();
            // Lets add some checks to see if there was anything entered in the first place
            // If there wasn't, Toast should display "Please enter a team teamNumber to scout"
            if (!userInput.equals("") && userInput != null) {
                // When the teamNumber entered is valid, we can set the teamNumber to the entered value, and then start the data collection activity
                try {
                    Core.teamNumber = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    dialog.cancel();
                }
                this.startActivity(new Intent(main_activity.this, data_collection.class));
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        // Return the dialog
        return alertDialogBuilder.create();


    }

    // https://developer.android.com/guide/topics/ui/dialogs#java
    private Dialog enterMACAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View enterMacAddressDialog = inflater.inflate(R.layout.macaddress, null);

        // Set the view, along with the positive and negative button actions
        builder.setView(enterMacAddressDialog);
        // TODO: Attempt to connect once saved
        builder.setPositiveButton("Save", (dialog, id) -> {
            Core.MAC = ((EditText) enterMacAddressDialog.findViewById(R.id.macAddressInput)).getText().toString();

            // Refresh the start button visibility
            start.setVisibility(Core.MAC != null && !Core.MAC.equals("") ? View.VISIBLE : View.GONE);
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        // Return the dialog
        return builder.create();
    }
}
