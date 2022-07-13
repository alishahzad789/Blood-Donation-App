package com.example.bloodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class donor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        LinearLayout l = findViewById(R.id.search);
        getSupportActionBar().hide();
        LinearLayout donreq = findViewById(R.id.donationreq);
        LinearLayout help = findViewById(R.id.help);


    }
    public void layoutClicked(View v)
    {
        Intent i = new Intent(getApplicationContext(),MapActivity.class);
        i.putExtra("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        startActivity(i);
    }
    public void helpclicked(View v)
    {
        return;
    }
    public void requestClicked(View v)
    {
        Intent i = new Intent(getApplicationContext(),donationReq.class);
        startActivity(i);
    }

}