package com.ridelimos.ridelimos.views;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ridelimos.ridelimos.R;
import com.ridelimos.ridelimos.adapters.CarTypesAdapter;
import com.ridelimos.ridelimos.helpers.ApiClient;
import com.ridelimos.ridelimos.helpers.ApiInterface;
import com.ridelimos.ridelimos.helpers.CircleOverlayView;
import com.ridelimos.ridelimos.helpers.Helper;
import com.ridelimos.ridelimos.models.Car;
import com.ridelimos.ridelimos.models.CarType;
import com.ridelimos.ridelimos.models.route.Direction;
import com.ridelimos.ridelimos.models.route.OverviewPolyline;
import com.ridelimos.ridelimos.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by zebarahman on 6/10/17.
 */

public class BookFrag extends Fragment implements OnMapReadyCallback,LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 5001;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 5002;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 5003;
    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private LatLng originLatlong,desLatlong;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 0;
    private LocationManager locationManager;
    Marker mymark,originMark,desMark;
    View view;
    RecyclerView car_types_bar;
    ApiInterface apiService,googleApiService;
    private GoogleMap mMap;
    CarTypesAdapter ctAdapter;
    ArrayList<CarType> cartypes;
    ArrayList<Car> cars;
    LatLng fromcoords,tocoords;
    Boolean z = false;
    CarType selectedCarType;
    int sel;
    ImageView selCarImg;
    TextView selCarInfo, ratecard, walletinfo, estimateinfo, couponinfo;
    LinearLayout primaryBottomView,confBottomView;
    Button btnbooknow,btnconfbooking, btnbookcancel;
    RelativeLayout overlay;

    TextView tvStartLoc,tvDesLoc;
    ImageView ivStartLocPin,ivDesLocPin,ivCancelOverlay;
    private GoogleApiClient mGoogleApiClient;

    private TextView currentView;
    private Polyline polyline;
    private View mapView,dottedView;
    LinearLayout circleView;
    private ArrayList<String> listPermissionsNeeded;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.frag_book, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);
        googleApiService = ApiClient.getGoogleApiInterface().create(ApiInterface.class);

        primaryBottomView = (LinearLayout) view.findViewById(R.id.bottomViewPrimary);
        confBottomView = (LinearLayout) view.findViewById(R.id.bottomViewConf);
        selCarImg = (ImageView) view.findViewById(R.id.selCarImg);
        selCarInfo = (TextView) view.findViewById(R.id.selCarInfo);

        ivStartLocPin= (ImageView) view.findViewById(R.id.ivStartLocPin);
        ivDesLocPin= (ImageView) view.findViewById(R.id.ivDesLocPin);
        ivCancelOverlay= (ImageView) view.findViewById(R.id.ivCancelOverlay);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        car_types_bar = (RecyclerView) view.findViewById(R.id.car_types_bar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        car_types_bar.setLayoutManager(layoutManager);
        btnbooknow = (Button) view.findViewById(R.id.btnbooknow);
        btnconfbooking = (Button) view.findViewById(R.id.btnbookconfirm);
        btnbookcancel = (Button) view.findViewById(R.id.btnbookcancel);
        overlay = (RelativeLayout) view.findViewById(R.id.circleOverlay);
        tvStartLoc= (TextView) view.findViewById(R.id.tvStartLoc);
        tvDesLoc= (TextView) view.findViewById(R.id.tvDestLoc);

        dottedView=view.findViewById(R.id.viewDotted);
        circleView=(LinearLayout)view.findViewById(R.id.viewCircle);
        currentView=tvStartLoc;


        tvStartLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ivStartLocPin.setVisibility(View.VISIBLE);
                findPlace(v,0);
            }
        });
        tvDesLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ivDesLocPin.setVisibility(View.VISIBLE);
                */
                findPlace(v,1);
            }
        });

        ivStartLocPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnbooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfView();
            }
        });
        btnbookcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideConfView();
            }
        });


        btnconfbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay.setVisibility(View.VISIBLE);
                hideConfView();
                primaryBottomView.setVisibility(View.GONE);
                //set animation
                YoYo.with(Techniques.FadeOut)
                        .duration(1000)
                        .repeat(50)
                        .playOn(circleView);
                //handler.sendEmptyMessageDelayed(11,30000);
            }
        });

        ivCancelOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay.setVisibility(View.GONE);
                primaryBottomView.setVisibility(View.VISIBLE);
            }
        });

        //init

        permissionsRequest();
        hideConfView();

        buildGoogleApiClient();
        createLocationRequest();
        setUpMapIfNeeded();
        setHasOptionsMenu(true);

        getRates();
        getCarCategories();

        return view;
    }

    private void setUpMapIfNeeded() {
        SupportMapFragment supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);
        mapView=supportMapFragment.getView();
    }

    protected void permissionsRequest(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkAndRequestPermissions();
            return;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
        // mMap.clear();
       /* LatLng mumbai= new LatLng(19.0760, 72.8777);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 10.0f));
*/
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                //mMap.clear();
                //mymark = mMap.addMarker(new MarkerOptions().title("Location").position(point).draggable(true).visible(true));

            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Address address=getLocationFromLatLong(cameraPosition.target.latitude,cameraPosition.target.longitude);
                if(address!=null ) {
                    originLatlong=new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);
                    currentView.setText(address.getAddressLine(0)+" "+address.getAddressLine(1));
                }
                Log.i("centerLat",cameraPosition.target.latitude+"");

                Log.i("centerLong",cameraPosition.target.longitude+"");
            }
        });


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try{
            mMap.setMyLocationEnabled(true);
        } catch(SecurityException e) { }

       /* boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusOfGPS == false) {
            AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
            ad.setTitle("Alert");
            ad.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            ad.setMessage("Please turn on your GPS to enable the app to function properly.");
            ad.show();
        } else { }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        originLatlong=new LatLng(location.getLatitude(),location.getLongitude());
        Address address=getLocationFromLatLong(location.getLatitude(),location.getLongitude());
        if(address!=null)
        tvStartLoc.setText(address.getAddressLine(0)+" "+address.getAddressLine(1));
      /*  mymark = mMap.addMarker(new MarkerOptions().title("Location")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("greenpin",100,80)))
                .position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(true).visible(true));
*/

       /* try{ stopLocationUpdates(); }
        catch (SecurityException e) { }*/

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getCarCategories() {
        cartypes = new ArrayList<>();
        Call<ResponseBody> call = apiService.getCarTypes(Helper.getSavedToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        JSONArray data = res.getJSONArray("carcategories");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            CarType ct = new CarType(c.getInt("id"),c.getString("title"),c.getString("appicon"),"no cars");
                            cartypes.add(ct);
                        }
                        setCarTypes();
                    } else {
                        Log.d("login", "error parsing");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("login", "error parsing");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e("login error response", t.toString());
            }
        });
    }

    public void setCarTypes() {
        ctAdapter = new CarTypesAdapter(getActivity(),cartypes);
        car_types_bar.setAdapter(ctAdapter);
    }

    public void tryEstimate() {
        if (fromcoords == null || tocoords == null || selectedCarType == null) { }
        else { getEstimate(); }
    }

    public void getEstimate() {
        String bodystr = "{\"from_lat\":" + fromcoords.latitude + ",\"from_lon\":" + fromcoords.longitude + ",\"to_lat\":"+ tocoords.latitude + ",\"to_lon\":" + tocoords.longitude + ",\"vehicle_type_id\":" + selectedCarType.getId() + ",\"time\":15,\"company_id\":5}";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), bodystr);
        Call<ResponseBody> call = apiService.getEstimate(Helper.getSavedToken(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        Double price = res.getDouble("price");
                    } else {
                        Log.d("estimate", "error parsing");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("estimate", "error parsing");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e("login error response", t.toString());
            }
        });
    }

    private void displayEstimate() {

    }


    public void getRates() {
        String bodystr = "{\"company_id\":5}";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), bodystr);
        Call<ResponseBody> call = apiService.getRateCards(Helper.getSavedToken(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        Double price = res.getDouble("price");
                    } else {
                        Log.d("ratecards", "error parsing");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ratecards", "error parsing");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e("login error response", t.toString());
            }
        });
    }

    public void makeOrder() {
        String bodystr = "{\"lat\":"+fromcoords.latitude+",\"lon\":"+fromcoords.longitude+",\"address\":\""+ "" +"\",\"carType\":"+ selectedCarType.getId() +"}";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), bodystr);
        Call<ResponseBody> call = apiService.makeOrder(Helper.getSavedToken(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());

                    } else {
                        Log.d("makeorder", "error parsing");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("makeorder", "error parsing");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                Log.e("login error response", t.toString());
            }
        });
    }

    public void showConfView() {
        primaryBottomView.setVisibility(View.GONE);
        confBottomView.setVisibility(View.VISIBLE);
       /* confBottomView.setAlpha(0.0f);

// Start the animation

                .alpha(1.0f);*/
    }

    public void hideConfView() {
        primaryBottomView.setVisibility(View.VISIBLE);
        confBottomView.setVisibility(View.GONE);
    }

    public void bookedGoAhead() {

        /*Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = CharitiesFrag.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        final Bundle bundle = new Bundle();
        bundle.putSerializable("category", categ);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        CategoriesFrag myFrag = (CategoriesFrag)fragmentManager.findFragmentByTag("FRAG1");
        ft.hide(myFrag);
        ft.add(R.id.fragmentContainer,fragment);
        ft.addToBackStack(null);
        ft.commit();*/
    }


     public Address getLocationFromLatLong(double latitude,double longitude){
         Geocoder geocoder;
         List<Address> addresses;
         geocoder = new Geocoder(getActivity(), Locale.getDefault());

         try {
             addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             String city = addresses.get(0).getLocality();
             String state = addresses.get(0).getAdminArea();
             String country = addresses.get(0).getCountryName();
             String postalCode = addresses.get(0).getPostalCode();
             String knownName = addresses.get(0).getFeatureName();
             return addresses.get(0);
         } catch (Exception e) {
             e.printStackTrace();
         }

        return  null;
     }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(),this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

    }

    public void findPlace(View view,int type) {
        currentView= (TextView) view;
        try {

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(getActivity());
            if(type==0) //origin
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            else
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    if (place == null)
                        return;
                    originLatlong=place.getLatLng();

                    updateCamera(place.getLatLng());
                    Address address=getLocationFromLatLong(place.getLatLng().latitude,place.getLatLng().longitude);
                    currentView.setText(address.getAddressLine(0)+" "+address.getAddressLine(1));
                    if(desLatlong!=null)
                        getDestinationRoute(originLatlong,desLatlong);

                    Log.i(TAG, "Place: " + place.getName());
                }
                else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    if (place == null)
                        return;
                    desLatlong=place.getLatLng();

                    Address address=getLocationFromLatLong(place.getLatLng().latitude,place.getLatLng().longitude);
                    currentView.setText(address.getAddressLine(0)+" "+address.getAddressLine(1));
                    getDestinationRoute(originLatlong,place.getLatLng());
                }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }


    }


    /**
     * Method to get route to destination
     * @param origin lat,long
     * @param destination lat,long
     */
    public void getDestinationRoute(final LatLng origin, final LatLng destination) {


        googleApiService.getRoute(origin.latitude+","+origin.longitude,
                destination.latitude+","+destination.longitude,getString(R.string.google_maps_key)).
                enqueue(new Callback<Direction>() {
                            @Override
                            public void onResponse(Call<Direction> call, Response<Direction> response) {
                               Direction direction=response.body();
                                if(direction.getRoutes().size()==0){
                                    return;
                                }
                                if(mMap==null)
                                    return;
                                mMap.clear();
                                ivStartLocPin.setVisibility(View.GONE);
                                dottedView.setVisibility(View.GONE);
                                mMap.setOnCameraChangeListener(null);
                                OverviewPolyline overviewPolyline=direction.getRoutes().get(0).getOverviewPolyline();

                                List<LatLng> polylines= Utility.decodeDirectionsPolyline(overviewPolyline.getPoints());

                                PolylineOptions options = new PolylineOptions().width(5).color(Color.parseColor("#2d5d99")).geodesic(true);
                                options.addAll(polylines);
                                polyline=mMap.addPolyline(options);


                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin).include(destination);

                                //set markers
                                mMap.addMarker(new MarkerOptions().title("Location").position(origin).draggable(true)
                                        .visible(true));
                                mMap.addMarker(new MarkerOptions().title("Location").position(destination).draggable(true)
                                        .visible(true));

                                mMap.setLatLngBoundsForCameraTarget(builder.build());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
                                mMap.moveCamera(cameraUpdate);
                            }

                            @Override
                            public void onFailure(Call<Direction> call, Throwable t) {

                            }
                        }
                );
    }



    private void updateCamera(LatLng latLng){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            overlay.setVisibility(View.GONE);
            primaryBottomView.setVisibility(View.VISIBLE);
        }
    };
    private  boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
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
                for(int i=0;i<listPermissionsNeeded.size();i++){
                    if(grantResults[i]== PackageManager.PERMISSION_DENIED){
                        //do your stuff
                       /* Toast.makeText(getActivity(), "some permissions Denied", Toast.LENGTH_SHORT)
                                .show();*/
                       getActivity().finish();
                        break;

                    }

                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}

