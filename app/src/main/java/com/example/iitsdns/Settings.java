package com.example.iitsdns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iitsdns.EmergencyCall.EmergencyCallActivity;
import com.example.iitsdns.GPS.GPSActivity;

public class Settings extends AppCompatActivity {

    CardView blue,gps,e_number,e_call,e_msg,set_limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        blue=findViewById(R.id.blue);
        gps=findViewById(R.id.gps);
        e_number=findViewById(R.id.e_number);
        e_call=findViewById(R.id.e_call);
        e_msg=findViewById(R.id.e_m);
        set_limit=findViewById(R.id.set_limit);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Bluetooth_Cnt.class));

            }
        });

        set_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, set_limit.class));

            }
        });
        e_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, emergency_number.class));

            }
        });


        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, GPSActivity.class));

            }
        });
        e_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, EmergencyCallActivity.class));
            }
        });



    }
}