package com.example.cop4655yelpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.LinkedList;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //test
        /*
        TextView t = (TextView)findViewById(R.id.testText);
        LinkedList<LocationData> aList = SearchActivity.getLocationList();
        t.setText(aList.get(4).getAddress());
        */

        //recycler view
        recyclerView = findViewById(R.id.recyclerView);
        LinkedList<LocationData> aList = SearchActivity.getLocationList();

        CustomAdapter mAdapter = new CustomAdapter(aList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}