package com.example.hw8_5;

public class WeatherData {
    private String name;
    private String visibility;
    private String lat;
    private String lon;
    private String description;
    private String temp;
    private String feelsLike;
    private String pressure;
    private String humidity;
    private String speed;
    private String deg;
    private String country;
    private String sunrise;
    private String sunset;

    public WeatherData(){};

    //getters
    public void setName(String n){ this.name = n; }

    public void setVisibility(String v) { this.visibility = v; }

    public void setLat(String l) { this.lat = l; }

    public void setLon(String l) { this.lat = l; }

    public void setDescription(String d){this.description = d;}

    public void setTemp(String t){this.temp = t;}

    public void setFeelsLike(String f){this.temp = f;}

    public void setPressure(String p){this.pressure = p;}

    public void setHumidity(String h){this.humidity = h;}

    public void setSpeed(String s){this.speed = s;}

    public void setDeg(String d){this.deg = d;}

    public void setCountry(String c){this.country = c;}

    public void setSunrise(String s){this.sunrise = s;}

    public void setSunset(String s){this.sunset = s;}

    //setters
    public String getName(){return this.name;}

    public String getVisibility(){return this.visibility;}

    public String getLat(){return this.lat;}

    public String getLon(){return this.lon;}

    public String getDescription(){return this.description;}

    public String getTemp(){return this.temp;}

    public String getFeelsLike(){return this.feelsLike;}

    public String getPressure(){return this.pressure;}

    public String getHumidity(){return this.humidity;}

    public String getSpeed(){return this.speed;}

    public String getDeg(){return this.deg;}

    public String getCountry(){return this.country;}

    public String getSunrise(){return this.sunrise;}

    public String getSunset(){return this.sunset;}
}
