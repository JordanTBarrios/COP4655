package com.example.cop4655yelpapp;

public class LocationData {
    private String name;
    private String title;
    private String image_url;
    private boolean is_closed;
    private int review_count;
    private double rating;
    private double lat;
    private double lon;
    private String address;
    private String phone;
    private double distance;
    private String url;

    public LocationData(){};

    //setters
    public void setName(String n) { this.name = n; }
    public void setTitle(String t) { this.title = t; }
    public void setImgUrl(String i) { this.image_url = i; }
    public void setIsClosed(boolean i) { this.is_closed = i; }
    public void setReviewCount(int c) { this.review_count = c; }
    public void setRating(double r) { this.rating = r; }
    public void setLat(double l) { this.lat = l; }
    public void setLon(double l) { this.lon = l; }
    public void setAddress(String a) { this.address = a; }
    public void setPhone(String p) { this.phone = p; }
    public void setDistance(double d) { this.distance = d; }
    public void setUrl(String u) { this.url = u; }

    //getters
    public String getName(){ return name; }
    public String getTitle(){ return title; }
    public String getImgUrl(){ return image_url; }
    public boolean getIsClosed(){ return is_closed; }
    public int getReviewCount(){ return review_count; }
    public double getRating(){ return rating; }
    public double getLat(){ return lat; }
    public double getLon(){ return lon; }
    public String getAddress(){ return address; }
    public String getPhone(){ return phone; }
    public double getDistance(){ return distance; }
    public String getUrl(){ return url; }
}
