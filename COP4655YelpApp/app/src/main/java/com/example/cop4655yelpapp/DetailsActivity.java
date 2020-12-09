package com.example.cop4655yelpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getData();

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
            TextView isClosedTV = (TextView) findViewById(R.id.isClosedTV);
            isClosedTV.setText("Closed");
            isClosedTV.setTextColor(Color.RED);
        } else {
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
    }

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

    public void onWebsiteClick(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void onAddFavoriteClick(View view){

    }
}