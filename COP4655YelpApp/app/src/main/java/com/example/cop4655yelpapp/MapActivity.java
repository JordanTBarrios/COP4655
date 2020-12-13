package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

import java.util.LinkedList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //google maps fragment initialization
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //center text on action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.ab_layout);

        //Popup side drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_map);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set drawer nav buttons' listeners
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.signOut_drawer_item:
                        //Sign out and go to main activity
                        Toast.makeText(MapActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favorites_drawer_item:
                        //go to favorites activity
                        Toast.makeText(MapActivity.this, "Favorites",Toast.LENGTH_SHORT).show();
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
                                //go to search activity
                                Intent searchIntent = new Intent (getApplicationContext(), SearchActivity.class);
                                startActivity(searchIntent);
                                //break;
                                break;
                            case R.id.map_icon:
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
        //get search location list
        LinkedList<LocationData> list = SearchActivity.getLocationList();
        double latitude;
        double longitude;
        LatLng location = new LatLng(0, 0);

        //For each location, place a marker on the map
        for (int i = 0; i < list.size(); i++){
            latitude = list.get(i).getLat();
            longitude = list.get(i).getLon();

            //get location and add marker
            location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location).title(list.get(i).getName()));
        }
        //zoom in on map and move to last location in the list
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
