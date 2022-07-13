package com.example.bloodapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class homeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportActionBar().hide();
        ImageButton donor = findViewById(R.id.btnDoner);
        ImageButton donee = findViewById(R.id.btnDonee);


        donee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MapActivity.class);
                i.putExtra("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(i);
            }
        });
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),donor.class);
                i.putExtra("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(i);
            }
        });






    }
}