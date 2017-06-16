package com.ridelimos.ridelimos.views;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ridelimos.ridelimos.R;
import com.ridelimos.ridelimos.helpers.ApiClient;
import com.ridelimos.ridelimos.helpers.ApiInterface;
import com.ridelimos.ridelimos.helpers.Helper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zebarahman on 6/8/17.
 */

public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs;
    ApiInterface apiService;
    String TAG = "SPLASH ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        if (Helper.tokenExists() == true) {
            isTokenValid();
        } else {
            goToStart();
        }
    }

    public void isTokenValid() {

        String token = Helper.getSavedToken();

        Call<ResponseBody> call = apiService.getUser(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    Log.d(TAG, res.toString());
                    goToMain();
                } catch (Exception e) {
                    e.printStackTrace();
                    goToStart();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e(TAG, t.toString());
                goToStart();
            }
        });

    }

    public void goToStart() {
        Thread splashThread = new Thread() {
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    final Intent j = new Intent(SplashActivity.this, StartActivity.class);
                    finish();
                    startActivity(j);
                }
            }
        };
        splashThread.start();
    }

    public void goToMain() {
        Thread splashThread = new Thread() {
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    final Intent j = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                    startActivity(j);
                }
            }
        };
        splashThread.start();
    }
}
