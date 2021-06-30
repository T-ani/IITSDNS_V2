package com.example.iitsdns.EmergencyCall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iitsdns.Database.DatabaseHelperClass;
import com.example.iitsdns.R;

public class EmergencyCallActivity extends AppCompatActivity {
    Button btnEmergencyCall;
    ImageView button_1,button_2;
    DatabaseHelperClass databaseHelperClass;
    TextView phone_number_1,phone_number_2;
    String read_phone_1,read_phone_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);


        button_1=findViewById(R.id.btn_emergency_call_1);
        button_2=findViewById(R.id.btn_emergency_call_2);

        phone_number_1=findViewById(R.id.phone_number_1);
        phone_number_2=findViewById(R.id.phone_number_2);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        databaseHelperClass = new DatabaseHelperClass(this);

        if( databaseHelperClass.readPhoneNumberModelClass()!=null){

            read_phone_1=databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();
            //read_phone_1=read_phone_1.replaceAll("'","");

            read_phone_2=databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber2();
            //read_phone_2=read_phone_2.replaceAll("'","");
            System.out.println(read_phone_1+"-----------------");

            phone_number_1.setText(read_phone_1);
            phone_number_2.setText(read_phone_2);
        }

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingFunction();

            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingFunction2();
            }
        });

    }

    //all calling functionality here..........

    private void CallingFunction() {
        DatabaseHelperClass databaseHelperClass = new DatabaseHelperClass(this);
        String number = databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();
       // number=number.replaceAll("Phone","");

        if(number.length()>0){
            if(ContextCompat.checkSelfPermission(EmergencyCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EmergencyCallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }else{
                String call = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)));
            }

        } else{
            Toast.makeText(this, "You have to input phone number first", Toast.LENGTH_SHORT).show();
        }
    }
    private void CallingFunction2() {
        DatabaseHelperClass databaseHelperClass = new DatabaseHelperClass(this);
        String number = databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber2();
        number=number.replaceAll("Phone","");
        if(number.length()>0){
            if(ContextCompat.checkSelfPermission(EmergencyCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EmergencyCallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }else{
                String call = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)));
            }

        } else{
            Toast.makeText(this, "You have to input phone number first", Toast.LENGTH_SHORT).show();
        }
    }

    //permission result function here.........


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallingFunction();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}