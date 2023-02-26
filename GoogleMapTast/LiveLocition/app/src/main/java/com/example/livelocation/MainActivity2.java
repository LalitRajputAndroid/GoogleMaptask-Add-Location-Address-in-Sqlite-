package com.example.livelocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.livelocation.Database.MapDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    SupportMapFragment fragment;
    FusedLocationProviderClient client;
    private GoogleMap map;
    Geocoder geocoder;
    List<Address> addresses;
    String selected_address;
    Button savebtn, showbtn;
    private String city, state, country, pincode, landmark, area;
    MapDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragments_id);

        savebtn = findViewById(R.id.saveaddress_btn_id);
        showbtn = findViewById(R.id.showlist_btn_id);

        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, ShowAddressesList.class));
            }
        });

        client = LocationServices.getFusedLocationProviderClient(this);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getmylocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void getmylocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                if (location != null) {

                    fragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here...!!");
                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {

                                    double selected_let = latLng.latitude;
                                    double selected_log = latLng.longitude;

                                    GetAddress(selected_let, selected_log);

                                    savebtn.setVisibility(View.VISIBLE);

                                    savebtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (area.isEmpty() || landmark.isEmpty() || city.isEmpty() || state.isEmpty() || country.isEmpty() || pincode.isEmpty()) {
                                                Toast.makeText(MainActivity2.this, "Address not found", Toast.LENGTH_SHORT).show();
                                            } else {

                                                database = new MapDatabase(MainActivity2.this);

                                                Modal modal = new Modal(area, city, state, country, landmark, pincode);
                                                database.addaddress(modal);

                                                startActivity(new Intent(MainActivity2.this, ShowAddressesList.class));
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(MainActivity2.this, "Address not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void GetAddress(double aLat, double aLng) {

        geocoder = new Geocoder(MainActivity2.this, Locale.getDefault());

        if (aLat != 0) {
            try {
                addresses = geocoder.getFromLocation(aLat, aLng, 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (addresses != null) {

                selected_address = addresses.get(0).getAddressLine(0);

                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                pincode = addresses.get(0).getPostalCode();
                landmark = addresses.get(0).getFeatureName();
                area = addresses.get(0).getSubLocality();

                if (selected_address != null) {

                    MarkerOptions options = new MarkerOptions();
                    LatLng latLng = new LatLng(aLat, aLng);
                    options.position(latLng).title(selected_address);
                    map.addMarker(options).showInfoWindow();

                }

            }
        }

    }
}