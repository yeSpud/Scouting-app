package com.example.stephenogden.a1595scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Stephen Ogden on 5/27/17.
 */

// TODO: See below (34 - 45)

public class Settings extends AppCompatActivity {

    // <Keep>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        // </Keep>

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
