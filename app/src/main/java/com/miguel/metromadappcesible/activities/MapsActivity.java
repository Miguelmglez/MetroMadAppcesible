package com.miguel.metromadappcesible.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

public class MapsActivity extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        myOpenMapView = (MapView)findViewById(R.id.map);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);

        GeoPoint location = new GeoPoint(40.37018451188382,-3.6134711039363103);

        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(15);
        myOpenMapView.setUseDataConnection(true);
        myMapController.animateTo(location);
        myOpenMapView.setMultiTouchControls(true);
        myMapController.setCenter(location);

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

}
