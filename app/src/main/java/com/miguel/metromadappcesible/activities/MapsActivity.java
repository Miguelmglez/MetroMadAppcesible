package com.miguel.metromadappcesible.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapsActivity extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;
    private MyLocationNewOverlay mLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final Context context = this.getBaseContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        myOpenMapView = (MapView)findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setUseDataConnection(true);
        myOpenMapView.setMultiTouchControls(true);
        mRotationGestureOverlay = new RotationGestureOverlay(myOpenMapView);
        mRotationGestureOverlay.setEnabled(true);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), myOpenMapView);
        mScaleBarOverlay = new ScaleBarOverlay(myOpenMapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mRotationGestureOverlay = new RotationGestureOverlay(myOpenMapView);
        mRotationGestureOverlay.setEnabled(true);
        myOpenMapView.getOverlays().add(this.mLocationOverlay);
        mLocationOverlay.enableMyLocation();

        //this.myLocationOverlay.enableMyLocation();
        //this.myLocationOverlay.enableFollowLocation();

        //GeoPoint location = new GeoPoint(40.37018451188382,-3.6134711039363103);
        //double myLocationLat = myLocation.getLatitude();
        // double myLocationLong = myLocation.getLongitude();
        //GeoPoint startPoint = new GeoPoint(myLocationLat,myLocationLong);
        //GeoPoint myPoint = myLocationOverlay.getMyLocation();
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(18);


        //myMapController.animateTo(location);
        //myMapController.setCenter(location);
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
