package org.dragons.Dragons.scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Stephen Ogden on 6/24/18.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class error_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_page);
        findViewById(R.id.retrun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(error_activity.this, main_activity.class));
            }
        });
    }
}
