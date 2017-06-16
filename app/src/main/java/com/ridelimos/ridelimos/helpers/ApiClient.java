package com.ridelimos.ridelimos.helpers;

import com.ridelimos.ridelimos.BuildConfig;
import com.ridelimos.ridelimos.Config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zebarahman on 6/7/17.
 */

public class ApiClient {

    private static Retrofit retrofit = null;
    public static Retrofit googleWebServices = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getGoogleApiInterface() {
        if (googleWebServices == null) {

            try {
                googleWebServices = new Retrofit.Builder()
                        .baseUrl(Config.GOOGLE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return googleWebServices;
    }
}
