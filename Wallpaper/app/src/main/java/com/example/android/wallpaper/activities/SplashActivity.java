package com.example.android.wallpaper.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.wallpaper.MainActivity;
import com.example.android.wallpaper.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        },3500);

    }
}