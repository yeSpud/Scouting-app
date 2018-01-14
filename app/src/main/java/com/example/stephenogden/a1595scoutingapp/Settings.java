package com.example.stephenogden.a1595scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Stephen Ogden on 5/27/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Settings extends AppCompatActivity {

    public static String MACADDR;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button back_btn = (Button) findViewById(R.id.back);
        EditText macAddr = (EditText) findViewById(R.id.MAC);
        macAddr.setText(MACADDR);

        macAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MACADDR = s.toString();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });
    }
}
