package com.ridelimos.ridelimos.views;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ridelimos.ridelimos.R;
import com.ridelimos.ridelimos.adapters.HistoryAdapter;
import com.ridelimos.ridelimos.helpers.ApiClient;
import com.ridelimos.ridelimos.helpers.ApiInterface;
import com.ridelimos.ridelimos.helpers.Helper;
import com.ridelimos.ridelimos.helpers.SegmentedControl;
import com.ridelimos.ridelimos.models.Trip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zebarahman on 6/10/17.
 */

public class HistoryFrag extends Fragment {

    List<Trip> alltrips = new ArrayList<>();
    ArrayList<Trip> trips1 = new ArrayList<>();
    ArrayList<Trip> trips2 = new ArrayList<>();
    ArrayList<Trip> trips3 = new ArrayList<>();

    ApiInterface apiService;
    ListView listview;
    SegmentedControl segment;
    HistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_history, container, false);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        segment = (SegmentedControl) view.findViewById(R.id.segmented_control);
        listview = (ListView) view.findViewById(R.id.listview);

        segment.check(R.id.opt_1);
        segment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = adapter.getItem(position);
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = TripDetailFrag.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Bundle bundle = new Bundle();
                bundle.putSerializable("trip",trip);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();

            }
        });

        return view;
    }

    public void getHistory() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "\"{\"skip\":0}\"");

        Call<ResponseBody> call = apiService.getHistory(Helper.getSavedToken(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    Log.d("history", res.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e("history", t.toString());
            }
        });
    }


}
