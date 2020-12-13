package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        LinkedList<LocationData> favs = MainActivity.getFavList();

        //Recycler View initialization
        recyclerView = findViewById(R.id.recyclerView);
        //pass favorites list to recycler adapter
        CustomAdapter mAdapter = new CustomAdapter(favs, this, R.layout.favorites_row_item);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //center text on action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.ab_layout);

        //Popup side drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_favorites);
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
                        Toast.makeText(FavoritesActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favorites_drawer_item:
                        Toast.makeText(FavoritesActivity.this, "Already on favorites",Toast.LENGTH_SHORT).show();
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
                                break;
                            case R.id.map_icon:
                                //go to map activity
                                //Checks if user has searched yet
                                LinkedList<LocationData> d1 = SearchActivity.getLocationList();
                                if (d1.isEmpty()) {
                                    Toast.makeText(FavoritesActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent mapIntent = new Intent (getApplicationContext(), MapActivity.class);
                                    startActivity(mapIntent);
                                }
                                break;
                            case R.id.list_view_icon:
                                //go to list activity
                                //Checks if user has searched yet
                                LinkedList<LocationData> d2 = SearchActivity.getLocationList();
                                if (d2.isEmpty()) {
                                    Toast.makeText(FavoritesActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent listIntent = new Intent (getApplicationContext(), ListActivity.class);
                                    startActivity(listIntent);
                                }
                                break;
                        }

                        return true;

                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}