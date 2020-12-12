package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private String name;
    private int reviews;
    private double rating;
    private String address;
    private double distance;
    private boolean isClosed;
    private String phone;
    private String url;
    private String imgUrl;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //gets the location data from Intent
        getData();

        //Set the values of the views
        //name
        TextView nameTV = (TextView) findViewById(R.id.nameTV);
        nameTV.setText(name);

        //reviews
        TextView reviewsTV = (TextView) findViewById(R.id.reviewsTV);
        reviewsTV.setText("Reviews: " + reviews);

        //rating
        TextView ratingTV = (TextView) findViewById(R.id.ratingTV);
        ratingTV.setText("Rating: " + rating);

        //address
        TextView addressTV = (TextView) findViewById(R.id.addressTV);
        addressTV.setText(address);

        //distance
        TextView distanceTV = (TextView) findViewById(R.id.distanceTV);
        distanceTV.setText(distance + "mi");

        //isClosed
        if (isClosed){
            //Red "Closed" if closed
            TextView isClosedTV = (TextView) findViewById(R.id.isClosedTV);
            isClosedTV.setText("Closed");
            isClosedTV.setTextColor(Color.RED);
        } else {
            //Green "Open" if opened
            TextView isClosedTV = (TextView) findViewById(R.id.isClosedTV);
            isClosedTV.setText("Open");
            isClosedTV.setTextColor(Color.GREEN);
        }

        //phone
        TextView phoneTV = (TextView) findViewById(R.id.phoneTV);
        phoneTV.setText(phone);

        //location image
        ImageView locationImage = (ImageView) findViewById(R.id.locationImageView);
        Picasso.get().load(imgUrl).into(locationImage);


        //Popup side drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_details);
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
                        Toast.makeText(DetailsActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent mainIntent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(mainIntent);
                        break;
                    case R.id.favorites_drawer_item:
                        //go to favorites activity
                        Toast.makeText(DetailsActivity.this, "Favorites",Toast.LENGTH_SHORT).show();
                        Intent favIntent = new Intent (getApplicationContext(), FavoritesActivity.class);
                        startActivity(favIntent);
                        break;
                    default:
                        return true;
                }

                return true;

            }
        });


        //listener for the bottom navigation bar
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

    //gets the location data from Intent
    public void getData(){
        name = getIntent().getStringExtra("locName");
        reviews = getIntent().getIntExtra("locReviews", 0);
        rating = getIntent().getDoubleExtra("locRating", 0);
        address = getIntent().getStringExtra("locAddress");
        distance = getIntent().getDoubleExtra("locDistance", 0);
        isClosed = getIntent().getBooleanExtra("locIsClosed", true);
        phone = getIntent().getStringExtra("locPhone");
        url = getIntent().getStringExtra("locUrl");
        imgUrl = getIntent().getStringExtra("locImgUrl");
    }

    //Opens new activity with this location's real Yelp page
    public void onWebsiteClick(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    //Add this location to current user's favorites
    public void onAddFavoriteClick(View view){
        //access Cloud Firestore database
        FirebaseFirestore myDatabase = FirebaseFirestore.getInstance();

        //add location attributes to hashmap
        Map<String, Object> location = new HashMap<>();
        location.put("name", name);
        location.put("reviews", reviews);
        location.put("rating", rating);
        location.put("address", address);
        location.put("distance", distance);
        location.put("isClosed", isClosed);
        location.put("phone", phone);
        location.put("url", url);
        location.put("imgUrl", imgUrl);

        //add location hashmap to user collection
        //sets the location name as the document name
        myDatabase.collection(MainActivity.userEmail)
                .document(name)
                .set(location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: Not added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                });

        //update favorites after adding location to favorites
        MainActivity.updateFavList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}