package com.jiat.travelsrilanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.travelsrilanka.model.Place;

public class mapView extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1312;
    private static final String TAG = "Map";
    private GoogleMap map;
    private Marker mymarker;
    private String placename=SinglePlaceViewActivity.publicPlacename;
    private String longitude;
    private String latitude;
    private FirebaseFirestore firestore;
    private Marker shopMarker;
    String imf;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Intent intent = getIntent();
        String lName = intent.getStringExtra("locationName");

        Intent intent2 = getIntent();
        String lName2 = intent2.getStringExtra("location");


        if (lName != null && lName2 == null){
            path = "Place/Wildlife/dWildlife";
            imf = SinglePlaceViewActivity.loca;
            longitude =SinglePlaceViewActivity.publicLongitude;
            latitude = SinglePlaceViewActivity.publicLatitude;
            Log.i(TAG,"Location  WildLife :"+longitude+latitude);
        }else if (lName2 != null && lName == null){
            path = "Place/Culture/dCulture";
            imf = cultureSingleView.cloca;
            longitude =cultureSingleView.publicLongitudeC;
            latitude = cultureSingleView.publicLatitudeC;

            Log.i(TAG,"Location  Culture :"+longitude+latitude);
        }


        firestore=FirebaseFirestore.getInstance();

        // SinglePlaceViewActivity ac = new SinglePlaceViewActivity();
//        imf = SinglePlaceViewActivity.loca;
//        Log.i(TAG,imf);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.i(TAG,latitude+"longi::"+longitude);

        firestore.collection(path).whereEqualTo("pname",imf)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){

                            Log.i(TAG,"Location ID  :");

                            Place location = snapshot.toObject(Place.class);
                            LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                            MarkerOptions markplace = new MarkerOptions();
                            markplace.icon(BitmapDescriptorFactory.fromResource(R.drawable.flagk));
                            Log.i(TAG,"Donnneee line number 71");
                            markplace.title(location.getPname());
                            markplace.position(latLng);
                            shopMarker=map.addMarker(markplace);

                            CameraPosition position = CameraPosition.builder()
                                    .target(latLng)
                                    .zoom(15f)
                                    .build();

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                            map.animateCamera(cameraUpdate);



                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.i(TAG,"msgggggg:  "+e.getMessage());
            }
        });


    }



    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        map = googleMap;
        // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (checkPermissions()){
            map.setMyLocationEnabled(true);
        }else {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
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

            if (permissionsGranted){
                map.setMyLocationEnabled(true);
            }
        }
    }
    //checking whether the requested permission were allowed ->END


}