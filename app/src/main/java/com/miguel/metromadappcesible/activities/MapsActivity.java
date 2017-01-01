package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


import com.miguel.metromadappcesible.LocationListenerService;
import com.miguel.metromadappcesible.code.Estacion;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.Marker;



import java.util.ArrayList;

import static com.miguel.metromadappcesible.activities.IndexActivity.miMetro;


public class MapsActivity extends AppCompatActivity {

    public Location locationGPS;
    public LocationManager locationManager;
    public static MapView myOpenMapViewMap;
    public static MapController myMapControllerMap;
    public static Marker myPositionMarkerMap;
    private AutoCompleteTextView textEstacion;
    public static ArrayList<String> estacionesMetro = miMetro.getListaNombreEstaciones();
    public static Intent servicio;
    GeoPoint punto = new GeoPoint(40.41694,-3.70361);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String person = getResources().getString(R.string.textPerson);
        Button imageButton = (Button) findViewById(R.id.findRouteButton);
        myOpenMapViewMap = (MapView) findViewById(R.id.map);
        myOpenMapViewMap.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapViewMap.setUseDataConnection(true);
        myOpenMapViewMap.setMultiTouchControls(true);

        myPositionMarkerMap = new Marker(myOpenMapViewMap);
        servicio = new Intent(this,LocationListenerService.class);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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
        this.pintaEstaciones();
        startService(servicio);


        locationGPS = this.damePuntoNuevo(locationGPS);

        try {
            punto.setCoords(locationGPS.getLatitude(), locationGPS.getLongitude());
        }catch (NullPointerException e){
        }
        myMapControllerMap = (MapController) myOpenMapViewMap.getController();
        myMapControllerMap.setZoom(16);
        myMapControllerMap.setCenter(punto);
        myMapControllerMap.animateTo(punto);
        myPositionMarkerMap.setPosition(punto);
        myPositionMarkerMap.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        myPositionMarkerMap.setIcon(getResources().getDrawable(R.drawable.person));
        myPositionMarkerMap.setTitle(person);
        myOpenMapViewMap.getOverlays().add(myPositionMarkerMap);


        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.findRouteButton);

                imageButton.setTextColor(Color.WHITE);
                imageButton.setBackground(getDrawable(R.drawable.shapeonclick));

                return false;
            }
        });

    }

    public void routes(View v) {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
        Button imageButton = (Button) findViewById(R.id.findRouteButton);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setTextColor(getResources().getColor(R.color.colorAccent));
        imageButton.setBackground(getDrawable(R.drawable.shape));
        stopService(servicio);
    }

    public void locateMe(View v) {
        GeoPoint puntoActual = myPositionMarkerMap.getPosition();
        myMapControllerMap.setZoom(17);
        myMapControllerMap.setCenter(puntoActual);
        myMapControllerMap.animateTo(puntoActual);
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
            Marker estacion = new Marker(myOpenMapViewMap);
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
            estacion.setIcon(getResources().getDrawable(R.drawable.locationsign64));
            estacion.setTitle(descripcionEstacion);
            myOpenMapViewMap.getOverlays().add(estacion);
        }
    }

    private void focusOnStation(AdapterView.OnItemClickListener v, String estacion) {
        Estacion estacionSeleccionada = miMetro.getMapaEstaciones().get(estacion).get(0);
        GeoPoint coordenadasEstacionSeleccionada = new GeoPoint(estacionSeleccionada.getLatitud(), estacionSeleccionada.getLongitud());
        myMapControllerMap.setCenter(coordenadasEstacionSeleccionada);
        myMapControllerMap.animateTo(coordenadasEstacionSeleccionada);
        myMapControllerMap.setZoom(18);
    }

}
