package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;

public class ListActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Recycler View initialization
        recyclerView = findViewById(R.id.recyclerView);
        LinkedList<LocationData> aList = SearchActivity.getLocationList();
        //pass search list to recycler adapter
        CustomAdapter mAdapter = new CustomAdapter(aList, this, R.layout.text_row_item);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Popup side drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_list);
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
                        Toast.makeText(ListActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favorites_drawer_item:
                        //go to favorites activity
                        Toast.makeText(ListActivity.this, "Favorites",Toast.LENGTH_SHORT).show();
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
                                break;
                            case R.id.map_icon:
                                //go to map activity
                                Intent mapIntent = new Intent (getApplicationContext(), MapActivity.class);
                                startActivity(mapIntent);
                                break;
                            case R.id.list_view_icon:
                                Toast.makeText(ListActivity.this, "Already on List", Toast.LENGTH_SHORT).show();
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