package com.jiat.travelsrilanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpVerification extends AppCompatActivity {


    private FirebaseAuth firebaseauth;
    private static final String TAG ="otpVerification";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);


        ImageView imageView = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.otp).override(350,350).into(imageView);

        firebaseauth= FirebaseAuth.getInstance();

        EditText phone = findViewById(R.id.phonenumber);
        EditText OTPnumber = findViewById(R.id.verifyOtpCode);

        //send OTP button
        findViewById(R.id.sendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phone.getText().toString().isEmpty()){
                    Toast.makeText(otpVerification.this,"Phone Number Not Entered",Toast.LENGTH_SHORT).show();
                }else {

                    signInWithPhoneNumber(phone.getText().toString());
                }
            }
        });

        // Resend OTP Button
        findViewById(R.id.resendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTPCode(phone.getText().toString());
            }
        });

        //Verify OTP Button
        findViewById(R.id.verifyOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phone.getText().toString().isEmpty()){
                    Toast.makeText(otpVerification.this,"Phone Number Not Entered",Toast.LENGTH_SHORT).show();
                }else if (OTPnumber.getText().toString().isEmpty()) {
                    Toast.makeText(otpVerification.this,"Please Enter OTP Number",Toast.LENGTH_SHORT).show();
                }else {
                verifyOtp(OTPnumber.getText().toString());
            }}
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Toast.makeText(otpVerification.this,"OTP Code has Been Sent",Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }



        };
        }


    private void resendOTPCode(String phoneNumber) {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseauth)
                .setPhoneNumber("+94" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(mResendToken)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneNumber(String phoneNumber) {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseauth)
                .setPhoneNumber("+94" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        Task<AuthResult> authResultTask = firebaseauth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        }else{

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if(user != null){
            Intent intent = new Intent(otpVerification.this, MainActivity.class);
            startActivity(intent);
        }
    }


}