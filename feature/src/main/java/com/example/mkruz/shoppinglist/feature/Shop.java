package com.example.mkruz.shoppinglist.feature;

public class Shop {
    private int id;
    private String name;
    private String subtitle;
    private float lat;
    private float lng;
    private long range;
    private boolean favorite = false;

    public Shop(String name, String subtitle, float lat, float lng, long range){
        this.name = name;
        this.subtitle = subtitle;
        this.lat = lat;
        this.lng = lng;
        this.range = range;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
