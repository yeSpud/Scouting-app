package org.dragons.Dragons.scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static org.dragons.Dragons.scoutingapp.Core.isSetInChinese;

/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class settings extends AppCompatActivity {

    // We will need a string to store the macaddress
    @SuppressWarnings("SpellCheckingInspection")
    public static String MACADDR;

    // In order to get the text entered, we need to get data from the EditText element
    // We'll define what exactly this is when the activity is created
    @SuppressWarnings("SpellCheckingInspection")
    EditText macAddr;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Get the back button in order to return to main_activity.java
        Button back_btn = findViewById(R.id.back);
        if (isSetInChinese()) {
            back_btn.setText(R.string.backCN);
            ((TextView) findViewById(R.id.settings_header)).setText(R.string.settingsCN);
            ((TextView) findViewById(R.id.warning)).setText(R.string.warningCN);
            ((TextView) findViewById(R.id.macAddrHeader)).setText(R.string.macAddrCN);
            setTitle(R.string.app_name_CN);
        }

        // Ill be honest: Im lazy and don't like entering the same thing over and over, so I created a button that auto-enters the MAC of my laptop to speed things up
        Button auto = findViewById(R.id.autoEnter);

        // Here is where we define that editable text from earlier
        // If the MAC address was entered before however, we will want to set the text to that entered prior, for continuity reasons
        macAddr = findViewById(R.id.MAC);
        macAddr.setText(MACADDR);

        // In order to set the string for the MAC address as the text entered, well need to add a listener to the element
        macAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // This is when the string gets set to whatever is entered
                MACADDR = s.toString();
            }
        });

        // Remember that lazy button I added earlier? Lets create that action for when its pressed.
        // What it will do is auto compete the MAC address to what is entered below.
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MAC address of my laptop
                MACADDR = "AC:BC:32:8E:CC:1A";

                // This is what sets the text to the MAC address set above
                macAddr.setText(MACADDR);
            }
        });

        // In order to return to the previous activity when the back button is pressed, we need to create a listener for the button
        // So, when the back button is pressed, it will return to main_activity.java
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, main_activity.class));
            }
        });
    }
}
