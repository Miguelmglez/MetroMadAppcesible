package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.miguel.metromadappcesible.code.Conexion;
import com.miguel.metromadappcesible.code.Estacion;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;


import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleOrigen;
import static com.miguel.metromadappcesible.activities.RoutesActivity.rutaFinal;

public class SolutionActivity extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;
    public Location locationGPS;
    public LocationManager locationManager;
    private LocationListener locationListener;
    private Marker myPositionMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
        imageButton.setBackgroundColor(Color.CYAN);
        myOpenMapView = (MapView) findViewById(R.id.mapSolution);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapView.setUseDataConnection(true);
        locationGPS = this.damePuntoNuevo(locationGPS);
        GeoPoint origen = new GeoPoint(estacionAccesibleOrigen.getLatitud(), estacionAccesibleOrigen.getLongitud());
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(15);
        myMapController.animateTo(origen);
        myMapController.setCenter(origen);
        this.pintaRuta();
        this.pintaEstaciones();
        GeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        myPositionMarker = new Marker(myOpenMapView);
        myPositionMarker.setPosition(punto);
        myPositionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        myPositionMarker.setIcon(getResources().getDrawable(R.drawable.person));
        myPositionMarker.setTitle("Hey! You are here!");
        myOpenMapView.getOverlays().add(myPositionMarker);

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
                imageButton.setBackgroundColor(Color.RED);
                return false;
            }
        });

    }

    public void details(View v) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
        Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
        imageButton.setBackgroundColor(Color.CYAN);
    }

    public void locateMe(View v) {
        locationGPS = this.damePuntoNuevo(locationGPS);
        IGeoPoint punto = new GeoPoint(locationGPS.getLatitude(), locationGPS.getLongitude());
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(15);
        myMapController.setCenter(punto);
        myMapController.animateTo(punto);
    }

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

    public void centroMapaOrigen(View v) {
        GeoPoint origen = new GeoPoint(estacionAccesibleOrigen.getLatitud(), estacionAccesibleOrigen.getLongitud());
        myMapController.setCenter(origen);
        myMapController.animateTo(origen);
    }

    private void pintaEstaciones() {
        String reciente = estacionAccesibleOrigen.getNombre();
        String descripcionEstacion;
        Marker estacionOrigen = new Marker(myOpenMapView);
        GeoPoint coordenadasOrigen = new GeoPoint(estacionAccesibleOrigen.getLatitud(), estacionAccesibleOrigen.getLongitud());
        String descripcionOrigen = "Station : " + estacionAccesibleOrigen.getNombre() + "\n";
        if (estacionAccesibleOrigen.getLinea() == 50) {
            descripcionOrigen = descripcionOrigen + "Line: R";
        } else {
            descripcionOrigen = descripcionOrigen + "Line: " + estacionAccesibleOrigen.getLinea();
        }
        estacionOrigen.setPosition(coordenadasOrigen);
        estacionOrigen.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        estacionOrigen.setIcon(getResources().getDrawable(R.drawable.metro_mad));
        estacionOrigen.setTitle(descripcionOrigen);
        myOpenMapView.getOverlays().add(estacionOrigen);

        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            Marker estacion = new Marker(myOpenMapView);
            descripcionEstacion = "";
            GeoPoint coordenadasEstacion = new GeoPoint(0.0, 0.0);
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())) {
                Conexion auxAnterior = (Conexion) rutaFinal.get(i - 1);
                Conexion auxSiguiente = (Conexion) rutaFinal.get(i + 1);
                descripcionEstacion = "Station : " + c.getEstacionOrigen().getNombre().toUpperCase() + "\n";
                descripcionEstacion = descripcionEstacion + "\n" + "Change from line " + auxAnterior.getEstacionDestino().getLinea() + " to line " + auxSiguiente.getEstacionOrigen().getLinea();
                coordenadasEstacion.setCoords(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
            } else {
                if (reciente.equals(c.getEstacionOrigen().getNombre())) {
                    descripcionEstacion = "Station : " + c.getEstacionDestino().getNombre().toUpperCase() + "\n";
                    coordenadasEstacion.setCoords(c.getEstacionDestino().getLatitud(), c.getEstacionDestino().getLongitud());
                    if (c.getEstacionDestino().getLinea() == 50) {
                        descripcionEstacion = descripcionEstacion + "Line: R";
                    } else {
                        descripcionEstacion = descripcionEstacion + "Line: " + c.getEstacionDestino().getLinea();
                    }
                    reciente = c.getEstacionDestino().getNombre();

                } else if (reciente.equals(c.getEstacionDestino().getNombre())) {
                    descripcionEstacion = "Station : " + c.getEstacionOrigen().getNombre().toUpperCase() + "\n";
                    coordenadasEstacion.setCoords(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
                    if (c.getEstacionOrigen().getLinea() == 50) {
                        descripcionEstacion = descripcionEstacion + "Line: R";
                    } else {
                        descripcionEstacion = descripcionEstacion + "Line: " + c.getEstacionOrigen().getLinea();
                    }
                    reciente = c.getEstacionOrigen().getNombre();
                }
            }
            estacion.setPosition(coordenadasEstacion);
            estacion.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            estacion.setIcon(getResources().getDrawable(R.drawable.metro_mad));
            estacion.setTitle(descripcionEstacion);
            myOpenMapView.getOverlays().add(estacion);
        }
    }

    public void pintaRuta() {
        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            Polyline linea = new Polyline();
            ArrayList<GeoPoint> puntos = new ArrayList();
            if (c.getEstacionOrigen().getNombre().equals(c.getEstacionDestino().getNombre())) {
                linea.setColor(Color.RED);
                linea.setTitle("CHANGE");
                linea.setEnabled(true);
            } else {
                linea.setColor(Color.GREEN);
            }
            GeoPoint punto1 = new GeoPoint(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
            GeoPoint punto2 = new GeoPoint(c.getEstacionDestino().getLatitud(), c.getEstacionDestino().getLongitud());
            puntos.add(punto1);
            puntos.add(punto2);
            linea.setPoints(puntos);
            linea.setWidth(20);
            linea.setVisible(true);
            linea.setGeodesic(true);
            myOpenMapView.getOverlays().add(linea);
        }
    }
}



