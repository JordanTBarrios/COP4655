package com.example.hw8_5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
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
    private TextView textView;

    private String TAG = MainActivity.class.getSimpleName();
    private Context context;

    private String openWeatherKey = "";
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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say city name");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    String openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + result.get(0) + "&units=imperial&APPID=" + openWeatherKey +"";
                    setWeatherData(openWeatherUrl);
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

        //check if input is a number
        if (isNum(input)){ //if zip code
            openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?zip=" + input + "&units=imperial&APPID=" + openWeatherKey +"";
            setWeatherData(openWeatherUrl);
        } else { //otherwise city name
            openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + input + "&units=imperial&APPID="+ openWeatherKey +"";
            setWeatherData(openWeatherUrl);
        }
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

        String openWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon="+ lon +"&units=imperial&APPID=" + openWeatherKey+"";
        setWeatherData(openWeatherUrl);
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

                            //get current time, used for weather history
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
