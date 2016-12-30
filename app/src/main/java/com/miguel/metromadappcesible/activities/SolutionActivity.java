package com.miguel.metromadappcesible.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.miguel.metromadappcesible.code.Conexion;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;


import static com.miguel.metromadappcesible.activities.MapsActivity.servicio;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleOrigen;
import static com.miguel.metromadappcesible.activities.RoutesActivity.rutaFinal;

public class SolutionActivity extends AppCompatActivity {
    public static MapView myOpenMapViewMapSolution;

    public Location locationGPS;
    public LocationManager locationManager;
    public static Marker myPositionMarkerMapSolution;
    public static MapController myMapControllerMapSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
        String person = getResources().getString(R.string.textPerson);
        myOpenMapViewMapSolution = (MapView) findViewById(R.id.mapSolution);
        myOpenMapViewMapSolution.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapViewMapSolution.setMultiTouchControls(true);
        myOpenMapViewMapSolution.setUseDataConnection(true);
        myPositionMarkerMapSolution = new Marker(myOpenMapViewMapSolution);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint origen = new GeoPoint(estacionAccesibleOrigen.getLatitud(), estacionAccesibleOrigen.getLongitud());
        myMapControllerMapSolution = (MapController) myOpenMapViewMapSolution.getController();
        myMapControllerMapSolution.setZoom(15);
        myMapControllerMapSolution.animateTo(origen);
        myMapControllerMapSolution.setCenter(origen);
        this.pintaRuta();
        this.pintaEstaciones();
        GeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        startService(servicio);
        myPositionMarkerMapSolution.setPosition(punto);
        myPositionMarkerMapSolution.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        myPositionMarkerMapSolution.setIcon(getResources().getDrawable(R.drawable.person));
        myPositionMarkerMapSolution.setTitle(person);
        myOpenMapViewMapSolution.getOverlays().add(myPositionMarkerMapSolution);

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
                imageButton.setTextColor(Color.WHITE);

                imageButton.setBackground(getDrawable(R.drawable.shapeonclick));

                return false;
            }
        });

    }

    public void details(View v) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
        Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setTextColor(getResources().getColor(R.color.colorAccent));
        imageButton.setBackground(getDrawable(R.drawable.shape));
        stopService(servicio);
    }

    public void locateMe(View v) {
        GeoPoint puntoActual = myPositionMarkerMapSolution.getPosition();
        myMapControllerMapSolution.setZoom(17);
        myMapControllerMapSolution.setCenter(puntoActual);
        myMapControllerMapSolution.animateTo(puntoActual);
        /*
        locationGPS = this.damePuntoNuevo(locationGPS);
        GeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        myMapControllerMapSolution = (MapController) myOpenMapViewMapSolution.getController();
        myPositionMarkerMapSolution.setPosition(punto);
        myMapControllerMapSolution.setZoom(15);
        myMapControllerMapSolution.setCenter(punto);
        myMapControllerMapSolution.animateTo(punto);*/
    }
