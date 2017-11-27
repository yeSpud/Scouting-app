package com.example.stephenogden.a1595scoutingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter;

import java.util.Set;

/**
 * Created by Stephen Ogden on 5/29/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class bluetoothTransmiter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        BluetoothWriter writer = new BluetoothWriter(Settings.service);

        writer.writeln("Your text here");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmitter);
    }
}
