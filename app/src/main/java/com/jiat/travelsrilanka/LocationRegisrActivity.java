package com.jiat.travelsrilanka;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationRegisrActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int IMAGE_REQUEST = 20;
    private static final String TAG = "locationRegister";
    ImageButton imageButton ;
    public static Uri imagePath;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1312;

    private GoogleMap map;
    private String latitudeLocation;
    private  String longitudeLocation;



    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_regisr);

        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ImageView backimg = findViewById(R.id.imageView4);
//        Glide.with(getApplicationContext()).load(R.drawable.previous).override(35,35).into(backimg);
//        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LocationRegisrActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });


        findViewById(R.id.getLocationBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onMapReady(map);
                Toast.makeText(LocationRegisrActivity.this,"Location Recieved",Toast.LENGTH_SHORT).show();
            }
        });


        Glide.with(this).load(R.drawable.pj).into((ImageView) findViewById(R.id.imageButtonLocation));

        Spinner spinner = findViewById(R.id.locCombo);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select type of place");
        arrayList.add("Wildlife");
        arrayList.add("Culture");
//        arrayList.add("Heritage");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                string tutorialsName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView <?> parent) {
//            }
//        });

        imageButton = findViewById(R.id.imageButtonLocation);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                intentActivityResultLauncher.launch(intent);
            }
        });




        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTextTextLName = findViewById(R.id.editTextTextLName);
                EditText editTextTextDescription = findViewById(R.id.editTextTextDescription);
//                EditText editTextTextLatitude = findViewById(R.id.editTextTextLatitude);
//                EditText editTextTextLongitude = findViewById(R.id.editTextTextLongitude);

                 String pName = editTextTextLName.getText().toString();
                String pDescription = editTextTextDescription.getText().toString();
//                String pLatitude = editTextTextLatitude.getText().toString();
//                String pLongitude = editTextTextLongitude.getText().toString();
                String locationType = spinner.getSelectedItem().toString();

               // Log.i(TAG,"Data parse:"+pName+pDescription+pLatitude+pLongitude+locationType+imagePath);

                if (pName.isEmpty()){
                    Toast.makeText(LocationRegisrActivity.this,"Name Not Entered",Toast.LENGTH_SHORT).show();
                }else if (pDescription.isEmpty()){
                    Toast.makeText(LocationRegisrActivity.this,"Description Not Entered",Toast.LENGTH_SHORT).show();
                }else if (locationType== "Select type of place"){
                    Toast.makeText(LocationRegisrActivity.this,"Location Category Not Selected",Toast.LENGTH_SHORT).show();
                }else if (imagePath == null){
                    Toast.makeText(LocationRegisrActivity.this,"Image Not Selected",Toast.LENGTH_SHORT).show();
                }else {

                    //Code to start an activity
                    Intent intent = new Intent(LocationRegisrActivity.this, Registration2Activity.class);
                    intent.putExtra("name", pName);
                    intent.putExtra("description", pDescription);
                    intent.putExtra("latitude", latitudeLocation);
                    intent.putExtra("longitude", longitudeLocation);
                    intent.putExtra("typeLo", locationType);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //new way
    ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== Activity.RESULT_OK){
                        imagePath = result.getData().getData();
                        Log.i(TAG,imagePath.getPath());
                        Glide.with(LocationRegisrActivity.this).load(imagePath).into(imageButton);

                    }
                }
            }
    );



    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (checkPermissions()){
//            map.setMyLocationEnabled(true);
        }else {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
        //taking the current location->START
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(60000)
                .setFastestInterval(60000)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) { super.onLocationAvailability(locationAvailability); }

                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        Location lastLocation = locationResult.getLastLocation();
                        latitudeLocation=String.valueOf(lastLocation.getLatitude());
                        longitudeLocation=String.valueOf(lastLocation.getLongitude());
                        Log.i(TAG,latitudeLocation);
                        Log.i(TAG,longitudeLocation);
                    }
                }, Looper.getMainLooper());
        //taking the current location->END

    }




    //Checking whether the permissions are allowed ->START

    @SuppressLint("NewApi")
    public boolean checkPermissions(){
        boolean permissions = false;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            permissions = true;
        }
        return permissions;
    }
//Checking whether the permissions are allowed ->END



//checking whether the requested permission were allowed ->START

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionsGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE){

            for (int i = 0; i < permissions.length; i++){

                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    permissionsGranted = true;

                }else if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    permissionsGranted = true;
                }
            }

        }
    }
    //checking whether the requested permission were allowed ->END


    }





