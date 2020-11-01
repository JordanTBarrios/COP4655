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
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.MalformedURLException;
import java.net.URL;

public class DisplayWeatherMap extends AppCompatActivity implements OnMapReadyCallback {

    private String openWeatherKey = "130ecc705ac2dd85de148abbeff2d7ff";
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
                                Intent historyIntent = new Intent (context, DisplayWeatherHistory.class);
                                startActivity(historyIntent); //is last
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {            /* Define the URL pattern for the tile images */
            String s = String.format("https://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid="+openWeatherKey+"", zoom, x, y);
                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }            try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }
            /*
            * Check that the tile server supports the requested x, y and zoom.
             * Complete this stub according to the tile range you support.
            * If you support a limited range of tiles at different zoom levels, then you
            * need to define the supported x, y range at each zoom level.
            */
            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 1;
                int maxZoom = 16;
                return (zoom >= minZoom && zoom <= maxZoom);
            }
        };

        //get weather data, which was set in main activity
        WeatherData data = MainActivity.getWeatherInstance();

        //convert latSearch and lonSearch String to double
        double latitude = Double.parseDouble(data.getLat());
        double longitude = Double.parseDouble(data.getLon());

        // Add a marker and move the camera
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title(data.getName() + ", " + data.getCountry()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(5));
        TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider)); //add tile overlay
    }
}
