package com.miguel.metromadappcesible.activities;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.miguel.metromadappcesible.LocationListenerService;
import com.miguel.metromadappcesible.code.Estacion;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;


import static com.miguel.metromadappcesible.activities.IndexActivity.miMetro;


/**
 * Created by Miguel Maroto González on 8-12-16.
 *
 * Clase que representa la actividad que representa la pantalla acrivity_maps.xml
 *
 */
public class MapsActivity extends AppCompatActivity {

    private Location locationGPS;
    private LocationManager locationManager;
    public static MapView myOpenMapViewMap;
    private static MapController myMapControllerMap;
    public static Marker myPositionMarkerMap;
    private AutoCompleteTextView textEstacion;
    private static ArrayList<String> estacionesMetro = miMetro.getListaNombreEstaciones();
    private Intent servicio;
    private GeoPoint punto = new GeoPoint(40.41694,-3.70361);

    /**
     * Método que se ejecuta cuando se crea una instancia de esta actividad.
     *
     * Inicializa el mapa
     * Inicializa el servicio de localización
     * Pinta las estaciones en el mapa
     * Pinta el icono con la posición del usuario
     * Añade la funcionalidad al campo superior para localizar una estación en el mapa
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String person = getResources().getString(R.string.textPerson);
        final String hintText = getResources().getString(R.string.lookStation);
        Button imageButton = (Button) findViewById(R.id.findRouteButton);
        ImageButton locationButton = (ImageButton) findViewById(R.id.locationButton);
        locationButton.setImageDrawable(getDrawable(R.drawable.position));
        myOpenMapViewMap = (MapView) findViewById(R.id.map);
        myOpenMapViewMap.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapViewMap.setUseDataConnection(true);
        myOpenMapViewMap.setMultiTouchControls(true);

        myPositionMarkerMap = new Marker(myOpenMapViewMap);
        servicio = new Intent(this, LocationListenerService.class);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        textEstacion = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewLookStation);
        textEstacion.setCursorVisible(false);
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
                textEstacion.setCursorVisible(false);
            }
        });
        this.pintaEstaciones();
        startService(servicio);


        locationGPS = this.damePuntoNuevo();

        try {
            punto.setCoords(locationGPS.getLatitude(), locationGPS.getLongitude());
        } catch (NullPointerException e) {
        }
        myMapControllerMap = (MapController) myOpenMapViewMap.getController();
        myMapControllerMap.setZoom(16);
        myMapControllerMap.setCenter(punto);
        myMapControllerMap.animateTo(punto);
        myPositionMarkerMap.setPosition(punto);
        myPositionMarkerMap.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        myPositionMarkerMap.setIcon(getResources().getDrawable(R.drawable.person_ball, null));
        myPositionMarkerMap.setTitle(person);
        myOpenMapViewMap.getOverlays().add(myPositionMarkerMap);


        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.findRouteButton);
                imageButton.setTextColor(Color.WHITE);
                imageButton.setBackground(getDrawable(R.drawable.shapeonclick));
                textEstacion.setHint(hintText);
                textEstacion.setCursorVisible(false);
                return false;
            }
        });
        locationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton locationButton = (ImageButton) findViewById(R.id.locationButton);
                locationButton.setImageDrawable(getDrawable(R.drawable.position_pressed));
                textEstacion.setHint(hintText);
                textEstacion.setCursorVisible(false);
                return false;
            }
        });

        textEstacion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textEstacion.setHint("");
                textEstacion.setCursorVisible(true);
                return false;
            }
        });
        myOpenMapViewMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textEstacion.setHint(hintText);
                textEstacion.setCursorVisible(false);
                return false;
            }
        });
    }
    /**
     * Método que se ejecuta para crear una instancia de la clase RoutesActivity.
     *
     * Para el servicio de localización.
     */
    public void routes(View v) {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);

        Button imageButton = (Button) findViewById(R.id.findRouteButton);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setTextColor(getResources().getColor(R.color.colorAccent,null));
        imageButton.setBackground(getDrawable(R.drawable.shape));
        stopService(servicio);
    }
    /**
     * Método que se ejecuta para posicionar en el centro del mapa la ubicación del usuario
     */
    public void locateMe(View v) {
        GeoPoint puntoActual = myPositionMarkerMap.getPosition();
        myMapControllerMap.setZoom(17);
        myMapControllerMap.setCenter(puntoActual);
        myMapControllerMap.animateTo(puntoActual);
        ImageButton locationButton = (ImageButton) findViewById(R.id.locationButton);
        locationButton.setImageDrawable(getDrawable(R.drawable.position));
    }
    /**
     * Método auxiliar para tomar el primer valor de la localización del usuario
     */
    private Location damePuntoNuevo() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, new LocationListener() {
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
    /**
     * Método auxiliar para pintar las estaciones en el mapa, añadiendo su descripción al icono.
     */
    private void pintaEstaciones() {
        String lineas = getResources().getString(R.string.infoLine);
        String estacionNoAccesible = getResources().getString(R.string.estacionNoAccesible);
        String estacionAccesible = getResources().getString(R.string.estacionAccesible);
        String estacionSinArticulo = getResources().getString(R.string.estacionSinArticulo);

        for (int i = 0; i < miMetro.getListaNombreEstaciones().size(); i++) {
            final Marker estacion = new Marker(myOpenMapViewMap);
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
            estacion.setIcon(getResources().getDrawable(R.drawable.estacion,null));
            estacion.setTitle(descripcionEstacion);
            myOpenMapViewMap.getOverlays().add(estacion);
        }
    }
    /**
     * Método para posicionar el centro del mapa en la estación introducida por el usuario.
     */
    private void focusOnStation(AdapterView.OnItemClickListener v, String estacion) {
        Estacion estacionSeleccionada = miMetro.getMapaEstaciones().get(estacion).get(0);
        GeoPoint coordenadasEstacionSeleccionada = new GeoPoint(estacionSeleccionada.getLatitud(), estacionSeleccionada.getLongitud());
        myMapControllerMap.setCenter(coordenadasEstacionSeleccionada);
        myMapControllerMap.animateTo(coordenadasEstacionSeleccionada);
        myMapControllerMap.setZoom(18);
    }
}
