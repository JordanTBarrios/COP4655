package com.example.hw8_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayWeatherHistory extends AppCompatActivity {

    private String openWeatherKey = "130ecc705ac2dd85de148abbeff2d7ff";
    private String TAG = DisplayWeatherHistory.class.getSimpleName();
    private Context context;

    private ListView lv;
    ArrayList<HashMap<String, String>> weatherList;
    WeatherData data = MainActivity.getWeatherInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather_history);

        //get the list
        weatherList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        context = getApplicationContext();

        //get weather history
        doInBackground();

        //display list
        postExecute();

        //listener for the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.weather_results:
                                Toast.makeText(DisplayWeatherHistory.this, "Results", Toast.LENGTH_SHORT).show();
                                Intent resultsIntent = new Intent (context, DisplayWeatherResults.class);
                                startActivity(resultsIntent); //is last
                                break;
                            case R.id.map:
                                Toast.makeText(DisplayWeatherHistory.this, "Map", Toast.LENGTH_SHORT).show();
                                Intent mapIntent = new Intent (context, DisplayWeatherMap.class);
                                startActivity(mapIntent); //is last
                                break;
                            case R.id.weather_history:
                                Toast.makeText(DisplayWeatherHistory.this, "History", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                }
        );

    }

    public void doInBackground(){
        int searchTime = Integer.parseInt(data.getCurrentTime());
        String lat = data.getLat();
        String lon = data.getLon();

        for (int i = 0; i < 5; i++){
            final int day = i+1;

            searchTime = searchTime - 86400; //go back a day in time

            String historyUrl = "https://api.openweathermap.org/data/2.5/onecall/timemachine?lat="+ lat +"&lon="+ lon +"&dt="+ searchTime +"&units=imperial&appid="+ openWeatherKey +"";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, historyUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //temp hash map for single day weather
                                HashMap<String, String> weather = new HashMap<>();

                                JSONObject current = response.getJSONObject("current");
                                JSONArray w = current.getJSONArray("weather");
                                JSONObject obj = w.getJSONObject(0);

                                weather.put("day", day + " days ago");
                                weather.put("condition", obj.getString("main"));
                                weather.put("temp",  current.getString("temp") + "F");
                                weather.put("humidity", current.getString("humidity")+ "%");

                                weatherList.add(weather);

                            } catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //TODO: Handle error
                }
            });

            //Access the RequestQueue through your singleton class.
            MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }

    }

    //display the data in a list format
    public void postExecute() {
        ListAdapter adapter = new SimpleAdapter(DisplayWeatherHistory.this, weatherList,
                R.layout.list_item, new String[]{ "day","condition","temp","humidity"},
                new int[]{R.id.day, R.id.condition, R.id.temp, R.id.humidity});
        lv.setAdapter(adapter);
    }
}

