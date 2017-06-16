package com.ridelimos.ridelimos.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ridelimos.ridelimos.MyApplication;
import com.ridelimos.ridelimos.R;
import com.ridelimos.ridelimos.helpers.ApiClient;
import com.ridelimos.ridelimos.helpers.ApiInterface;
import com.ridelimos.ridelimos.helpers.Helper;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zebarahman on 6/9/17.
 */

public class StartLoginFrag extends Fragment {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 101;
    ApiInterface apiService;
    KProgressHUD loading;
    private ArrayList<String> listPermissionsNeeded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_start_login, container, false);

        Button btnGoToReg = (Button) view.findViewById(R.id.btnGoToReg);
        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);

        final EditText inputEmail = (EditText) view.findViewById(R.id.input_email);
        final EditText inputPassword = (EditText) view.findViewById(R.id.input_password);
        loading = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait").setDetailsLabel("Logging in")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        btnGoToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = StartRegFrag.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) { e.printStackTrace(); }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {
                    loading.show();
                    logMeIn(email,password);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void logMeIn(String em, String pass) {
        String authstr = Helper.getAuthorizationValue(em,pass);
        String body = "{\"username\":\"" + em + "\",\"password\":\"" + pass + "\"}";

        RequestBody bodyb = RequestBody.create(MediaType.parse("text/plain"), body);

        Call<ResponseBody> call = apiService.loginUser(authstr,bodyb);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        JSONObject data = res.getJSONObject("data");
                        String token = data.getString("token");
                        goAhead(token);
                    } else {
                        Log.d("login", response.body().string());
                    }
                    loading.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("login", "error parsing");
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e("login error response", t.toString());
                loading.dismiss();
            }
        });
    }

    public void goAhead(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token",token);
        editor.commit();
       /* if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkAndRequestPermissions();
            return;
        }*/
        final Intent j = new Intent(getActivity(), MainActivity.class);
        getActivity().finish();
        startActivity(j);
    }

    public static String optString(JSONObject json, String key) {
        if (json.isNull(key))
            return null;
        else
            return json.optString(key, null);
    }


    private  boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int callPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        checkGrantedPermissions(getActivity());
        listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }
        return true;
    }
    //for multiple permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                checkGrantedPermissions(getActivity());
                for(int i=0;i<listPermissionsNeeded.size();i++){
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        //do your stuff
                       /* Toast.makeText(getActivity(), "some permissions Denied", Toast.LENGTH_SHORT)
                                .show();*/
                        break;

                    }

                }
                Intent j = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(j);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void checkGrantedPermissions(Context context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Log.d("permissions","location_granted");
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("permissions","storage_granted");
        }
    }
}
