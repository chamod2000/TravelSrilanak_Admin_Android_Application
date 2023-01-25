package com.jiat.travelsrilanka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView imageView = findViewById(R.id.homepageLogo);
        Glide.with(this).load(R.drawable.hme).override(350,700).into(imageView);

        ImageView imageView2 = findViewById(R.id.logout);
        Glide.with(this).load(R.drawable.logou).override(50,50).into(imageView2);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });

        //Travel Btn Config
        findViewById(R.id.btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LocationRegisrActivity.class);
                startActivity(intent);
            }
        });



        findViewById(R.id.btnView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,testAcitivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}