package com.jiat.travelsrilanka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class testAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_acitivity);

        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



//        ImageView backimg = findViewById(R.id.imageView5);
//        Glide.with(getApplicationContext()).load(R.drawable.previous).override(35,35).into(backimg);
//        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(testAcitivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });


        findViewById(R.id.selectSearchItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(testAcitivity.this,PlacesList.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.selectSearchItem2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(testAcitivity.this,cultureActivity.class);
                startActivity(intent);
            }
        });

//        findViewById(R.id.textViewCulture).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(testAcitivity.this,Notworking.class);
//                startActivity(intent);
//            }
//        });

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


}