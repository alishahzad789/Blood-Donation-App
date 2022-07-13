package com.example.bloodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpAcitivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String email="";
    private String password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acitivity);
        mAuth = FirebaseAuth.getInstance();

        EditText EmailtextView=findViewById(R.id.EmailTextEdit);
        EditText passwordtextView=findViewById(R.id.PasswordTextEdit);
        Button SignUp=findViewById(R.id.SignUpBtn);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"SignUp Clicked!", Toast.LENGTH_LONG).show(); //onStart Called
                email=EmailtextView.getText().toString().trim();
                password=passwordtextView.getText().toString().trim();
                Intent i=new Intent(getApplicationContext(),ExtraInfoActivity.class);
                i.putExtra("EMAIL", email);
                i.putExtra("PASSWORD", password);
                Log.d("**email**", email);
                Log.d("**pass**", password);
                startActivity(i);

            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            Toast.makeText(getApplicationContext(),"User Already Logged In", Toast.LENGTH_LONG).show(); //onStart Called
        }
    }

}