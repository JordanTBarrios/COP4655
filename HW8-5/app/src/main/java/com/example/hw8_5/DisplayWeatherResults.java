package com.example.hw8_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class DisplayWeatherResults extends AppCompatActivity {

    private Context context;
    private TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather_results);

        context = getApplicationContext();

        //get the intent that started this activity
        Intent intent = getIntent();

        //get weather data, which was set in main activity
        WeatherData data = MainActivity.getWeatherInstance();

        //Display weather information
        //Access the various views
        final TextView cityNameView = findViewById(R.id.cityName);
        final TextView temperatureView = findViewById(R.id.temp);

        //Access the various cells of table
        final TextView conditionText = findViewById(R.id.conditionText);
        final TextView feelsLikeText = findViewById(R.id.feelsLikeText);
        final TextView humidityText = findViewById(R.id.humidityText);
        final TextView pressureText = findViewById(R.id.pressureText);
        final TextView windText = findViewById(R.id.windText);
        final TextView geoCoordsText = findViewById(R.id.geoCoordsText);
        final TextView sunriseText = findViewById(R.id.sunriseText);
        final TextView sunsetText = findViewById(R.id.sunsetText);
        final TextView visibilityText = findViewById(R.id.visibilityText);
        final TextView countryText = findViewById(R.id.countryText);

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

        //set text-to-speech
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        //set the listeners for text-to-speech
        cityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = cityNameView.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        temperatureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = temperatureView.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Temperature: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        conditionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = conditionText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Condition: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        feelsLikeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = feelsLikeText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Feels Like: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        humidityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = humidityText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Humidity: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        pressureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = pressureText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Pressure: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        windText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = windText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Wind: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        geoCoordsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = geoCoordsText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        sunriseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = sunriseText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Sunrise: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        sunsetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = sunsetText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Sunset: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        visibilityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = visibilityText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Visibility: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        countryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = countryText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak("Country: " + toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}