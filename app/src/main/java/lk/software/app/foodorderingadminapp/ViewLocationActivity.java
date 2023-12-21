package lk.software.app.foodorderingadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lk.software.app.foodorderingadminapp.model.User;
import lk.software.app.foodorderingadminapp.services.DirectionApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    FirebaseFirestore firebaseFirestore;
    String customerId;
    TextView address, area, city, postalCode;
    private Marker marker_current, marker_pin;

    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        firebaseFirestore = FirebaseFirestore.getInstance();

        customerId = getIntent().getExtras().getString("customerId");
        Log.d("customer_id", customerId);
        address = findViewById(R.id.textView25);
        area = findViewById(R.id.textView37);
        city = findViewById(R.id.textView38);
        postalCode = findViewById(R.id.textView23);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

            firebaseFirestore.collection("customers").document(customerId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {
                                    LatLng start = new LatLng(6.912159, 79.851629);
                                    String latitude = user.getLatitude();
                                    String longitude = user.getLongitude();
                                    LatLng end = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    googleMap.addMarker(new MarkerOptions().position(start).title("Delivery Location"));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 100));
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(end);
                                    marker_pin= googleMap.addMarker(markerOptions);
                                    getDirection(start,end);
                                    if (user.getAddress() != null) {
                                        address.setText(user.getAddress());
                                    }
                                    if (user.getCity() != null) {
                                        city.setText(user.getCity());
                                    }
                                    if (user.getArea() != null) {
                                        area.setText(user.getArea());
                                    }

                                    if (user.getPostal_code() != null) {
                                        postalCode.setText(user.getPostal_code());
                                    }

                                }


                            }


                        }
                    });


    }

    public void getDirection(LatLng start, LatLng end) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DirectionApi directionApi = retrofit.create(DirectionApi.class);
        String origin = start.latitude + "," + start.longitude;
        String destination = end.latitude + "," + end.longitude;
        String key = "AIzaSyD0unZbfKFx7KcMFSSjT-StDFrCOodA4XI";

        Call<JsonObject> json = directionApi.getJson(origin, destination, true, key);
        json.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    Log.d("response body", response.body().toString());
                    JsonObject body = response.body();

                    JsonArray routes = body.getAsJsonArray("routes");
                    JsonObject route = routes.get(0).getAsJsonObject();
                    JsonObject overview_polyline = route.getAsJsonObject("overview_polyline");
                    Log.d("polylines",overview_polyline.toString());
                    List<LatLng> points= PolyUtil.decode(overview_polyline.get("points").getAsString());


                    new Handler(Looper.getMainLooper()).post(()->{
                        if(polyline==null){
                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.width(20);
                            polylineOptions.color(getColor(R.color.black));
                            polylineOptions.addAll(points);
                            polyline = googleMap.addPolyline(polylineOptions);
                        }else{
                            polyline.setPoints(points);
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("failure",t.getMessage());

            }


        });
    }


}