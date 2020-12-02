package com.example.cop4655yelpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
    }

    public void onSignInClick(View view){
        Intent SearchIntent = new Intent (context, MapActivity.class);
        startActivity(SearchIntent);
    }

    public void onSignUpClick(View view){

    }
}