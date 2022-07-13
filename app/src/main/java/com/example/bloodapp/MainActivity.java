package com.example.bloodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String appID = "2119851231e76a34"; // Replace with your App ID
        String region = "us"; // Replace with your App Region ("eu" or "us")

        AppSettings appSettings=new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(this, appID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Log.d("**COMETCHAT**", "Initialization completed successfully");
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("**COMETCHAT**", "Initialization failed with exception: " + e.getMessage());
            }
        });


        Button ToSignUp=findViewById(R.id.ToSignUpPagebtn);
        Button ToSignIn=findViewById(R.id.ToSignInPagebtn);
        Button ToMaps=findViewById(R.id.Mapsbtn);
        Button cards=findViewById(R.id.btncard);
        Button ToChat=findViewById(R.id.ToChat);
        Button homesScreen = findViewById(R.id.Homescreen);

        homesScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),homeScreen.class);
                startActivity(i);
            }
        });

       ToChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(getApplicationContext(),ChatActivity.class);
               startActivity(i);
           }
       });


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        else {

        }
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),dispCards.class);
                startActivity(i);
            }
        });
        ToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MapActivity.class);
                //Log.d("**userid**",FirebaseAuth.getInstance().getCurrentUser().toString());
                i.putExtra("UID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(i);
            }
        });

        ToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),SignUpAcitivity.class);
                startActivity(i);
            }
        });
        ToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),SignInAcitivity.class);
                startActivity(i);
            }
        });
        Button Logoutbtn=findViewById(R.id.LogoutBtb);

        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOutComet();
                FirebaseAuth.getInstance().signOut();
                ChangeLoggedStatus();
            }
        });
        ChangeLoggedStatus();
    }
    public  void ChangeLoggedStatus(){
        TextView tv=findViewById(R.id.loggedinstatus);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //User is Logged in
            tv.setText("LOGGED IN!!");
            Intent i = new Intent(getApplicationContext(),homeScreen.class);
            startActivity(i);
        }else{
            //No User is Logged in
            tv.setText("Not LOGGED IN!!");
        }
    }

    public void LogOutComet(){
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Log.d("**cometlogout**", "Logout completed successfully");
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("**cometlogout**", "Logout failed with exception: " + e.getMessage());
            }
        });
    }
}