package com.example.hw8_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DisplayWeatherMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather_map);

        context = getApplicationContext();

        //get the intent that started this activity
        Intent intent = getIntent();

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
                                Toast.makeText(DisplayWeatherMap.this, "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.weather_results:
                                Toast.makeText(DisplayWeatherMap.this, "Results", Toast.LENGTH_SHORT).show();
                                Intent resultsIntent = new Intent (context, DisplayWeatherResults.class);
                                startActivity(resultsIntent); //is last
                                break;
                            case R.id.map:
                                Toast.makeText(DisplayWeatherMap.this, "Map", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.weather_history:
                                Toast.makeText(DisplayWeatherMap.this, "History", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        WeatherData data = MainActivity.getWeatherInstance();

        //convert latSearch and lonSearch String to double
        double latitude = Double.parseDouble(data.getLat());
        double longitude = Double.parseDouble(data.getLon());

        // Add a marker and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(data.getName() + ", " + data.getCountry()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
