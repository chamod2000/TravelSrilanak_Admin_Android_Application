package com.jiat.travelsrilanka;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jiat.travelsrilanka.model.Place;

public class cultureSingleView extends AppCompatActivity {


    private static final String TAG = "SingleView";
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ImageButton imageButton;
    public static Uri imagePath;
    Place place;
    public static String id;
    public double clati;
    public double clogi;
    public String img;
    public static String publicPlacename;
    public static String publicLongitudeC;
    public static String publicLatitudeC;
    public static String cloca;
    public static String clName;
    public static String plocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_single_view);

        ImageView backimg = findViewById(R.id.imageViewback);
        Glide.with(getApplicationContext()).load(R.drawable.previous).override(35,35).into(backimg);
        findViewById(R.id.imageViewback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cultureSingleView.this,cultureActivity.class);
                startActivity(intent);
            }
        });



        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        clName = intent.getStringExtra("locationName");
        cloca = clName;

        Log.i(TAG,clName);

        place =new Place();
        imageButton = findViewById(R.id.imageViewC);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intentActivityResultLauncher.launch(intent);
            }
        });

        TextView placeName = findViewById(R.id.editTextTextPlaceNameC);
        TextView placeDescription = findViewById(R.id.textViewdescriptionC);
        TextView placeLocationAdd = findViewById(R.id.editTextTextplaceLocationC);
        TextView placeContactNu = findViewById(R.id.editTextTextContactNumberC);
        TextView placeOpenTime= findViewById(R.id.editTextTextOpenCloseC);
        TextView placeWebsite= findViewById(R.id.editTextTextWebsitenameC);
