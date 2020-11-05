package com.applex.payo_assignment.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.applex.payo_assignment.R;
import com.applex.payo_assignment.SharedPreferences.IntroPref;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        IntroPref introPref = new IntroPref(this);
        ProgressBar progress = findViewById(R.id.progressbar);
        progress.setVisibility(View.VISIBLE);

        if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
            Toast.makeText(SplashActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
        else {
            if (introPref.isLoggedIn()) {
                new Handler().postDelayed(() -> {
                    progress.setVisibility(View.GONE);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }, 1500);
            }
            else {
                new Handler().postDelayed(() -> {
                    progress.setVisibility(View.GONE);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }, 1500);
            }
        }
    }
}