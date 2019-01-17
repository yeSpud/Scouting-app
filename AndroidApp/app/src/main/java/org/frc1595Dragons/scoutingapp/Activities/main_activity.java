package org.frc1595Dragons.scoutingapp.Activities;

import android.app.Dialog;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.frc1595Dragons.scoutingapp.BlueFiles.Bluethread;
import org.frc1595Dragons.scoutingapp.BlueFiles.Bluetooth;
import org.frc1595Dragons.scoutingapp.R;
import org.json.JSONException;

import java.util.UUID;

/**
 * Created by Stephen Ogden on 3/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class main_activity extends android.support.v7.app.AppCompatActivity {

    private Button start;

    protected void onCreate(android.os.Bundle savedInstance) {
        super.onCreate(savedInstance);

        // Set the view to the main_activity layout
        this.setContentView(R.layout.main_activity);

        // Find and then add a listener to the MAC address button
        this.findViewById(R.id.settings).setOnClickListener((event) -> this.enterMACAddress().show());

        // Find and then add a listener to the start button
        this.start = this.findViewById(R.id.start);
        this.start.setOnClickListener((event) -> this.startScouting().show());

    }

    protected void onResume() {
        super.onResume();

        this.start.setVisibility(Bluetooth.MAC != null && !Bluetooth.MAC.equals("") ? View.VISIBLE : View.GONE);

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
            if (!userInput.equals("")) {
                // When the teamNumber entered is valid, we can set the teamNumber to the entered value, and then start the data collection activity
                try {
                    Bluetooth.setMatchData(Bluetooth.test);
                    data_collection.teamNumber = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    dialog.cancel();
                } catch (JSONException e) {
                    new error_activity().new CatchError().Catch(this, e);
                    return;
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
        builder.setPositiveButton("Save", (dialog, id) -> {
            Bluetooth.MAC = ((EditText) enterMacAddressDialog.findViewById(R.id.macAddressInput)).getText().toString();

            // Try to connect with the given MAC address
            //this.establishConnection();

            // Refresh the start button visibility
            this.start.setVisibility(Bluetooth.MAC != null && !Bluetooth.MAC.equals("") ? View.VISIBLE : View.GONE);
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        // Return the dialog
        return builder.create();
    }

    // Function to establish a connection with the receiver. The context provided is simply for error catching
    private void establishConnection() {

        try {
            // Try to set the receiver based on the MAC address entered
            Bluetooth.device = Bluetooth.btAdapter.getRemoteDevice(Bluetooth.MAC);

            // Well known SPP UUID
            BluetoothSocket btSocket = Bluetooth.device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

            // Make sure that discovery is off, as its fairly resource intensive
            Bluetooth.btAdapter.cancelDiscovery();

            new Bluethread(btSocket).run();

        } catch (Exception e) {
            new error_activity().new CatchError().Catch(this, e);
        }

    }

}
