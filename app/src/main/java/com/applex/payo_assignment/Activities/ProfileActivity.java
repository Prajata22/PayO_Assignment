package com.applex.payo_assignment.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applex.payo_assignment.R;
import com.applex.payo_assignment.SharedPreferences.IntroPref;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView contact = findViewById(R.id.contact);
        TextView address = findViewById(R.id.address);

        IntroPref introPref = new IntroPref(ProfileActivity.this);

        if(introPref.getName() != null) {
            name.setText(introPref.getName());
        }
        if(introPref.getEmail() != null) {
            email.setText(introPref.getEmail());
        }
        if(introPref.getContact() != null) {
            contact.setText(introPref.getContact());
        }
        if(introPref.getAddress() != null) {
            address.setText(introPref.getAddress());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}