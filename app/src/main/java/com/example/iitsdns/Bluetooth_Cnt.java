package com.example.iitsdns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class Bluetooth_Cnt extends AppCompatActivity {


    LinearLayout button_on,button_off,button_settings;
    BluetoothAdapter bluetoothAdapter;
    ListView list_view;
    int RQ=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth__cnt);

        button_on=findViewById(R.id.on);
        button_off=findViewById(R.id.off);
        button_settings=findViewById(R.id.settings);

        list_view=findViewById(R.id.list_view);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter!=null)
            adapter();

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));

            }
        });

        button_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter==null)
                {
                    Toast.makeText(Bluetooth_Cnt.this, "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!bluetoothAdapter.isEnabled())
                    {
                        Intent blueToothIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(blueToothIntent,RQ);


                    }
                    else
                    {
                        Toast.makeText(Bluetooth_Cnt.this, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        button_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled())
                {
                    bluetoothAdapter.disable();
                    Toast.makeText(Bluetooth_Cnt.this, "Bluetooth is disabled", Toast.LENGTH_SHORT).show();
                    list_view.setAdapter(null);

                }
                else {
                    Toast.makeText(Bluetooth_Cnt.this, "Bluetooth is already disable", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==RQ)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(Bluetooth_Cnt.this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                adapter();
            }
            else if(requestCode==RESULT_CANCELED)
            {
                Toast.makeText(Bluetooth_Cnt.this, "Bluetooth enabling is cancelled", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void adapter() {
        System.out.println("hello");
        Set<BluetoothDevice> devices= bluetoothAdapter.getBondedDevices();
        String[] str_device=new String[devices.size()];
        int i=0;
        if(devices.size()>0)
        {
            for(BluetoothDevice device:devices)
            {
                str_device[i]=device.getName();
                i++;
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,str_device);
            list_view.setAdapter(arrayAdapter);
        }
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Bluetooth_Cnt.this, MainActivity.class);
                intent.putExtra("position",Integer.toString(position));
                startActivity(intent);
                finish();
            }
        });
    }
}