/*
    public Location damePuntoNuevo(Location puntoAntiguo) {
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
*/
    public void centroMapaOrigen(View v) {
        GeoPoint origen = new GeoPoint(estacionAccesibleOrigen.getLatitud(), estacionAccesibleOrigen.getLongitud());
        myMapControllerMapSolution.setCenter(origen);
        myMapControllerMapSolution.animateTo(origen);
        myMapControllerMapSolution.setZoom(15);
    }

    private void pintaEstaciones() {
        String estacionSinArticulo = getResources().getString(R.string.estacionSinArticulo);
        String lineas = getResources().getString(R.string.infoLine);
        String change = getResources().getString(R.string.changeStation);
        String toLine = getResources().getString(R.string.toLine);
        String reciente = estacionAccesibleOrigen.getNombre();
        String descripcionEstacion;
        Marker estacionOrigen = new Marker(myOpenMapViewMapSolution);
        GeoPoint coordenadasOrigen = new GeoPoint(estacionAccesibleOrigen.getLatitud(), estacionAccesibleOrigen.getLongitud());
        String descripcionOrigen = estacionSinArticulo +" " + estacionAccesibleOrigen.getNombre() + "\n";
        if (estacionAccesibleOrigen.getLinea() == 50) {
            descripcionOrigen = descripcionOrigen + lineas +" : R";
        } else {
            descripcionOrigen = descripcionOrigen + lineas +" : " + estacionAccesibleOrigen.getLinea();
        }
        estacionOrigen.setPosition(coordenadasOrigen);
        estacionOrigen.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        estacionOrigen.setIcon(getResources().getDrawable(R.drawable.metro_mad));
        estacionOrigen.setTitle(descripcionOrigen);
        myOpenMapViewMapSolution.getOverlays().add(estacionOrigen);

        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            Marker estacion = new Marker(myOpenMapViewMapSolution);
            descripcionEstacion = "";
            GeoPoint coordenadasEstacion = new GeoPoint(0.0, 0.0);
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())) {
                Conexion auxAnterior = (Conexion) rutaFinal.get(i - 1);
                Conexion auxSiguiente = (Conexion) rutaFinal.get(i + 1);
                descripcionEstacion = estacionSinArticulo+" " + c.getEstacionOrigen().getNombre().toUpperCase() + "\n";
                descripcionEstacion = descripcionEstacion + "\n" + change +" " + auxAnterior.getEstacionDestino().getLinea() + " "+toLine+" " + auxSiguiente.getEstacionOrigen().getLinea();
                coordenadasEstacion.setCoords(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
            } else {
                if (reciente.equals(c.getEstacionOrigen().getNombre())) {
                    descripcionEstacion = estacionSinArticulo+" " + c.getEstacionDestino().getNombre().toUpperCase() + "\n";
                    coordenadasEstacion.setCoords(c.getEstacionDestino().getLatitud(), c.getEstacionDestino().getLongitud());
                    if (c.getEstacionDestino().getLinea() == 50) {
                        descripcionEstacion = descripcionEstacion + lineas +" : R";
                    } else {
                        descripcionEstacion = descripcionEstacion + lineas +" : " + c.getEstacionDestino().getLinea();
                    }
                    reciente = c.getEstacionDestino().getNombre();

                } else if (reciente.equals(c.getEstacionDestino().getNombre())) {
                    descripcionEstacion = estacionSinArticulo+" " + c.getEstacionOrigen().getNombre().toUpperCase() + "\n";
                    coordenadasEstacion.setCoords(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
                    if (c.getEstacionOrigen().getLinea() == 50) {
                        descripcionEstacion = descripcionEstacion + lineas +" : R";
                    } else {
                        descripcionEstacion = descripcionEstacion + lineas +" : " + c.getEstacionOrigen().getLinea();
                    }
                    reciente = c.getEstacionOrigen().getNombre();
                }
            }
            estacion.setPosition(coordenadasEstacion);
            estacion.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            estacion.setIcon(getResources().getDrawable(R.drawable.metro_mad));
            estacion.setTitle(descripcionEstacion);
            myOpenMapViewMapSolution.getOverlays().add(estacion);
        }
    }

    public void pintaRuta() {
        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            Polyline linea = new Polyline();
            ArrayList<GeoPoint> puntos = new ArrayList();
            GeoPoint punto1 = new GeoPoint(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
            GeoPoint punto2 = new GeoPoint(c.getEstacionDestino().getLatitud(), c.getEstacionDestino().getLongitud());
            if (c.getEstacionOrigen().getNombre().equals(c.getEstacionDestino().getNombre())) {
                Conexion auxAnterior = (Conexion) rutaFinal.get(i-1);
                Conexion auxSiguiente = (Conexion) rutaFinal.get(i+1);
                if (auxAnterior.getEstacionOrigen().getNombre().equals(c.getEstacionOrigen().getNombre())){
                    punto1.setCoords(auxAnterior.getEstacionOrigen().getLatitud(),auxAnterior.getEstacionOrigen().getLongitud());
                }
                else{
                    punto1.setCoords(auxAnterior.getEstacionDestino().getLatitud(),auxAnterior.getEstacionDestino().getLongitud());
                }
                if (auxSiguiente.getEstacionOrigen().getNombre().equals(c.getEstacionOrigen().getNombre())){
                    punto2.setCoords(auxSiguiente.getEstacionDestino().getLatitud(),auxSiguiente.getEstacionDestino().getLongitud());
                }
                else{
                    punto2.setCoords(auxSiguiente.getEstacionOrigen().getLatitud(),auxSiguiente.getEstacionOrigen().getLongitud());
                }
                i++;
            }
            puntos.add(punto1);
            puntos.add(punto2);
            linea.setPoints(puntos);
            linea.setWidth(20);
            linea.setVisible(true);
            linea.setGeodesic(true);
            linea.setColor(Color.RED);
            myOpenMapViewMapSolution.getOverlays().add(linea);
        }
    }
}



