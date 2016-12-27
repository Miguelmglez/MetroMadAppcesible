package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.miguel.metromadappcesible.code.Estacion;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.Marker;



import java.util.ArrayList;
import java.util.Locale;

import static com.miguel.metromadappcesible.activities.IndexActivity.miMetro;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleOrigen;


public class MapsActivity extends AppCompatActivity {

    public Location locationGPS;
    public LocationManager locationManager;
    private MapView myOpenMapView;
    private MapController myMapController;
    private Marker myPositionMarker;
    private AutoCompleteTextView textEstacion;
    public static ArrayList<String> estacionesMetro = miMetro.getListaNombreEstaciones();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String person = getResources().getString(R.string.textPerson);
        Button imageButton = (Button) findViewById(R.id.findRouteButton);
        imageButton.setBackgroundColor(Color.GREEN);
        textEstacion = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewLookStation);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, estacionesMetro);
        textEstacion.setAdapter(adapter);
        textEstacion.setThreshold(1);
        textEstacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                String estacion = textEstacion.getText().toString();
                focusOnStation(this, estacion);
            }
        });
        myOpenMapView = (MapView) findViewById(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setUseDataConnection(true);
        myOpenMapView.setMultiTouchControls(true);
        this.pintaEstaciones();
        myPositionMarker = new Marker(myOpenMapView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                GeoPoint changedPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                myPositionMarker.setPosition(changedPoint);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, locationListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationGPS = this.damePuntoNuevo(locationGPS);
        GeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(17);
        myMapController.setCenter(punto);
        myMapController.animateTo(punto);


        myPositionMarker.setPosition(punto);
        myPositionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        myPositionMarker.setIcon(getResources().getDrawable(R.drawable.person));
        myPositionMarker.setTitle(person);
        myOpenMapView.getOverlays().add(myPositionMarker);


        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.findRouteButton);
                imageButton.setBackgroundColor(Color.RED);
                return false;
            }
        });

    }

    public void routes(View v) {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
        Button imageButton = (Button) findViewById(R.id.findRouteButton);
        imageButton.setBackgroundColor(Color.GREEN);
    }

    public void locateMe(View v) {
        locationGPS = this.damePuntoNuevo(locationGPS);
        GeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(17);
        myMapController.setCenter(punto);
        myMapController.animateTo(punto);
        myPositionMarker.setPosition(punto);
    }

    private Location damePuntoNuevo(Location puntoAntiguo) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return puntoAntiguo;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return puntoAntiguo;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, new LocationListener() {
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

    private void pintaEstaciones() {
        String lineas = getResources().getString(R.string.infoLine);
        String estacionNoAccesible = getResources().getString(R.string.estacionNoAccesible);
        String estacionAccesible = getResources().getString(R.string.estacionAccesible);
        String estacionSinArticulo = getResources().getString(R.string.estacionSinArticulo);
        for (int i = 0; i < miMetro.getListaNombreEstaciones().size(); i++) {
            Marker estacion = new Marker(myOpenMapView);
            String nombreEstacion = miMetro.getListaNombreEstaciones().get(i);
            ArrayList<Estacion> correspondencias = miMetro.getMapaEstaciones().get(nombreEstacion);
            GeoPoint coordenadasEstacion = new GeoPoint(correspondencias.get(0).getLatitud(), correspondencias.get(0).getLongitud());
            String descripcionEstacion = estacionSinArticulo+ " " + nombreEstacion.toUpperCase() + "\n" + "\n" + lineas +" : ";
            descripcionEstacion = descripcionEstacion + "\n";
            for (int j = 0; j < correspondencias.size(); j++) {
                if (j == 0) {
                } else {
                    descripcionEstacion = descripcionEstacion + " - ";
                }
                if (correspondencias.get(j).getLinea() == 50) {
                    descripcionEstacion = descripcionEstacion + "R";
                } else {
                    descripcionEstacion = descripcionEstacion + String.valueOf(correspondencias.get(j).getLinea());

                }
                if (correspondencias.get(j).isAccesible()) {
                    descripcionEstacion = descripcionEstacion + " "+estacionAccesible+" ";
                } else {
                    descripcionEstacion = descripcionEstacion + " "+estacionNoAccesible+" ";
                }
            }
            estacion.setPosition(coordenadasEstacion);
            estacion.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            estacion.setIcon(getResources().getDrawable(R.drawable.metro_mad));
            estacion.setTitle(descripcionEstacion);
            myOpenMapView.getOverlays().add(estacion);
        }
    }

    private void focusOnStation(AdapterView.OnItemClickListener v, String estacion) {
        Estacion estacionSeleccionada = miMetro.getMapaEstaciones().get(estacion).get(0);
        GeoPoint coordenadasEstacionSeleccionada = new GeoPoint(estacionSeleccionada.getLatitud(), estacionSeleccionada.getLongitud());
        myMapController.setCenter(coordenadasEstacionSeleccionada);
        myMapController.animateTo(coordenadasEstacionSeleccionada);
    }


}