//        TextView pLatitude= findViewById(R.id.latitudeC);
//        TextView pLongitute= findViewById(R.id.longitudeC);



        firestore.collection("Place/Culture/dCulture")
                .whereEqualTo("pname",clName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    place = snapshot.toObject(Place.class);


                    id = snapshot.getId();


                    clati =place.getLatitude();
                    String latitu = String.valueOf(clati);
//                    pLatitude.setText(latitu);

                    clogi =place.getLongitude();
                    String longitu = String.valueOf(clogi);
//                    pLongitute.setText(longitu);


                    placeName.setText(place.getPname());
                    placeDescription.setText(place.getDetails());
                    placeLocationAdd.setText(place.getPlaceLocation());

                    plocation = place.getPlaceLocation();
                    Log.i(TAG,clName);

                    placeContactNu.setText(place.getTell());
                    placeOpenTime.setText(place.getOpenClose());
                    placeWebsite.setText(place.getWebsite());
                    publicPlacename=placeName.getText().toString();
                    publicLongitudeC=longitu;
                    publicLatitudeC=latitu;
                    img = place.getImage();
                    Log.i(TAG,img);
                    tookimg(img);




                }
            }
        });


        findViewById(R.id.btnUpdateC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //firestore.collection("Place/Wildlife/dWildlife")

                TextView placeName = findViewById(R.id.editTextTextPlaceNameC);
                TextView placeDescription = findViewById(R.id.textViewdescriptionC);
                TextView placeLocationAdd = findViewById(R.id.editTextTextplaceLocationC);
                TextView placeContactNu = findViewById(R.id.editTextTextContactNumberC);
                TextView placeOpenTime= findViewById(R.id.editTextTextOpenCloseC);
                TextView placeWebsite= findViewById(R.id.editTextTextWebsitenameC);

                String pName = placeName.getText().toString();
                String pDescription = placeDescription.getText().toString();
                String pPlaceLocation = placeLocationAdd.getText().toString();
                String pContact = placeContactNu.getText().toString();
                String pOpentime = placeOpenTime.getText().toString();
                String pWebsite = placeWebsite.getText().toString();
                Log.i(TAG,"New sleceted ID : "+String.valueOf(imagePath));

                double longi = place.getLongitude();
                double latitute = place.getLongitude();

                if (pDescription.isEmpty()){
                    Toast.makeText(cultureSingleView.this,"Place Description input field is empty",Toast.LENGTH_SHORT).show();
                }else if (pPlaceLocation.isEmpty()){
                    Toast.makeText(cultureSingleView.this,"Place Location input field is empty",Toast.LENGTH_SHORT).show();
                }else if (pContact.isEmpty()){
                    Toast.makeText(cultureSingleView.this,"Place Contact input field is empty",Toast.LENGTH_SHORT).show();
                }else if (pContact.length() >10){
                    Toast.makeText(cultureSingleView.this,"Contact Number More than 10 Digits",Toast.LENGTH_SHORT).show();
                }else if (pContact.length() <10){
                    Toast.makeText(cultureSingleView.this,"Contact Number Less than 10 Digits",Toast.LENGTH_SHORT).show();
                }else if (pWebsite.isEmpty()){
                    Toast.makeText(cultureSingleView.this,"Place Website input field is empty",Toast.LENGTH_SHORT).show();
                }else {


                    firestore.collection("Place/Culture/dCulture").whereEqualTo("pname", clName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                DocumentReference documentReference = snapshot.getReference();

                                documentReference.update("details", pDescription);
                                documentReference.update("openClose", pOpentime);
                                documentReference.update("tell", pContact);
                                documentReference.update("website", pWebsite);
                                documentReference.update("placeLocation", pPlaceLocation);
                                updateImage();

                                Toast.makeText(cultureSingleView.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
//                              Intent intent1 = new Intent(SinglePlaceViewActivity.this,SinglePlaceViewActivity.class);
//                               startActivity(intent1);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, e.getMessage());
                        }
                    });
                }

            }
        });





        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(cultureSingleView.this,mapView.class);
                intent1.putExtra("location",cloca);
                startActivity(intent1);
            }
        });


        findViewById(R.id.btnDeletet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Place/Culture/dCulture").whereEqualTo("pname", clName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            DocumentReference documentReference = snapshot.getReference();
                            documentReference.delete();
                            Toast.makeText(cultureSingleView.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        firestore.collection("Place/Culture/dCulture").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange change : value.getDocumentChanges()){

                    switch (change.getType()){
                        case ADDED:

                            break;
                        case MODIFIED:

                            Toast.makeText(cultureSingleView.this,"Location Data Modified",Toast.LENGTH_SHORT).show();
                            QueryDocumentSnapshot document = change.getDocument();
                            Place place = document.toObject(Place.class);
                            String details = place.getDetails();
                            String img = place.getImage();
                            String pLocatio = place.getPlaceLocation();
                            String openClose = place.getOpenClose();
                            String web = place.getWebsite();
                            String tell = place.getTell();

                            placeDescription.setText(details);
                            placeLocationAdd.setText(pLocatio);
                            placeContactNu.setText(tell);
                            placeOpenTime.setText(openClose);
                            placeWebsite.setText(web);
                            break;
                        case REMOVED:
                            Toast.makeText(cultureSingleView.this,"Location Data Deleted",Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(cultureSingleView.this,testAcitivity.class);
                            startActivity(intent1);
                            finish();
                            break;
                    }
                }


            }
        });

    }

    //new way
    ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== Activity.RESULT_OK){
                        imagePath = result.getData().getData();
                        Log.i(TAG,imagePath.getPath());
                        Glide.with(cultureSingleView.this).load(imagePath).into(imageButton);

                    }
                }
            }
    );

    public void updateImage(){
        if (imagePath != null){
            StorageReference reference = storage.getReference("location-images").child(img);
            reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(cultureSingleView.this,"Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                     Log.i(TAG,e.getMessage());
                }
            });


        }
  }


    public void tookimg(String img){


        storage.getReference("location-images/"+img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i(TAG, uri.getPath());
                ImageButton image = findViewById(R.id.imageViewC);
                Glide.with(getApplicationContext()).load(uri).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getMessage());
            }
        });
    }


}