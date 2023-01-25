package com.jiat.travelsrilanka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    private static final String TAG = "registerActivity";
    private FirebaseAuth firebaseauth;
    private LoginButton fblogin;
    CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.logiin).override(300,300).into(imageView);

        firebaseauth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.LoginTextField);
        EditText pswd = findViewById(R.id.LoginPasswordField);

        findViewById(R.id.noAccount).setOnClickListener(new View.OnClickListener() {

            EditText cEmail = findViewById(R.id.LoginTextField);
            EditText cPswd = findViewById(R.id.LoginPasswordField);





            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this,registerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String pswdText = pswd.getText().toString();

                if (emailText.isEmpty()){
                    Toast.makeText(loginActivity.this,"Email Not Entered",Toast.LENGTH_SHORT).show();
                }else if (pswdText.isEmpty()){
                    Toast.makeText(loginActivity.this,"Password Not Entered",Toast.LENGTH_SHORT).show();
                }else {


                firebaseauth.signInWithEmailAndPassword(emailText, pswdText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = firebaseauth.getCurrentUser();
                                    SignIn(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(loginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    SignIn(null);


                                }
                            }
                        });
                }
            }
        });






    }







    private void SignIn(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Log.d(TAG, "signInWithEmail:success");
            Toast.makeText(loginActivity.this,"Sign-In Failed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}