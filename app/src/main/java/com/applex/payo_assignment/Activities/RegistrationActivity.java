package com.applex.payo_assignment.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.applex.payo_assignment.Helpers.DatabaseHelper;
import com.applex.payo_assignment.R;
import com.applex.payo_assignment.SharedPreferences.IntroPref;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        DatabaseHelper databaseHelper = new DatabaseHelper(RegistrationActivity.this);
        IntroPref introPref = new IntroPref(RegistrationActivity.this);

        EditText first_name_edit_text = findViewById(R.id.first_name);
        EditText last_name_edit_text = findViewById(R.id.last_name);
        EditText email_edit_text = findViewById(R.id.email);
        EditText contact_edit_text = findViewById(R.id.contact);
        EditText address_line_edit_text = findViewById(R.id.address);
        EditText city_edit_text = findViewById(R.id.city);
        EditText state_edit_text = findViewById(R.id.state);
        EditText pin_code_edit_text = findViewById(R.id.pin_code);
        EditText password_edit_text = findViewById(R.id.password);
        EditText confirm_password_edit_text = findViewById(R.id.confirm_password);
        MaterialButton proceed = findViewById(R.id.proceed);

        Dialog sign_up_animation_dialog = new Dialog(RegistrationActivity.this);
        sign_up_animation_dialog.setContentView(R.layout.dialog_animation);
        sign_up_animation_dialog.setCancelable(false);

        proceed.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                Toast.makeText(RegistrationActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }
            else {
                final String first_name = first_name_edit_text.getText().toString().trim();
                final String last_name = last_name_edit_text.getText().toString().trim();
                final String email = email_edit_text.getText().toString().trim();
                final String contact = contact_edit_text.getText().toString().trim();
                final String address_line = address_line_edit_text.getText().toString().trim();
                final String city = city_edit_text.getText().toString().trim();
                final String state = state_edit_text.getText().toString().trim();
                final String pin_code = pin_code_edit_text.getText().toString().trim();
                final String password = password_edit_text.getText().toString().trim();
                final String confirm_password = confirm_password_edit_text.getText().toString().trim();

                if (first_name == null || first_name.isEmpty()) {
                    first_name_edit_text.setError("First name missing");
                    first_name_edit_text.requestFocus();
                }
                else if (last_name == null || last_name.isEmpty()) {
                    last_name_edit_text.setError("Last name missing");
                    last_name_edit_text.requestFocus();
                }
                else if (email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (email == null || email.isEmpty()) {
                        email_edit_text.setError("Email missing");
                        email_edit_text.requestFocus();
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        email_edit_text.setError("Please enter a valid email");
                        email_edit_text.requestFocus();
                    }
                }
                else if (contact == null || contact.isEmpty()) {
                    contact_edit_text.setError("Contact number missing");
                    contact_edit_text.requestFocus();
                }
                else if (address_line == null || address_line.isEmpty()) {
                    address_line_edit_text.setError("Address line missing");
                    address_line_edit_text.requestFocus();
                }
                else if (city == null || city.isEmpty()) {
                    city_edit_text.setError("City missing");
                    city_edit_text.requestFocus();
                }
                else if (state == null || state.isEmpty()) {
                    state_edit_text.setError("State missing");
                    state_edit_text.requestFocus();
                }
                else if (pin_code == null || pin_code.length() != 6) {
                    if (pin_code == null || pin_code.isEmpty()) {
                        pin_code_edit_text.setError("Email missing");
                        pin_code_edit_text.requestFocus();
                    }
                    else if (pin_code.length() != 6) {
                        pin_code_edit_text.setError("Please enter a valid pin_code");
                        pin_code_edit_text.requestFocus();
                    }
                }
                else if (password == null || password.isEmpty()) {
                    password_edit_text.setError("Password missing");
                    password_edit_text.requestFocus();
                }
                else if (confirm_password == null || confirm_password.isEmpty()) {
                    confirm_password_edit_text.setError("Please re-enter your password once");
                    confirm_password_edit_text.requestFocus();
                }
                else if(!password.matches(confirm_password) || !password.equals(confirm_password)) {
                    password_edit_text.setError("Passwords not matching");
                    password_edit_text.requestFocus();
                    confirm_password_edit_text.setError("Passwords not matching");
                    confirm_password_edit_text.requestFocus();
                }
                else {
                    sign_up_animation_dialog.show();
                    ArrayList<String> data = databaseHelper.getData(email);
                    if(data != null) {
                        new Handler().postDelayed(() -> {
                            if (sign_up_animation_dialog != null && sign_up_animation_dialog.isShowing())
                                sign_up_animation_dialog.dismiss();
                            introPref.setIsLoggedIn(false);
                            Toast.makeText(RegistrationActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                        }, 1500);
                    }
                    else {
                        introPref.setName(first_name + " " + last_name);
                        introPref.setEmail(email);
                        introPref.setContact(contact);
                        introPref.setAddress(address_line + ", " + city + " - " + pin_code + ", " + state);
                        introPref.setPassword(password);
                        boolean result = databaseHelper.addData(first_name + " " + last_name, email, contact,
                                address_line + ", " + city + " - " + pin_code + ", " + state, password);
                        if(result) {
                            new Handler().postDelayed(() -> {
                                if (sign_up_animation_dialog != null && sign_up_animation_dialog.isShowing())
                                    sign_up_animation_dialog.dismiss();
                                introPref.setIsLoggedIn(true);
                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                finish();
                            }, 1500);
                        } else {
                            new Handler().postDelayed(() -> {
                                if (sign_up_animation_dialog != null && sign_up_animation_dialog.isShowing())
                                    sign_up_animation_dialog.dismiss();
                                introPref.setIsLoggedIn(false);
                                Toast.makeText(RegistrationActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            }, 1500);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Are you sure?")
                .setMessage("All your entries will be lost")
                .setPositiveButton("Ok", (dialog, which) -> RegistrationActivity.super.onBackPressed())
                .setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}