package com.ridelimos.ridelimos.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zebarahman on 6/8/17.
 */

public class RateCard {

    @SerializedName("car_type")
    int car_type;

    @SerializedName("price_km_day")
    String price_km_day;

    @SerializedName("price_km_night")
    String price_km_night;

    @SerializedName("wait_price_day")
    String wait_price_day;

    @SerializedName("wait_price_night")
    String wait_price_night;

    @SerializedName("start_price")
    String start_price;


}
