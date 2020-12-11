package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
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
    //private FirebaseFirestore myDatabase = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        LinkedList<LocationData> favs = MainActivity.getFavList();

        //recycler view
        recyclerView = findViewById(R.id.recyclerView);
        //LinkedList<LocationData> aList = SearchActivity.getLocationList();

        /*
        final LinkedList<LocationData> favList = new LinkedList<LocationData>();

        FirebaseFirestore myDatabase = FirebaseFirestore.getInstance();
        myDatabase.collection(MainActivity.userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                //test.setText(document.get("name").toString());
                                //store a single location in a LocationData object
                                LocationData aFavorite = new LocationData();

                                Map<String, Object> aFav = document.getData();

                                for (Map.Entry<String, Object> entry : map.entrySet()){
                                    String key = entry.getKey();
                                    if (key == "name"){
                                        aFavorite.setName(entry.getValue().toString());
                                    }
                                }


                                aFavorite.setName(document.get("name").toString());
                                aFavorite.setReviewCount(Integer.parseInt(document.get("reviews").toString()));
                                aFavorite.setRating(Double.parseDouble(document.get("rating").toString()));
                                aFavorite.setAddress(document.get("address").toString());
                                aFavorite.setDistance(Double.parseDouble(document.get("distance").toString()));
                                aFavorite.setIsClosed(Boolean.parseBoolean(document.get("isClosed").toString()));
                                aFavorite.setPhone(document.get("phone").toString());
                                aFavorite.setUrl(document.get("url").toString());
                                aFavorite.setImgUrl(document.get("imgUrl").toString());


                                //add LocationData object to linkedlist
                                favList.add(aFavorite);
                            }
                        }
                    }
                });

        */

        //give linkedlist of locations to recycler adapter
        CustomAdapter mAdapter = new CustomAdapter(favs, this, R.layout.favorites_row_item);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //testing
        /*
        final TextView test = findViewById(R.id.testing);
        FirebaseFirestore myDatabase = FirebaseFirestore.getInstance();
        myDatabase.collection(MainActivity.userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                test.setText(document.get("name").toString());
                            }
                        }
                    }
                });

        */

        //popup drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_favorites);
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
                        Toast.makeText(FavoritesActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent); //is last
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

                                //Toast.makeText(DisplayWeatherMap.this, "Results", Toast.LENGTH_SHORT).show();

                                //go to search activity
                                Intent searchIntent = new Intent (getApplicationContext(), SearchActivity.class);
                                startActivity(searchIntent);
                                break;
                            case R.id.map_icon:
                                /*
                                Toast.makeText(DisplayWeatherMap.this, "Map", Toast.LENGTH_SHORT).show();
                                break;

                                 */
                                //go to map activity
                                LinkedList<LocationData> d1 = SearchActivity.getLocationList();
                                if (d1.isEmpty()) {
                                    Toast.makeText(FavoritesActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent mapIntent = new Intent (getApplicationContext(), MapActivity.class);
                                    startActivity(mapIntent);
                                }

                                //Toast.makeText(MapActivity.this, "Already on Map", Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.list_view_icon:

                                //go to list activity
                                LinkedList<LocationData> d2 = SearchActivity.getLocationList();
                                if (d2.isEmpty()) {
                                    Toast.makeText(FavoritesActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent listIntent = new Intent (getApplicationContext(), ListActivity.class);
                                    startActivity(listIntent);
                                }
                                //Toast.makeText(ListActivity.this, "Already on List", Toast.LENGTH_SHORT).show();
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

    public void onDeleteButtonClicked(View view){

    }

}