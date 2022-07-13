package com.example.bloodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    JSONObject final_info;
    String UserID = "";
    Double mylat;
    Double mylon;
    Integer c = 0;
    TextView resultsfound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();
        Button btn_loc = findViewById(R.id.btn_loc);
        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        resultsfound = findViewById(R.id.resultsfound);
        final double[] lon = new double[1];
        final double[] lat = new double[1];

        UserID = getIntent().getExtras().getString("UID");
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"O+", "B+", "AB+","AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
                } catch (Exception e) {
                }
            }
        }
        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), dispCards.class);
                //Log.d("**userid**",FirebaseAuth.getInstance().getCurrentUser().toString());
                String btype = dropdown.getItemAtPosition(dropdown.getSelectedItemPosition()).toString();
                i.putExtra("db", final_info.toString());
                i.putExtra("btype", btype);
                i.putExtra("lat",mylat);
                i.putExtra("lon",mylon);
                startActivity(i);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please provide Permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        GoogleMap mMap = googleMap;
        ArrayList<LatLng> locationArrayList = new ArrayList<>();
        Log.d("**map ready called", "xd");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        JSONObject info = new JSONObject();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<LatLng> tempArrayList = new ArrayList<>();
                Log.d("**User-snap**", snapshot.toString());
                String info = "";
                for (DataSnapshot child : snapshot.getChildren()) {
                    //Log.d("**User**",child.getKey()); // "-key1", "-key2", etc
                    info = child.getValue().toString();
                    Log.d("**User**", info); // true, true, etc
                }
                try {
                    JSONObject jobj = new JSONObject(info);
                    final_info = jobj;
                    Log.d("**ind**", jobj.toString());
//                    for(int i = 0; i<final_info.nam   es().length(); i++){
//                        Log.d("**iter_json", "key = " + final_info.names().getString(i) + " value = " + final_info.get(final_info.names().getString(i)));
//                    }
                    JSONArray key = final_info.names();
                    c = key.length()-1;
                    for (int i = 0; i < key.length(); ++i) {
                        String keys = key.getString(i);
                        String value = final_info.getString(keys);
                        Log.d("**iter_json", "key = " + keys + " value = " + value.getClass().getName());
                        JSONObject temp = new JSONObject(value);
                        Log.d("**lat_lon_json", "key = " + keys + " value = " + temp.getDouble("lat"));
                        Log.d("**lat_lon_json", "key = " + keys + " value = " + temp.getDouble("lon"));
                        if (keys.equals(UserID)) {
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            mylat =  temp.getDouble("lat");
                            mylon =  temp.getDouble("lon");
                        }
                        else {
                            tempArrayList.add(new LatLng(temp.getDouble("lat"), temp.getDouble("lon")));
                        }
                    }
                    locationArrayList.addAll(tempArrayList);
                    Log.d("**size of loc list", String.valueOf(locationArrayList.size()));
                    for (int i = 0; i < locationArrayList.size(); i++) {
                        mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

                        // below lin is use to zoom our camera on map.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

                        // below line is use to move our camera to the specific location.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
                    }
                    resultsfound.setText(c+" results found");
                } catch (JSONException e) {
                    e.printStackTrace();
                    resultsfound.setText(0+" results found");
                    Log.d("**error**", "lmao");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }



    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}