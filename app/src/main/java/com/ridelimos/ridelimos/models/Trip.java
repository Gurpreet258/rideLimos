package com.ridelimos.ridelimos.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zebarahman on 6/8/17.
 */

public class Trip implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("address")
    String address;

    @SerializedName("created_at")
    int created_at;

    @SerializedName("total_price")
    String total_price;


}
