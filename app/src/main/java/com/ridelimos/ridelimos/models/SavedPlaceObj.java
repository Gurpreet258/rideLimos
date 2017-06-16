package com.ridelimos.ridelimos.models;

import java.io.Serializable;

/**
 * Created by zebarahman on 6/13/17.
 */

public class SavedPlaceObj implements Serializable {

    String name;
    String add;
    Double lat;
    Double lon;

    public SavedPlaceObj(String name, String add, Double lat, Double lon) {
        this.name = name;
        this.add = add;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
