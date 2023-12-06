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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lk.software.app.foodorderingadminapp.model.User;

public class ViewLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    FirebaseFirestore firebaseFirestore;
    String customerId;
    TextView address, area, city, postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        firebaseFirestore = FirebaseFirestore.getInstance();

        customerId = getIntent().getExtras().getString("customerId");
        Log.d("customer_id",customerId);
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
        new Thread(()->{
            firebaseFirestore.collection("customers").document(customerId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {
                                    String latitude = user.getLatitude();
                                    String longitude = user.getLongitude();
                                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Delivery Location"));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 50));
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
        }).start();

    }




}