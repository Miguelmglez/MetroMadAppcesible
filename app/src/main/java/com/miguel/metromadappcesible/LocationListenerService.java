package com.miguel.metromadappcesible;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import static com.miguel.metromadappcesible.activities.MapsActivity.myOpenMapViewMap;
import static com.miguel.metromadappcesible.activities.MapsActivity.myPositionMarkerMap;
import static com.miguel.metromadappcesible.activities.SolutionActivity.myOpenMapViewMapSolution;
import static com.miguel.metromadappcesible.activities.SolutionActivity.myPositionMarkerMapSolution;

/**
 * Created by Miguel Maroto González on 28-12-16.
 *
 * Servicio que se ejecuta en background y actualiza la posición en el mapa del usuario.
 *
 */
public class LocationListenerService extends Service {
    private static final String TAG = "SERVICE LOCATION";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 1;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            myPositionMarkerMap.setPosition(new GeoPoint(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
            myOpenMapViewMap.invalidate();
            if (myPositionMarkerMapSolution!=null) {
                myPositionMarkerMapSolution.setPosition(new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                myOpenMapViewMapSolution.invalidate();
            }
        }

        @Override
        public void onProviderDisabled(String provider){
        }

        @Override
        public void onProviderEnabled(String provider){
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}