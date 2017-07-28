package com.capstone.jobapplication.jobbridge.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.JobDetailActivity;
import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Aicun on 6/3/2017.
 */

public class JobsMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private View rootView;
    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private double latitude;
    private double longitude;
    private LocationRequest locationRequest;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static int UPDATE_INTERVAL = 5000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleApiClient = new GoogleApiClient.Builder(getContext()).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},123);
        }

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(UPDATE_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng latLng = new LatLng(latitude, longitude);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).
                        zoom(12)
                        .bearing(0)
                        .tilt(25)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                addJobLocationsMarkers();
                googleMap.setInfoWindowAdapter(new JobInfoWindowAdapter());
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        int jobId = (int) marker.getTag();
                        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
                        intent.putExtra("jobId",jobId);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            new AlertDialog.Builder(getContext()).setMessage("Connection  failed. Error code: " + connectionResult.getErrorCode()).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECTION_FAILURE_RESOLUTION_REQUEST) {
            // perform additional processing here
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private void addJobLocationsMarkers() {
        googleMap.clear();
        List<Job> jobs = CacheData.cachedJobs();
        for (Job job : jobs) {
            LatLng position = job.getPosition();
            if(position == null) {
                position = getLocationFromAddress(job.getJobAddress());
                job.setPosition(position);
                CacheData.addJob(job.getId(),job);
            }
            if (position != null)
                googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(job.getTitle())
                        .snippet(job.getEmployer().getName()))
                        .setTag(job.getId());
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }

    class JobInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(final Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(final Marker marker) {
            // Getting view from the layout file info_window_layout
            View v = getActivity().getLayoutInflater().inflate(R.layout.marker_layout, null);

            TextView title = (TextView) v.findViewById(R.id.title);

            // Getting reference to the TextView to set longitude
            TextView snippet = (TextView) v.findViewById(R.id.snippet);

            // Setting the latitude
            title.setText(marker.getTitle());

            // Setting the longitude
            snippet.setText(marker.getSnippet());

            // Returning the view containing InfoWindow contents
            return v;
        }
    }
}
