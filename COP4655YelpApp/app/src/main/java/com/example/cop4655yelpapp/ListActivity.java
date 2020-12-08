package com.example.cop4655yelpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.LinkedList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView t = (TextView)findViewById(R.id.testText);
        LinkedList<LocationData> aList = SearchActivity.getLocationList();

        t.setText(aList.get(4).getAddress());
    }
}