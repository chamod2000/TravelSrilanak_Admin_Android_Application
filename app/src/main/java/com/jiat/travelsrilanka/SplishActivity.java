package com.jiat.travelsrilanka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);

        setTheme(R.style.Theme_TravelSrilanka_Splash);

       ImageView imageView = findViewById(R.id.splash);
        Glide.with(this).load(R.drawable.logo).override(350,350).into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            }
        },5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                Intent intent = new Intent(SplishActivity.this,loginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}