package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //google maps fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //popup drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_map);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.signOut_drawer_item:
                        Toast.makeText(MapActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent); //is last
                        break;
                    case R.id.favorites_drawer_item:
                        Toast.makeText(MapActivity.this, "Favorites",Toast.LENGTH_SHORT).show();
                        //go to favorites activity
                        Intent favIntent = new Intent (getApplicationContext(), FavoritesActivity.class);
                        startActivity(favIntent);
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });


        //listener for the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.home_icon:

                                //Toast.makeText(DisplayWeatherMap.this, "Results", Toast.LENGTH_SHORT).show();

                                //go to search activity
                                Intent searchIntent = new Intent (getApplicationContext(), SearchActivity.class);
                                startActivity(searchIntent);
                                //break;
                                break;
                            case R.id.map_icon:
                                /*
                                Toast.makeText(DisplayWeatherMap.this, "Map", Toast.LENGTH_SHORT).show();
                                break;

                                 */
                                //go to map activity
                                Toast.makeText(MapActivity.this, "Already on Map", Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.list_view_icon:

                                //go to list activity
                                Intent listIntent = new Intent (getApplicationContext(), ListActivity.class);
                                startActivity(listIntent);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}
