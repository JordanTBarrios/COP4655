package com.example.cop4655yelpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
