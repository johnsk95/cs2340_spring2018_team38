package com.example.team38;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created by Nathaniel on 3/9/2018.
 *
 * Lets users see a map
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap shelterMap;
    private List<HomelessShelter> shelters;
    @SuppressWarnings("unused")
    private Marker marker;
    private FusedLocationProviderClient client;
    private LocationRequest request;

    private static final int TWO_MINUTES_IN_MILLISECONDS = 120000;
    private static final int unknownVar2 = 11;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        @SuppressWarnings("ChainedMethodCall") SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get the shelters from ShelterListView
        //noinspection ChainedMethodCall
        shelters = getIntent().getParcelableArrayListExtra("SheltersToDisplay");
        // setting up the map
        client = LocationServices.getFusedLocationProviderClient(this);
        //noinspection ChainedMethodCall
        SupportMapFragment frag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        frag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        shelterMap = googleMap;
        // location request
        request = LocationRequest.create();
        request.setInterval(TWO_MINUTES_IN_MILLISECONDS);
        // two minutes (can be changed for higher accuracy if needed)
        request.setFastestInterval(TWO_MINUTES_IN_MILLISECONDS);
        // can use high accuracy if we need more precise location but uses more power
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            client.requestLocationUpdates(request, locationCallback, Looper.myLooper());
            shelterMap.setMyLocationEnabled(true);
        } else {
            //Request Location Permission
            checkLocationPermission();
        }


        // adds the shelter markers to the map
        int counter = 0;
        for (HomelessShelter shelter: shelters) {
            LatLng shelterLoc = new LatLng(shelter.longitude, shelter.latitude);
            marker = shelterMap.addMarker(
                    new MarkerOptions()
                    .position(shelterLoc)
                    .title(shelter.name)
            .snippet("Phone: " + shelter.phoneNumber));
            // sets tag to location in shelters array
            marker.setTag(counter);
            counter++;
        }
        // starts ShelterDetailView activity when a points window is clicked
        shelterMap.setOnInfoWindowClickListener(this);
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    // checks if the user has location services enabled
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // sends an alert to the user if location is disabled
                //noinspection ChainedMethodCall,ChainedMethodCall,
                //ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs your current location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        }).create().show();


            } else {
                // request location permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (client != null) {
            client.removeLocationUpdates(locationCallback);
        }
    }

    private final LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {

                if (marker != null) {
                    marker.remove();
                }

                //Place current location marker for device location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");


                //markerOptions.icon(BitmapDescriptorFactory
                // .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                //marker = shelterMap.addMarker(markerOptions);

                //move map camera
                shelterMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, unknownVar2));
            }
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        client.requestLocationUpdates(request, locationCallback, Looper.myLooper());
                        shelterMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Disable the functionality that depends on this permission.
                    //noinspection ChainedMethodCall
                    if (marker != null) {
                        shelterMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11));
                    }
                }
            }
        }

    }
    // Goes to ShelterDetailView for the point if it's window is clicked on
    @Override
    public void onInfoWindowClick(Marker marker) {
        HomelessShelter shelter = shelters.get((int) marker.getTag());
        Intent intent = new Intent(this, ShelterDetailView.class);
        intent.putExtra("HomelessShelter", shelter);
        startActivity(intent);
    }
}
