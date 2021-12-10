package com.example.shopiki.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.shopiki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 3000;
    private FirebaseAuth auth;
    private String admin_email_check = "admin1999@gmail.com";
    private String isAdmin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("firstTime", true);

        if (isFirstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

            Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        } else {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (auth.getCurrentUser() != null) {
                        isAdmin = auth.getCurrentUser().getEmail();
                        CheckAdmin();
                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    }


                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void CheckAdmin() {
        if (isAdmin.equalsIgnoreCase(admin_email_check)) {
            Intent i = new Intent(SplashActivity.this, AdminMenu.class);
            startActivity(i);

            // close this activity
            finish();
        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }
    }
}