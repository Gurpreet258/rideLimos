package com.ridelimos.ridelimos.helpers;

import com.ridelimos.ridelimos.models.route.Direction;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zebarahman on 6/7/17.
 */

public interface ApiInterface {

    @GET("passenger/me")
    Call<ResponseBody> getUser(@Query("token") String token);

    @POST("passenger/register")
    Call<ResponseBody> registerUser(@Body RequestBody body);

    @POST("passenger/auth_register")
    Call<ResponseBody> loginUser(@Header("Authorization") String authstr, @Body RequestBody body);

    @GET("passenger/car_categories")
    Call<ResponseBody> getCarTypes(@Query("token") String token);

    @POST("passenger/estimate")
    Call<ResponseBody> getEstimate(@Query("token") String token,@Body RequestBody body);

    @POST("passenger/get_trips")
    Call<ResponseBody> getHistory(@Query("token") String token, @Body RequestBody body);

    @POST("passenger/get_cars")
    Call<ResponseBody> getCars(@Query("token") String token, @Body RequestBody body);

    @POST("passenger/make_order")
    Call<ResponseBody> makeOrder(@Query("token") String token, @Body RequestBody body);

    @POST("passenger/my_car")
    Call<ResponseBody> trackCar(@Query("token") String token, @Body RequestBody body);

    @POST("company/send_reset")
    Call<ResponseBody> resetPwd(@Body RequestBody body);

    @POST("passenger/rate_driver")
    Call<RequestBody> rateDriverTrip(@Query("token") String token, @Body RequestBody body);

    @POST("passenger/cancel_booking")
    Call<ResponseBody> cancelBooking(@Query("token") String token, @Body RequestBody body);

    @POST("passenger/update_token")
    Call<ResponseBody> updateToken();

    @POST("passenger/get_prices")
    Call<ResponseBody> getRateCards(@Query("token") String token, @Body RequestBody body);

    @GET("directions/json")
    Call<Direction> getRoute(@Query("origin") String origin,
                             @Query("destination") String destination,
                             @Query("key") String key
    );





}
