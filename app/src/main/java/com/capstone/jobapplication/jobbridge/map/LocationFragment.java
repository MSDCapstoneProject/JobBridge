package com.capstone.jobapplication.jobbridge.map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.util.GenerateDistance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Sarah on 2017-06-23.
 */

public class LocationFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Context context;
        mMap = googleMap;
        LatLng location ;
        GenerateDistance gd = new GenerateDistance();
        //LocationFragment.context = getApplicationContext();

        location = gd.reverseGeocoding(this, "N2P 0C7");



        // Add a marker in Sydney, Australia, and move the camera.
        LatLng where = new LatLng(location.latitude, location.longitude);
        //TODO: focus
        mMap.addMarker(new MarkerOptions().position(where).title("Marker in Destination"));
        //mMap.setMinZoomPreference(6.0f);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(where));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(where , 14.0f) );
    }
}