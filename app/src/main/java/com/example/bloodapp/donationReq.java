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

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class donationReq extends AppCompatActivity {
    LinearLayoutCompat cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_req);
        cards = findViewById(R.id.mycards);
        int random = (int) Math.floor(Math.random() * (15 - 6 + 1) + 6);
        String[] array = {"Jim", "Fred", "Baz", "Bing", "Duck", "Swan", "Cooper", "Bing"};
        List<String> list = new ArrayList<String>();
        for (String lang : array) {
            list.add(lang);
        }
        List<Integer> dista = new ArrayList<Integer>();
        dista.add(1);
        dista.add(2);
        dista.add(3);
        dista.add(4);
        dista.add(5);
        dista.add(6);

        for (int i = 0; i < random; ++i) {

            CardView newCard = new CardView(donationReq.this);

            getLayoutInflater().inflate(R.layout.card_view, newCard);

            TextView name = newCard.findViewById(R.id.txtName);
            TextView blood = newCard.findViewById(R.id.txtBld);
            TextView dist = newCard.findViewById(R.id.txtDist);
            Button message = newCard.findViewById(R.id.btnMsg);

            name.setText(list.get(i));
            blood.setText("O+");
            newCard.setTag(list.get(i)); //
            Random rand = new Random();
            int randomElement = dista.get(rand.nextInt(dista.size()));
            dist.setText(randomElement + " Km");

            cards.addView(newCard);

        }
    }
}