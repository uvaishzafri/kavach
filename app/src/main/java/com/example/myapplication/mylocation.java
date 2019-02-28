package com.example.myapplication;

import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class mylocation implements LocationListener {
   String Location;

    @Override
    public void onLocationChanged(Location location) {
 Location="Current Location: " + location.getLatitude() + ", " + location.getLongitude();

    }
    public String getlocation()
    {
        return Location;
    }
    public void getlatitude()
    {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
