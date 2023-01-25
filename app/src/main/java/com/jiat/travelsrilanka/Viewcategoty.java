package com.jiat.travelsrilanka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Viewcategoty extends AppCompatActivity {


    FirebaseFirestore firestore;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcategoty);

        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        Glide.with(this).load(R.drawable.imgup).into((ImageView) findViewById(R.id.imageButtonLocation));





    }
}