package com.example.iitsdns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.CarrierConfigManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import android.os.Message;

import com.example.iitsdns.Database.DatabaseHelperClass;
import com.example.iitsdns.EmergencyCall.EmergencyCallActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseHelperClass databaseHelperClass;

    BluetoothSocket bluetoothSocket;
    BluetoothAdapter bluetoothAdapter_1;
    InputStream INPUTSTREAM = null;
    OutputStream OUTPUTSTREAM = null;
    SendReceive sendReceive;
    long NOTI_1 = 1000;
    long NOTI_2 = 12 * 60 * 1000;
    TextView edit_1, edit_2;
    long gps_msg_time;
    long gps_total_time;
    int data_1;
    int data_2;
    int count1 = 0;
    long notification1;
    long notification2;
    long notify1After;
    long notify2After;
    byte[] read;
    int j = 0;
    int itt1 = 0, itt2 = 0;
    byte[] ex;
    byte[] first_data = new byte[2];
    byte[] second_data = new byte[2];
    int permission_msg = 0;
    int permission_locate_msg_call = 0;


    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    String position;
    int incoming_buffer_size = 60;
    int incoming_byte_size = 6;

    CardView settings;
    int ps;
    BluetoothAdapter bluetoothAdapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        databaseHelperClass = new DatabaseHelperClass(MainActivity.this);

        bluetoothAdapter_1 = BluetoothAdapter.getDefaultAdapter();

        gps_msg_time = 60 * 1000;
        gps_total_time = System.currentTimeMillis() - gps_msg_time;

        edit_1 = findViewById(R.id.edit_1);
        edit_2 = findViewById(R.id.edit_2);
        textView = findViewById(R.id.textView);

        read = new byte[6];
        ex = new byte[incoming_buffer_size];

        Notification notification = new Notification();
        notification.start();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Bluetooth_Cnt.class));

            }
        });

        if (getIntent().hasExtra("position")) {
            position = getIntent().getExtras().getString("position");
            ps = Integer.parseInt(position);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
            String[] strings = new String[bt.size()];
            final BluetoothDevice[] btArray = new BluetoothDevice[bt.size()];
            int index = 0;
            if (bt.size() > 0) {
                for (BluetoothDevice device : bt) {
                    btArray[index] = device;
                    strings[index] = device.getName();
                    index++;
                }
            }

            ClientClass clientClass = new ClientClass(btArray[ps]);
            clientClass.start();

        }

        permission_location_msg_call();

        System.out.println("----" + permission_locate_msg_call + "----");

    }


    private void permission_location_msg_call() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED

                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            permission_locate_msg_call = 1;


        } else {
//                    POP UP FOR GET THE PERMISSION..........
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE}, 44);

        }
    }

    private class Notification extends Thread {
        Timer timer = new Timer();

        public Notification() {
            notification1 = System.currentTimeMillis();
            notify1After = NOTI_1; // 10 minutes

            notification2 = System.currentTimeMillis();
            notify2After = NOTI_2; // 10 minutes

        }

        public void run() {

            System.out.println("----------Notfication---------");


            while (true) {

                if (System.currentTimeMillis() - notification1 > notify1After) {

                    LocationManager lm = (LocationManager) getSystemService(MainActivity.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {

                    }

                    /*try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch(Exception ex) {

                    }*/

                    if (!gps_enabled) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);
                        }

                        notification1 = System.currentTimeMillis();
                        System.out.println("----------Notification---------");

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "n");
                        builder.setContentText("GPS");
                        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
                        builder.setAutoCancel(true);
                        builder.setContentText("Please Enable GPS");
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                        managerCompat.notify(999, builder.build());

                    }

                }
                if (System.currentTimeMillis() - notification2 > notify2After) {


                    if (!bluetoothAdapter_1.isEnabled()) {
                        notification2 = System.currentTimeMillis();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel2 = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel2);
                        }

                        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(MainActivity.this, "n");
                        builder2.setContentText("Bluetooth");
                        builder2.setSmallIcon(R.drawable.ic_baseline_notifications_24);
                        builder2.setAutoCancel(true);
                        builder2.setContentText("Please Enable Bluetooth");
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);


                        managerCompat.notify(992, builder2.build());
                    }


                }
            }


        }

    }

    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1) {
            device = device1;
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket = socket;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {

            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case STATE_CONNECTING:
                    Toast.makeText(MainActivity.this, "Connecting", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    textView.setText("Bluetooth is connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;

                    for (int i = 0; i < readBuff.length; i++) {
                        System.out.println("read buff " + i);
                        Extract();

                        if (itt1 >= incoming_buffer_size) {
                            itt1 = 0;
                        }
                        ex[itt1] = readBuff[i];
                        itt1++;
                    }
                    /*extraction = new Extraction();
                    boolean b = extraction.receive(readBuff,readBuff.length);
                    if(b==true)
                    {
                        data_1=extraction.data_1;
                        data_2=extraction.data_2;
                        edit_1.setText(String.valueOf(data_1));
                        edit_2.setText(String.valueOf(data_2));
                    }*/
                    break;
            }
            return true;
        }
    });

    private class SendReceive extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;


        public SendReceive(BluetoothSocket socket) {
            bluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tmpIn;
            outputStream = tmpOut;
            INPUTSTREAM = tmpIn;
            OUTPUTSTREAM = tmpOut;

        }

        public void run() {
            while (true) {
                byte[] buffer = new byte[100];
                int bytes;

                try {
                    bytes = inputStream.read(buffer);
                    byte[] k = new byte[bytes];
                    for (int i = 0; i < bytes; i++) {
                        k[i] = buffer[i];

                    }

                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, k).sendToTarget();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this, Settings.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(MainActivity.this, About_App.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void Extract() {

        if (itt2 >= incoming_buffer_size)
            itt2 = 0;

        if (itt2 + incoming_byte_size <= itt1) {
            if (ex[itt2] == 11 && ex[itt2 + 5] == 22) {

                first_data[0] = ex[itt2 + 1];
                first_data[1] = ex[itt2 + 2];

                data_1 = fromByteArraytoInt2(first_data);

                first_data[0] = ex[itt2 + 3];
                first_data[1] = ex[itt2 + 4];

                data_2 = fromByteArraytoInt2(first_data);

                System.out.println("First data " + data_1);
                System.out.println("Second data " + data_2);

                edit_1.setText(String.valueOf(data_2));
                edit_2.setText(String.valueOf(data_1));

// al mamun working here..........
                if (data_1 < databaseHelperClass.readSPO2AndHeartRate().getSPO2() || data_2 < databaseHelperClass.readSPO2AndHeartRate().getHeartBeat())
                {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                            && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED

                            && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        getMyLocation(data_1, data_2);
                    } else {
                        permission_location_msg_call();
                    }


                }


                itt2 = itt2 + incoming_byte_size;

            } else {
                itt2++;
            }

        }
    }


    public int fromByteArraytoInt2(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 8) |
                ((bytes[1] & 0xFF) << 0);
    }


    //    getlocation here..........

    @SuppressLint("MissingPermission")
    private void getMyLocation(int a, int b) {
        if (System.currentTimeMillis() - gps_total_time > gps_msg_time) {

            gps_total_time = System.currentTimeMillis();

//        DatabaseHelperClass databaseHelperClass = new DatabaseHelperClass(MainActivity.this);
            LocationManager locationManager = (LocationManager) getSystemService(MainActivity.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {



                            String number1 = databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();

                            String number2 = databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();

                            if(number1.length()>0||number2.length()>0)
                            {
                                String message = "SpO2 : " + String.valueOf(a) + "\nHeart Rate : " + String.valueOf(b) + "\n";
                                // String message1 = "http://google.com/maps/bylatlng?lat=" + String.valueOf(location.getLatitude()) + "&lng=" + String.valueOf(location.getLongitude());
                                String message2 = "\nLatitude : " + String.valueOf(location.getLatitude()) + "\nLongtitude : " + String.valueOf(location.getLongitude());

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(number1, null, message + message2, null, null);
                                smsManager.sendTextMessage(number2, null, message + message2, null, null);

                                String call = "tel:" + number1;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)));
                            }





                        } else {
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
//                                again send location
                                    Location location1 = locationResult.getLastLocation();
                                    //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


                                    String number1 = databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();

                                    String number2 = databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();

                                    if(number1.length()>0||number2.length()>0)
                                    {
                                        String message1 = "SpO2 : " + String.valueOf(a) + "\nHeart Rate : " + String.valueOf(b) + "\n";
                                        // String message = "http://google.com/maps/bylatlng?lat=" + String.valueOf(location1.getLatitude()) + "&lng=" + String.valueOf(location1.getLongitude());
                                        String message2 = "\nLatitude : " + String.valueOf(location1.getLatitude()) + "\nLongtitude : " + String.valueOf(location1.getLongitude());

                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(number1, null, message1 + message2, null, null);
                                        smsManager.sendTextMessage(number2, null, message1 + message2, null, null);

                                        String call = "tel:" + number1;
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)));

                                    }




                                    super.onLocationResult(locationResult);
                                }
                            };
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        }

                    }
                });
            } else {
//            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }

    }


    // on activity result here..........
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 44 && grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            Toast.makeText(MainActivity.this, "Permission Granted.....", Toast.LENGTH_SHORT).show();
            permission_locate_msg_call = 1;


        } else {
            Toast.makeText(MainActivity.this, "Permission Denied....", Toast.LENGTH_SHORT).show();

        }

    }
}
