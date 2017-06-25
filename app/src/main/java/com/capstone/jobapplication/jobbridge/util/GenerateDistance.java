package com.capstone.jobapplication.jobbridge.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sarah on 2017-06-24.
 */

public class GenerateDistance {
    private static final int maxResult = 1;


    public static LatLng reverseGeocoding(Context context, String locationName){
        if(!Geocoder.isPresent()){
            Log.w("MAP", "Geocoder implementation not present !");
        }
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geoCoder.getFromLocationName(locationName, maxResult);
            int tentatives = 0;
            while (addresses.size()==0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName("<address goes here>", 1);
                tentatives ++;
            }


            if(addresses.size() > 0){
                Log.d("MAP", "reverse Geocoding : locationName " + locationName + " - Latitude " + addresses.get(0).getLatitude() );
                Log.d("MAP", "reverse Geocoding : locationName " + locationName + " - Longitude " + addresses.get(0).getLongitude() );
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }else{
                //use http api
            }

        } catch (IOException e) {
            Log.d(GenerateDistance.class.getName(), "not possible finding LatLng for Address : " + locationName);
        }
        return null;
    }

    public static String findAdress(Context context, LatLng latLng){
        StringBuilder address = new StringBuilder();
        final int maxResult = 10;

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, maxResult);
            if (addresses.size() > 0)
            {
                Address first = addresses.get(0);
                for (int i=0; i<first.getMaxAddressLineIndex();i++){
                    address.append(first.getAddressLine(i));
                    address.append(" ");
                }
            }
        } catch (IOException e) {
            Log.d(GenerateDistance.class.getName(), "not possible finding address for LatLng : " + latLng.toString());
        }
        return address.toString();
    }

}