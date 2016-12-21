package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {

    public Location locationGPS;
    public LocationManager locationManager;
    private MapView myOpenMapView;
    private MapController myMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myOpenMapView = (MapView) findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setUseDataConnection(true);
        myOpenMapView.setMultiTouchControls(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        IGeoPoint punto = new GeoPoint(locationGPS.getLatitude(),locationGPS.getLongitude());
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(18);
        myMapController.setCenter(punto);
        myMapController.animateTo(punto);
    }

    public void routes(View v) {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

    public void locateMe(View v) {
        locationGPS = this.damePuntoNuevo(locationGPS);
        IGeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(18);
        myMapController.setCenter(punto);
        myMapController.animateTo(punto);
    }
    public Location damePuntoNuevo(Location puntoAntiguo){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return puntoAntiguo;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return puntoAntiguo;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        });
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return locationGPS;
    }
}
