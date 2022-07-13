package com.example.bloodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class dispCards extends AppCompatActivity {
    private int starter = 66; //ASCII code for `B`
    LinearLayoutCompat cards;
    Button buttonAdd;
    Button buttonDoSth;
    TextView searching;
    int c = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_cards);
        JSONObject info;
        getSupportActionBar().hide();
        searching = findViewById(R.id.searching);
        cards = findViewById(R.id.cards);
        String btype = getIntent().getExtras().getString("btype");
        double lat= getIntent().getExtras().getDouble("lat");
        double lon = getIntent().getExtras().getDouble("lon");
       // buttonAdd = findViewById(R.id.butAdd);
        //buttonDoSth = findViewById(R.id.butDoSth);
        try {
            info = new JSONObject(getIntent().getExtras().getString("db"));
        JSONArray key = info.names ();
        for (int i = 0; i < key.length (); ++i) {

            String keys = key.getString(i);
            String value = info.getString(keys);
            JSONObject temp = new JSONObject(value);
            Log.d("**lat_lon_json", "key = " + keys + " value = " + temp.getDouble("lat"));
            Log.d("**lat_lon_json", "key = " + keys + " value = " + temp.getDouble("lon"));

            if (temp.getString("bloodtype").equals(btype) && !temp.getString("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                c++;
                CardView newCard = new CardView(dispCards.this);

                getLayoutInflater().inflate(R.layout.card_view, newCard);

                TextView name = newCard.findViewById(R.id.txtName);
                TextView blood = newCard.findViewById(R.id.txtBld);
                TextView dist = newCard.findViewById(R.id.txtDist);
                Button message = newCard.findViewById(R.id.btnMsg);

                name.setText(temp.getString("name"));
                blood.setText(temp.getString("bloodtype"));
                newCard.setTag(temp.getString("name")); //
                double lat2 = temp.getDouble("lat");
                double lon2 = temp.getDouble("lon");
                dist.setText(String.format("%.2f", distance(lat,lon,lat2,lon2))+" Km");
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String uid = temp.getString("uid");
                            String _name=temp.getString("name");
                            Intent i=new Intent(getApplicationContext(),ChatActivity.class);
                            i.putExtra("UID", uid);
                            i.putExtra("NAME", _name);
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                cards.addView(newCard);
            }
        }
         searching.setText(c+" Results Found");
        } catch (JSONException e) {
                searching.setText(0+" Results Found");
                e.printStackTrace();
            }
//        buttonAdd.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                CardView newCard = new CardView(dispCards.this);
//                getLayoutInflater().inflate(R.layout.card_view, newCard);
//
//                TextView t = newCard.findViewById(R.id.txtName);
//
//                String current = Character.toString((char) starter++);
//
//                t.setText("Block " + current);
//                newCard.setTag(current); //
//
//                cards.addView(newCard);
//            }
//        });
    }

    private void findBlockAndDoSomething(String name)
    {
        Log.d("MyTAG", "CLICK");

        for (int i = 0; i < cards.getChildCount(); i++)
        {
            CardView selected = (CardView) cards.getChildAt(i);

            if (selected.getTag() != null && selected.getTag().toString().equals(name))
            {
                // do something. E.g change block name
                TextView textViewClassesBlock1 = selected.findViewById(R.id.txtName);
                textViewClassesBlock1.setText("Block XXX");
                return;
            }
        }
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}