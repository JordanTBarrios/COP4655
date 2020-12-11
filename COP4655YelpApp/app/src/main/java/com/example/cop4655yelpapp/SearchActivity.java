package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private String ACCESS_TOKEN = "LDbyPaYNGb0zZFNTYCaPgNH4nX1RmT89eM_eMPH0CUU97y9Jo6sdgaD_edk-u2x3GzFCZE2LwjemYhaBpJkTNu2j5i9y_FA65yVYSy3eIwtkuqborRUnBOa9SmTNX3Yx";

    public static LinkedList<LocationData> data = new LinkedList<LocationData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*
        //my call to drawer nav
        DrawerNav myNav = new DrawerNav();
        myNav.initDrawerNav(R.id.activity_search, this, this);
        */

        //popup drawer navigation
        dl = (DrawerLayout)findViewById(R.id.activity_search);
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
                        Toast.makeText(SearchActivity.this, "Sign out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent); //is last
                        break;
                    case R.id.favorites_drawer_item:
                        Toast.makeText(SearchActivity.this, "Favorites",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SearchActivity.this, "Already on Search", Toast.LENGTH_SHORT).show();
                                //break;
                                break;
                            case R.id.map_icon:
                                /*
                                Toast.makeText(DisplayWeatherMap.this, "Map", Toast.LENGTH_SHORT).show();
                                break;

                                 */
                                //go to map activity
                                //Intent mapIntent = new Intent (getApplicationContext(), MapActivity.class);
                                //startActivity(mapIntent);
                                //Toast.makeText(MapActivity.this, "Already on Map", Toast.LENGTH_SHORT).show();

                                //go to map activity
                                LinkedList<LocationData> d1 = SearchActivity.getLocationList();
                                if (d1.isEmpty()) {
                                    Toast.makeText(SearchActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent mapIntent = new Intent (getApplicationContext(), MapActivity.class);
                                    startActivity(mapIntent);
                                }

                                break;
                            case R.id.list_view_icon:

                                //go to list activity
                                //Intent listIntent = new Intent (getApplicationContext(), ListActivity.class);
                                //startActivity(listIntent);
                                //go to list activity
                                LinkedList<LocationData> d2 = SearchActivity.getLocationList();
                                if (d2.isEmpty()) {
                                    Toast.makeText(SearchActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
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

    public static LinkedList<LocationData> getLocationList(){ return data; }

    public void onSubmitClick(View view){
        EditText searchField = (EditText) findViewById(R.id.SearchTextField);
        EditText locationField = (EditText) findViewById(R.id.LocationTextField);
        String searchInput = searchField.getText().toString();
        String locationInput = locationField.getText().toString();

        String url;

        //check if input is valid
        if (!searchInput.isEmpty() && !locationInput.isEmpty()){
            //call to api
            url = "https://api.yelp.com/v3/businesses/search?location=" + locationInput + "&term=" + searchInput;
            setLocationData(url);
        } else if (!searchInput.isEmpty() && locationInput.isEmpty()){

            //use default phone location... lat and lon

        } else {
            //print error
            Toast.makeText(getApplicationContext(), "Fill both search fields", Toast.LENGTH_SHORT).show();
        }


        //Intent intent = new Intent (getApplicationContext(), MapActivity.class);
        //startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void setLocationData(String url){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);
                //use api response
                try {

                    //main array
                    JSONArray businessesArray = response.getJSONArray("businesses");
                    
                    data.clear();

                    for (int i = 0; i < 10; i++) {
                        LocationData aLocation = new LocationData();//individual location

                        //ith business in the response
                        JSONObject aBusiness = businessesArray.getJSONObject(i);

                        //name
                        aLocation.setName(aBusiness.getString("name"));

                        //title
                        JSONArray categories = aBusiness.getJSONArray("categories");
                        JSONObject aCategory = categories.getJSONObject(0);
                        aLocation.setTitle(aCategory.getString("title"));

                        //image url
                        aLocation.setImgUrl(aBusiness.getString("image_url"));

                        //is closed
                        aLocation.setIsClosed(aBusiness.getBoolean("is_closed"));

                        //review count
                        aLocation.setReviewCount(aBusiness.getInt("review_count"));

                        //rating
                        aLocation.setRating(aBusiness.getDouble("rating"));

                        //latitude and longitude
                        JSONObject coordinates = aBusiness.getJSONObject("coordinates");
                        aLocation.setLat(coordinates.getDouble("latitude"));
                        aLocation.setLon(coordinates.getDouble("longitude"));

                        //address
                        JSONObject location = aBusiness.getJSONObject("location");
                        aLocation.setAddress(location.getString("address1")
                                + " " + location.getString("city")
                                + ", " + location.getString("state")
                                + " " + location.getString("zip_code"));

                        //phone
                        aLocation.setPhone(aBusiness.getString("display_phone"));

                        //distance
                        aLocation.setDistance(aBusiness.getDouble("distance"));

                        //website url
                        aLocation.setUrl(aBusiness.getString("url"));

                        //once done adding attributes to location, add to linked list
                        data.add(aLocation);
                    }
                    //Toast.makeText(getApplicationContext(), data.get(4).getName(), Toast.LENGTH_SHORT).show();

                    //switch activities
                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);
                } catch (final JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            //Bearer token header
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //need space after "bearer" else app will crash
                params.put("Authorization","bearer " + ACCESS_TOKEN);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}