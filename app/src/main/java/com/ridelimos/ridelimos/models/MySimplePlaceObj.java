package com.ridelimos.ridelimos.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by zebarahman on 6/13/17.
 */

public class MySimplePlaceObj implements Serializable {

    String name;
    String vicinity;
    String placeID;
    LatLng coords;

    public MySimplePlaceObj(String name, String vicinity, String placeID, LatLng coords) {
        this.name = name;
        this.vicinity = vicinity;
        this.placeID = placeID;
        this.coords = coords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public LatLng getCoords() {
        return coords;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }
}
