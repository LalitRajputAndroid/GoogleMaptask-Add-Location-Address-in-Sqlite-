package com.example.livelocation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.audiofx.Equalizer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    GoogleMap map;
    Geocoder geocoder;
    List<Address> addresses;
    String selected_address;
    Button savebtn, showbtn;
    String city, state, country, pincode, landmark, area;
    MapDatabase database;

    int REQUEST_CODE = 11;
    ConnectivityManager manager;
    NetworkInfo networkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragments_id);

//        locationDialog();
//        checkconnection();

        savebtn = findViewById(R.id.saveaddress_btn_id);
        showbtn = findViewById(R.id.showlist_btn_id);

        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, ShowAddressesList.class));
            }
        });

        client = LocationServices.getFusedLocationProviderClient(MainActivity2.this);

//        if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            getmylocation();
//        } else {
//            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//        }

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

    private void getmylocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

//                                    if (networkInfo.isConnected() && networkInfo.isAvailable()) {

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

//                                    } else {
//                                        Toast.makeText(MainActivity2.this, "check Connection", Toast.LENGTH_SHORT).show();
//                                    }
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
//
//    public void locationDialog(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
//        builder.setTitle("Allow Location ");
//        builder.setMessage("Are You Sure ?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//        builder.create();
//        builder.show();
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getmylocation();
//            }
//        } else {
//            Toast.makeText(this, "Allow Permissskk", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void checkconnection() {
//
//        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
//        networkInfo = manager.getActiveNetworkInfo();
//    }
}