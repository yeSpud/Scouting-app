package com.example.stephenogden.a1595scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by Stephen Ogden on 3/23/17.
 */

public class MainActivity extends AppCompatActivity {

    // <Keep>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // </Keep>

        Button settings_btn = (Button)findViewById(R.id.settings);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });

        Button start_btn = (Button)findViewById(R.id.start);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, scouting.class));
            }
        });


    }
}
