package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //listener for the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.home:
                                /*
                                Toast.makeText(DisplayWeatherMap.this, "Results", Toast.LENGTH_SHORT).show();
                                Intent resultsIntent = new Intent (context, DisplayWeatherResults.class);
                                startActivity(resultsIntent); //is last
                                break;

                                 */
                            case R.id.map:
                                /*
                                Toast.makeText(DisplayWeatherMap.this, "Map", Toast.LENGTH_SHORT).show();
                                break;

                                 */
                            case R.id.more_icon:
                                //Intent historyIntent = new Intent (getApplicationContext(), DisplayWeatherHistory.class);
                                //startActivity(historyIntent); //is last
                                //MenuItem logout = findViewById(R.id.more_icon);
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                                startActivity(intent); //is last

                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap){


        //get weather data, which was set in main activity
        //WeatherData data = MainActivity.getWeatherInstance();

        //convert latSearch and lonSearch String to double
        double latitude = Double.parseDouble("27.27");
        double longitude = Double.parseDouble("-80.27");

        // Add a marker and move the camera
        LatLng location = new LatLng(latitude, longitude);
        //googleMap.addMarker(new MarkerOptions().position(location).title(data.getName() + ", " + data.getCountry()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(5));
        //TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider)); //add tile overlay
    }

}
