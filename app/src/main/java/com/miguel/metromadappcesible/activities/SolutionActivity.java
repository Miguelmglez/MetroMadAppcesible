package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class SolutionActivity extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        myOpenMapView = (MapView)findViewById(R.id.mapSolution);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint location = new GeoPoint(40.37018451188382,-3.6134711039363103);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(18);
        myOpenMapView.setUseDataConnection(true);
        myMapController.animateTo(location);
        myOpenMapView.setMultiTouchControls(true);
        myMapController.setCenter(location);
    }
    public void details (View v){
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }
}
