package com.ridelimos.ridelimos.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zebarahman on 6/8/17.
 */

public class CarType {

    int id;
    String title;
    String picture;
    String mineta;
    List<MyETA> etas;

    public CarType(int id, String title, String picture, String mineta) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.mineta = mineta;
    }

    public String getMineta() {
        return mineta;
    }

    public void setMineta(String mineta) {
        this.mineta = mineta;
    }

    public List<MyETA> getEtas() {
        return etas;
    }

    public void setEtas(List<MyETA> etas) {
        this.etas = etas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
