package com.jiat.travelsrilanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {

    private static final String TAG = "registerActivity";
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ImageView imageView = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.regi).override(300,300).into(imageView);

        findViewById(R.id.noAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });

        firebaseauth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.emailRegisterTextField);
        EditText pswd = findViewById(R.id.passwordRegisterPasswordField);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String pswdText = pswd.getText().toString();


                if (emailText.isEmpty()){
                    Toast.makeText(registerActivity.this,"Email Not Entered",Toast.LENGTH_SHORT).show();
                }else if (pswdText.isEmpty()){
                    Toast.makeText(registerActivity.this,"Password Not Entered",Toast.LENGTH_SHORT).show();
                }else {

                firebaseauth.createUserWithEmailAndPassword(emailText, pswdText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = firebaseauth.getCurrentUser();
                                    SignUp(user);
                                } else {
                                    Log.d(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(registerActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    SignUp(null);

                                }
                            }
                        });
            }
            }
        });
    }


    private void SignUp(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(registerActivity.this, otpVerification.class);
            startActivity(intent);
        } else {
            Toast.makeText(registerActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}



