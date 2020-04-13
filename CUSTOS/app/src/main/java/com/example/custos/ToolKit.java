package com.example.custos;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ToolKit {

    public String getLocationText(double latitude, double longitude, Context context) {
        String locationText = "";
        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude,1);
            locationText = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
            Log.d("mylog","complete address: " + addresses.toString());
            Log.d("mylog","address: " + locationText);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationText;
    }

}
