package com.applex.payo_assignment.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.applex.payo_assignment.Helpers.DatabaseHelper;
import com.applex.payo_assignment.R;
import com.applex.payo_assignment.SharedPreferences.IntroPref;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
        IntroPref introPref = new IntroPref(LoginActivity.this);

        EditText email_edit_text = findViewById(R.id.email);
        EditText password_edit_text = findViewById(R.id.password);
        MaterialButton login = findViewById(R.id.login);
        TextView sign_up = findViewById(R.id.sign_up);
        LottieAnimationView login_animation = findViewById(R.id.login_animation);

        sign_up.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            finish();
        });

        login.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                Toast.makeText(LoginActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }
            else {
                final String email = email_edit_text.getText().toString().trim();
                final String password = password_edit_text.getText().toString().trim();

                if (email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (email == null || email.isEmpty()) {
                        email_edit_text.setError("Email missing");
                        email_edit_text.requestFocus();
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        email_edit_text.setError("Please enter a valid email");
                        email_edit_text.requestFocus();
                    }
                }
                else if (password == null || password.isEmpty()) {
                    password_edit_text.setError("Password missing");
                    password_edit_text.requestFocus();
                }
                else {
                    if(databaseHelper.getListContents().getCount() == 0) {
                        Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ArrayList<String> data = databaseHelper.getData(email);
                        if(data == null) {
                            Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                        }
                        else if(!password.matches(data.get(4)) || !password.equals(data.get(4))) {
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            login_animation.setVisibility(View.VISIBLE);
                            introPref.setName(data.get(0));
                            introPref.setEmail(data.get(1));
                            introPref.setContact(data.get(2));
                            introPref.setAddress(data.get(3));
                            introPref.setPassword(data.get(4));
                            introPref.setIsLoggedIn(true);
                            new Handler().postDelayed(() -> {
                                login_animation.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }, 1500);
                        }
                    }
                }
            }
        });
    }
}