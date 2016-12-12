package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapsActivity extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;
    private MyLocationNewOverlay myLocationOverlay ;
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        myOpenMapView = (MapView)findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setUseDataConnection(true);
        myOpenMapView.setMultiTouchControls(true);
        //this.myLocationOverlay.enableMyLocation();
        //this.myLocationOverlay.enableFollowLocation();

        GeoPoint location = new GeoPoint(40.37018451188382,-3.6134711039363103);
        //double myLocationLat = myLocation.getLatitude();
        // double myLocationLong = myLocation.getLongitude();
        //GeoPoint startPoint = new GeoPoint(myLocationLat,myLocationLong);
        //GeoPoint myPoint = myLocationOverlay.getMyLocation();
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(18);


        myMapController.animateTo(location);
        myMapController.setCenter(location);
        //myMapController.setCenter(location);


        //important! set your user agent to prevent getting banned from the osm servers

        /*
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        double myLocationLat = map.getLatitudeSpanDouble();
        double myLocationLong = map.getLongitudeSpanDouble();
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(myLocationLat,myLocationLong);
        mapController.setCenter(startPoint);
        */
    }
    public void routes (View v){
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

}
