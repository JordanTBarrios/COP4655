package com.example.hw8_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DisplayWeatherResults extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather_results);

        context = getApplicationContext();

        //get the intent that started this activity
        Intent intent = getIntent();

        WeatherData data = MainActivity.getWeatherInstance();

        //Display weather information
        //Access the various views
        TextView cityNameView = findViewById(R.id.cityName);
        TextView temperatureView = findViewById(R.id.temp);

        //Access the various cells of table
        TextView conditionText = findViewById(R.id.conditionText);
        TextView feelsLikeText = findViewById(R.id.feelsLikeText);
        TextView humidityText = findViewById(R.id.humidityText);
        TextView pressureText = findViewById(R.id.pressureText);
        TextView windText = findViewById(R.id.windText);
        TextView geoCoordsText = findViewById(R.id.geoCoordsText);
        TextView sunriseText = findViewById(R.id.sunriseText);
        TextView sunsetText = findViewById(R.id.sunsetText);
        TextView visibilityText = findViewById(R.id.visibilityText);
        TextView countryText = findViewById(R.id.countryText);

        //set values in table
        cityNameView.setText(data.getName());
        temperatureView.setText(data.getTemp() + "F");
        conditionText.setText(data.getDescription());
        feelsLikeText.setText(data.getFeelsLike() + "F");
        humidityText.setText(data.getHumidity() + "%");
        pressureText.setText(data.getPressure() + "hPa");
        windText.setText(data.getSpeed() + "mph " + data.getDeg() + "deg");
        geoCoordsText.setText("lat: " + data.getLat() + "  lon: " + data.getLon());
        sunriseText.setText(data.getSunrise() + " unix UTC");
        sunsetText.setText(data.getSunset() + " unix UTC");
        visibilityText.setText(data.getVisibility() + "m");
        countryText.setText(data.getCountry());


        //listener for the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.home:
                                Toast.makeText(DisplayWeatherResults.this, "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.weather_results:
                                Toast.makeText(DisplayWeatherResults.this, "Results", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.map:
                                Toast.makeText(DisplayWeatherResults.this, "Map", Toast.LENGTH_SHORT).show();
                                Intent mapIntent = new Intent (context, DisplayWeatherMap.class);
                                startActivity(mapIntent); //is last
                                break;
                            case R.id.weather_history:
                                Toast.makeText(DisplayWeatherResults.this, "History", Toast.LENGTH_SHORT).show();
                                Intent historyIntent = new Intent (context, DisplayWeatherHistory.class);
                                startActivity(historyIntent); //is last
                                break;
                        }
                        return true;
                    }
                }
        );
    }
}