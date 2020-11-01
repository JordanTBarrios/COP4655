package com.example.hw8_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE = 100;
    TextView textView;

    private String TAG = MainActivity.class.getSimpleName();
    private Context context;

    public static final String EXTRA_MESSAGE = "com.example.hw8_5.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "com.example.hw8_5.MESSAGE2";
    public static final String EXTRA_MESSAGE3 = "com.example.hw8_5.MESSAGE3";

    private String openWeatherKey = "130ecc705ac2dd85de148abbeff2d7ff";
    public static WeatherData data = new WeatherData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set context
        context = getApplicationContext();

        textView = findViewById(R.id.text);
        ImageView speak = findViewById(R.id.speak);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        //listener for the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.home:
                                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.weather_results:
                                Toast.makeText(MainActivity.this, "Results", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.map:
                                Toast.makeText(MainActivity.this, "Map", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.weather_history:
                                Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    public static WeatherData getWeatherInstance(){ return data;}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if ((resultCode == RESULT_OK) && (null != data)) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText(result.get(0));
                }
                break;
            }
        }
    }

    //if user presses "Search" button
    public void sendUserInput(View view){
        //Intent intent = new Intent (this, DisplayWeatherResults.class);
        EditText inputField = (EditText) findViewById(R.id.searchInput);
        String input = inputField.getText().toString();

        String openWeatherUrl;
        //check if input is a number\
        //if it is, search as zip code
        if (isNum(input)){ //if zip code
            openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?zip=" + input + "&units=imperial&APPID=" + openWeatherKey +"";
            setWeatherData(openWeatherUrl);
            /*
            intent.putExtra(EXTRA_MESSAGE, input);
            intent.putExtra(EXTRA_MESSAGE2, "none");
            intent.putExtra(EXTRA_MESSAGE3, "zip");
            startActivity(intent);
            */
        } else { //otherwise city name
            openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + input + "&units=imperial&APPID="+ openWeatherKey +"";
            setWeatherData(openWeatherUrl);
            /*
            intent.putExtra(EXTRA_MESSAGE, input);
            intent.putExtra(EXTRA_MESSAGE2, "none");
            intent.putExtra(EXTRA_MESSAGE3, "city");
            startActivity(intent);
            */
        }


        //startActivity(intent); //is last
    }

    //if user presses "GPS" button
    public void sendUserGPS (View view) {

        //Intent intent = new Intent (this, DisplayWeatherResults.class);
        EditText inputField = (EditText) findViewById(R.id.searchInput);
        String input = inputField.getText().toString();

        //split latitude and longitude.
        String[] arr = input.split(" ");
        String lat = arr[0];
        String lon = arr[1];

        /*
        intent.putExtra(EXTRA_MESSAGE, lat);
        intent.putExtra(EXTRA_MESSAGE2, lon);
        intent.putExtra(EXTRA_MESSAGE3, "gps");
        startActivity(intent);
        */
        String openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon="+ lon +"&units=imperial&APPID=" + openWeatherKey+"";
        setWeatherData(openWeatherUrl);

        //startActivity(intent);
    }

    //check if a String is a number
    public static boolean isNum(String str){
        if (str == null || str.equals("")){
            return false;
        }
        for (int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            if (c < '0' || c > '9'){
                return false;
            }
        }
        return true;
    }

    public void setWeatherData(String url){
        Intent intent = new Intent (this, DisplayWeatherResults.class);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            data.setName(response.getString("name"));
                            data.setVisibility(response.getString("visibility"));

                            //coordinates
                            JSONObject coord = response.getJSONObject("coord");
                            data.setLat(coord.getString("lat"));
                            data.setLon(coord.getString("lon"));
                            //latSearch = lat; //for google maps
                            //lonSearch = lon; //for google maps

                            //description
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            data.setDescription(weather.getString("description"));

                            //main
                            JSONObject main = response.getJSONObject("main");
                            data.setTemp(main.getString("temp"));
                            data.setFeelsLike(main.getString("feels_like"));
                            data.setPressure(main.getString("pressure"));
                            data.setHumidity(main.getString("humidity"));

                            //wind
                            JSONObject wind = response.getJSONObject("wind");
                            data.setSpeed(wind.getString("speed"));
                            data.setDeg(wind.getString("deg"));

                            //sys
                            JSONObject sys = response.getJSONObject("sys");
                            data.setCountry(sys.getString("country"));
                            data.setSunrise(sys.getString("sunrise"));
                            data.setSunset(sys.getString("sunset"));
                            //locationName = name + ", " + country; //for google maps

                            //get current time
                            data.setCurrentTime(response.getString("dt"));

                            Intent intent = new Intent (context, DisplayWeatherResults.class);
                            startActivity(intent); //is last


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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


}