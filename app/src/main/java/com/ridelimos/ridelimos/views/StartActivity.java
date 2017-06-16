package com.ridelimos.ridelimos.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ridelimos.ridelimos.R;

/**
 * Created by zebarahman on 6/9/17.
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        Fragment fragment = null;
        FragmentManager fragmentManager = StartActivity.this.getSupportFragmentManager();
        Class fragmentClass;

        fragmentClass = StartLoginFrag.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle("Login");

    }

}