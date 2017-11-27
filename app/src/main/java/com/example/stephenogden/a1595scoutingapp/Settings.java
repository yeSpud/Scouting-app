package com.example.stephenogden.a1595scoutingapp;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothClassicService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;

import java.util.UUID;

/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Settings extends AppCompatActivity {

    public static BluetoothService service;

    protected void onCreate(Bundle savedInstanceState) {

        BluetoothConfiguration config = new BluetoothConfiguration();
        config.context = getApplicationContext();
        config.bluetoothServiceClass = BluetoothClassicService.class; // BluetoothClassicService.class or BluetoothLeService.class
        config.bufferSize = 1024;
        config.characterDelimiter = '\n';
        config.deviceName = "Your App Name";
        config.callListenersInMainThread = true;

        config.uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        BluetoothService.init(config);

        BluetoothService service = BluetoothService.getDefaultInstance();

        service.setOnScanCallback(new BluetoothService.OnBluetoothScanCallback() {
            @Override
            public void onDeviceDiscovered(BluetoothDevice device, int rssi) {
                // List devices
            }

            @Override
            public void onStartScan() {
                // Update text
            }

            @Override
            public void onStopScan() {
                // Update text
            }
        });

        service.startScan(); // See also service.stopScan();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button back_btn = (Button) findViewById(R.id.back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });

        // TODO: Fix phone string variable and ID

        // get EditText by id
        EditText inputTxt;
        inputTxt = (EditText) findViewById(R.id.phoneID);

        // Store EditText in Variable
        String phoneIDString = inputTxt.getText().toString();

        // Display new phone ID
        String updating_title = getString(R.string.phoneID);
        inputTxt.setText(updating_title);

    }



    }
