package com.example.stephenogden.a1595scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Stephen Ogden on 5/27/17.
 */

public class scouting extends AppCompatActivity {

    // <Keep>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scouting);
        // </Keep>

        Button back_btn = (Button) findViewById(R.id.cancel);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(scouting.this, MainActivity.class));
                Toast.makeText(scouting.this, "Scouting canceled.",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button send_btn = (Button) findViewById(R.id.submit);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(scouting.this, bluetoothTransmiter.class));
            }
        });
    }

}
