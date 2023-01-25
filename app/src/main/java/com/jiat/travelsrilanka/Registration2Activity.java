package com.jiat.travelsrilanka;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jiat.travelsrilanka.model.Place;

import java.util.UUID;

public class Registration2Activity extends AppCompatActivity {

    private static final String TAG ="R2" ;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    public String pLocationType;
    Uri imageLocation;
    Place place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        ImageView backimg = findViewById(R.id.imageView7);
        Glide.with(getApplicationContext()).load(R.drawable.previous).override(35,35).into(backimg);
        findViewById(R.id.imageView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration2Activity.this,LocationRegisrActivity.class);
                startActivity(intent);
            }
        });

        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        Intent intent = getIntent();
        String pName = intent.getStringExtra("name");
        String pDescription = intent.getStringExtra("description");

        String pl = intent.getStringExtra("latitude");
        double pLatitude = Double.parseDouble(pl);

        String pLon = intent.getStringExtra("longitude");
        double pLongitude = Double.parseDouble(pLon);

        imageLocation = LocationRegisrActivity.imagePath;

        Log.i(TAG,pName+pDescription+ pLatitude+pLongitude+imageLocation+pLocationType);



         pLocationType = intent.getStringExtra("typeLo");

        switch (pLocationType){
            case "Culture":
                pLocationType = "/Culture/dCulture/";
                break;
            case "Heritage":
                pLocationType = "/Heritage/dHeritage/";
                break;
            case "Wildlife":
                pLocationType = "/Wildlife/dWildlife/";
                break;
        }

        Log.i(TAG,pName+pDescription+ pLatitude+pLongitude+imageLocation+pLocationType);

             //add new record
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText editTextTextLocatioName = findViewById(R.id.editTextTextLocatioName);
                EditText editTextContactNumber = findViewById(R.id.editTextContactNumber);
                EditText editTextTextOpenTime = findViewById(R.id.editTextTextOpenTime);
                EditText editTextTextWebsite = findViewById(R.id.editTextTextWebsite);

                String pLocationName = editTextTextLocatioName.getText().toString();
                String pContactNu = editTextContactNumber.getText().toString();
                String pOpen = editTextTextOpenTime.getText().toString();
                String pWeb = editTextTextWebsite.getText().toString();

                if (pLocationName.isEmpty()){
                    Toast.makeText(Registration2Activity.this,"Location Name Not Entered",Toast.LENGTH_SHORT).show();
                }else if (pContactNu.isEmpty()){
                    Toast.makeText(Registration2Activity.this,"Contact Number Not Entered",Toast.LENGTH_SHORT).show();
                }else if (pContactNu.length() >10){
                    Toast.makeText(Registration2Activity.this,"Contact Number More than 10 Digits ",Toast.LENGTH_SHORT).show();
                }else if (pContactNu.length() <10) {
                    Toast.makeText(Registration2Activity.this,"Contact Number Less than 10 Digits",Toast.LENGTH_SHORT).show();
                }else if (pOpen.isEmpty()){
                    Toast.makeText(Registration2Activity.this,"Open and Close Time Not Selected",Toast.LENGTH_SHORT).show();
                }else if (pWeb.isEmpty() ){
                    Toast.makeText(Registration2Activity.this,"Email Address Not Selected",Toast.LENGTH_SHORT).show();
                }else {


                    ProgressDialog dialog = new ProgressDialog(Registration2Activity.this);
                    dialog.setMessage("Add New place...");
                    dialog.setCancelable(false);
                    dialog.show();

                    final String imageId = UUID.randomUUID().toString();

                     place = new Place(pName, pDescription, pLatitude, pLongitude, imageId, pLocationName, pContactNu, pOpen, pWeb);


                    firestore.collection("Place" + pLocationType).add(place).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            if (imageLocation != null) {


                                dialog.setMessage("Uploading Data..");

                                StorageReference reference = storage.getReference("location-images").child(imageId);
                                reference.putFile(imageLocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        dialog.dismiss();
                                        Intent intent1 = new Intent(Registration2Activity.this, MainActivity.class);
                                      startActivity(intent1);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(Registration2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                               });
//                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                        dialog.setMessage("Uploading" + (int) progress + "%");
//
////                                        Intent intent1 = new Intent(Registration2Activity.this, MainActivity.class);
////                                        startActivity(intent1);
//                                    }
//                                });
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




//        firestore.collection("Place" + pLocationType).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for(DocumentChange change : value.getDocumentChanges()){
//
//                    switch (change.getType()){
//                        case ADDED:
//                            EditText editTextTextLocatioName = findViewById(R.id.editTextTextLocatioName);
//                            EditText editTextContactNumber = findViewById(R.id.editTextContactNumber);
//                            EditText editTextTextOpenTime = findViewById(R.id.editTextTextOpenTime);
//                            EditText editTextTextWebsite = findViewById(R.id.editTextTextWebsite);
//
//                            editTextTextLocatioName.setText("");
//                            editTextContactNumber.setText("");
//                            editTextTextOpenTime.setText("");
//                            editTextTextWebsite.setText("");
//
//                            Toast.makeText(Registration2Activity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent1 = new Intent(Registration2Activity.this, MainActivity.class);
//                            startActivity(intent1);
//                            break;
//                        case MODIFIED:
//                            break;
//                        case REMOVED:
//                            break;
//                    }
//                }
//
//
//            }
//        });





    }




}