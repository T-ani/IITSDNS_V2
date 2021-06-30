package com.example.iitsdns.GPS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.iitsdns.MainActivity;
import com.example.iitsdns.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GPSActivity extends AppCompatActivity {
    Button btnGPS;
    PackageManager packageManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnGPS = findViewById(R.id.btn_enable_gps);
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(GPSActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(GPSActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    getMyLocation();

                } else {
//                    POP UP FOR GET THE PERMISSION..........
                    ActivityCompat.requestPermissions(GPSActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44 && grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            getLocation();
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("Mamun", "Problem occured");
            Toast.makeText(GPSActivity.this, "didn't get any response.....", Toast.LENGTH_SHORT).show();
        }
    }




//
//    @SuppressLint("MissingPermission")
//    private void getMyLocation() {
//        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
//
//
//
//
//
//
//
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    Location location = task.getResult();
//                    if (location != null) {
////
////                        try {
////                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
////                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getAltitude(), 1);
////
////                            textView1.setText(String.valueOf(addresses.get(0).getLatitude()));
////                            textView2.setText(String.valueOf(addresses.get(0).getLongitude()));
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//
//                    } else {
//                        LocationRequest locationRequest = new LocationRequest()
//                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                                .setInterval(10000)
//                                .setFastestInterval(1000)
//                                .setNumUpdates(1);
//                        LocationCallback locationCallback = new LocationCallback() {
//                            @Override
//                            public void onLocationResult(@NonNull LocationResult locationResult) {
//                                Location location1 = locationResult.getLastLocation();
//
//                                super.onLocationResult(locationResult);
//                            }
//                        };
////                        Request location updates.........
//                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//
//                    }
//
//                }
//            });
//        } else {
////            startActivity(new Intent(GPSActivity.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
//
//    }
}