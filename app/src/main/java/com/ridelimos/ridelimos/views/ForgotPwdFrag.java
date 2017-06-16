package com.ridelimos.ridelimos.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zebarahman on 6/13/17.
 */

public class ForgotPwdFrag extends Fragment {

    ApiInterface apiService;
    KProgressHUD loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_forgotpwd, container, false);

        Button btnsubmit = (Button) view.findViewById(R.id.btnsubmit);
        Button btngotoLogin = (Button) view.findViewById(R.id.btngotoLogin);

        final EditText inputEmail = (EditText) view.findViewById(R.id.input_email);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (!email.isEmpty()) {
                    loading.show();
                    resetPwd(email);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });
        loading = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait").setDetailsLabel("Logging in")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

        return view;
    }

    public void resetPwd(String em) {
        String body = "{\"email\":\"" + em + "\"}";
        RequestBody bodyb = RequestBody.create(MediaType.parse("text/plain"), body);

        Call<ResponseBody> call = apiService.resetPwd(bodyb);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());

                    } else {
                        Log.d("reset", response.body().string());
                    }
                    loading.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("reset", "error parsing");
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

}
