package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class SolutionActivity extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;
    public Location locationGPS;
    public LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        myOpenMapView = (MapView)findViewById(R.id.mapSolution);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapView.setUseDataConnection(true);

        GeoPoint location = new GeoPoint(40.37018451188382,-3.6134711039363103);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(18);
        myMapController.animateTo(location);
        myMapController.setCenter(location);
    }
    public void details (View v){
        Intent intent = new Intent(this, DetailsActivity.class);
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
