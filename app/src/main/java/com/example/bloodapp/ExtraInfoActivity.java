package com.example.bloodapp;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class ExtraInfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String email="";
    private String password="";
    double lat =0 ;
    double lon =0;
    String authKey = "012036ce48f46824b9b05f810c85743ecac83b4b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_info);
        Intent i=new Intent();
        email =getIntent().getExtras().getString("EMAIL");
        password = getIntent().getExtras().getString("PASSWORD");
        mAuth = FirebaseAuth.getInstance();

        EditText idcardnoview=findViewById(R.id.idcardview);
        EditText lineaddrview=findViewById(R.id.lineaddressview);
        EditText provinceview=findViewById(R.id.provinceview);
        EditText cityiew=findViewById(R.id.cityview);
        EditText bloodtypeview=findViewById(R.id.bloodtypeview);
        Button SignUp=findViewById(R.id.signUpBtnFinal);
        EditText Nameview=findViewById(R.id.nameview);

        SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String idcardno=idcardnoview.getText().toString().trim();
                String addr=lineaddrview.getText().toString().trim();
                String province=provinceview.getText().toString().trim();
                String city=cityiew.getText().toString().trim();
                String bloodtype=bloodtypeview.getText().toString().trim();
                String Name=Nameview.getText().toString().trim();


                //Log.d("**vals**", idcardno+" "+addr+" ");
                Toast.makeText(ExtraInfoActivity.this,"SignUp Clicked!", Toast.LENGTH_SHORT).show(); //onStart Called
                if(ActivityCompat.checkSelfPermission(ExtraInfoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ExtraInfoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(ExtraInfoActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                else {
                    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                    final Location[] loc = {lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)};
                    if (loc[0] == null) {
                        Toast.makeText(getApplicationContext(), "loc null", Toast.LENGTH_LONG);
                    } else {
                        try {
                            Geocoder g = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> add = g.getFromLocation(loc[0].getLatitude(), loc[0].getLongitude(), 1);
                            Address aa = add.get(0);
                            String wholeaddress = aa.getAddressLine(0);
                            double longitude = loc[0].getLongitude();
                            double latitude = loc[0].getLatitude();
                            Log.d("**loc**", " " + longitude + "-----" + latitude);
                            lat = latitude;
                            lon = longitude;
                        } catch (Exception e) {
                        }
                    }
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ExtraInfoActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign/Up in success, update UI with the signed-in user's information
                                    Log.d("**Success**", "createUserWithEmail:success");
                                    FirebaseUser fuser = mAuth.getCurrentUser();
                                    String UID=fuser.getUid();


                                    WriteExtraInfoToDb(Name,UID,idcardno,addr,province,city,bloodtype,lat,lon);


                                    User user = new User();
                                    user.setUid(UID); // Replace with the UID for the user to be created
                                    user.setName(Name); // Replace with the name of the user
                                    CometChat.createUser(user, authKey, new CometChat.CallbackListener<User>() {
                                        @Override
                                        public void onSuccess(User user) {
                                            Log.d("**createUser", user.toString());
                                            SignIntoComet(UID);
                                        }

                                        @Override
                                        public void onError(CometChatException e) {
                                            Log.d("**createUser", e.getMessage());
                                        }
                                    });


                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Log.d("**Failure**", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
    void WriteExtraInfoToDb(String Name,String UID,String id,String addr,String province,String city,String bloodtype,double _lat,double _lon){
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");
           // Data D=new Data(Name,UID,id,addr,province,city,bloodtype,_lat,_lon);
            //myRef.child(UID).setValue(D);

            JSONObject temp = new JSONObject();
            temp.put("uid", UID);
            temp.put("name", Name);
            temp.put("addr", addr);
            temp.put("prov", province);
            temp.put("city", city);
            temp.put("idcard", id);
            temp.put("bloodtype", bloodtype);
            temp.put("lon", lon);
            temp.put("lat", lat);
            myRef.child(UID).setValue(temp.toString());

        }catch (Exception e){
            Log.d("**error**",e.toString());

        }

    }
    void SignIntoComet(String _UID){
        String UID = _UID; // Replace with the UID of the user to login
        String authKey = "012036ce48f46824b9b05f810c85743ecac83b4b"; // Replace with your App Auth Key

        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(UID, authKey, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    Log.d("**cometlogin", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("**cometlogin", "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            Log.d("**cometlogin", "Comet User Logged IN: ");
        }
    }
}