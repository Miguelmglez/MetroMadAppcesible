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
import android.widget.ImageButton;

import com.miguel.metromadappcesible.LocationListenerService;
import com.miguel.metromadappcesible.code.Conexion;
import com.miguel.metromadappcesible.code.Estacion;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;



import static com.miguel.metromadappcesible.activities.RoutesActivity.getEstacionAccesibleDestino;
import static com.miguel.metromadappcesible.activities.RoutesActivity.getEstacionAccesibleOrigen;
import static com.miguel.metromadappcesible.activities.RoutesActivity.getRutaFinal;


/**
 * Created by Miguel Maroto González on 8-12-16.
 *
 * Clase que representa la actividad que representa la pantalla acrivity_solution.xml
 *
 */
public class SolutionActivity extends AppCompatActivity {
    public static MapView myOpenMapViewMapSolution;
    private Location locationGPS;
    private LocationManager locationManager;
    public static Marker myPositionMarkerMapSolution;
    private static MapController myMapControllerMapSolution;
    private GeoPoint punto = new GeoPoint(40.41694,-3.70361);
    private Intent servicio;
    @Override
    /**
     * Método que se ejecuta cuando se crea una instancia de esta actividad.
     *
     * Inicializa el mapa
     * Inicializa el servicio de localización
     * Pinta las estaciones de la ruta calculada en el mapa
     * Pinta el icono con la posición del usuario
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
        String person = getResources().getString(R.string.textPerson);
        ImageButton positionButton = (ImageButton) findViewById(R.id.locateMeButton);
        ImageButton stationButton = (ImageButton) findViewById(R.id.locationStationButton);
        positionButton.setImageDrawable(getDrawable(R.drawable.position));
        stationButton.setImageDrawable(getDrawable(R.drawable.position_station_1));
        servicio = new Intent(this,LocationListenerService.class);
        startService(servicio);
        myOpenMapViewMapSolution = (MapView) findViewById(R.id.mapSolution);
        myOpenMapViewMapSolution.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapViewMapSolution.setMultiTouchControls(true);
        myOpenMapViewMapSolution.setUseDataConnection(true);
        myPositionMarkerMapSolution = new Marker(myOpenMapViewMapSolution);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint origen = new GeoPoint(getEstacionAccesibleOrigen().getLatitud(), getEstacionAccesibleOrigen().getLongitud());
        myMapControllerMapSolution = (MapController) myOpenMapViewMapSolution.getController();
        myMapControllerMapSolution.setZoom(15);
        myMapControllerMapSolution.animateTo(origen);
        myMapControllerMapSolution.setCenter(origen);
        this.pintaRuta();
        this.pintaEstaciones();


        myPositionMarkerMapSolution.setPosition(punto);
        myPositionMarkerMapSolution.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        myPositionMarkerMapSolution.setIcon(getResources().getDrawable(R.drawable.person_ball,null));
        myPositionMarkerMapSolution.setTitle(person);
        myOpenMapViewMapSolution.getOverlays().add(myPositionMarkerMapSolution);

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
                imageButton.setTextColor(Color.WHITE);
                imageButton.setBackground(getDrawable(R.drawable.shapeonclick));
                ImageButton positionButton = (ImageButton) findViewById(R.id.locateMeButton);
                ImageButton stationButton = (ImageButton) findViewById(R.id.locationStationButton);
                positionButton.setImageDrawable(getDrawable(R.drawable.position));
                stationButton.setImageDrawable(getDrawable(R.drawable.position_station_1));
                return false;
            }
        });
        myOpenMapViewMapSolution.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton positionButton = (ImageButton) findViewById(R.id.locateMeButton);
                ImageButton stationButton = (ImageButton) findViewById(R.id.locationStationButton);
                positionButton.setImageDrawable(getDrawable(R.drawable.position));
                stationButton.setImageDrawable(getDrawable(R.drawable.position_station_1));
                return false;
            }
        });

    }
    /**
     * Método que se ejecuta para crear una instancia de la clase DetailsActivity.
     *
     * Para el servicio de localización.
     */
    public void details(View v) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
        Button imageButton = (Button) findViewById(R.id.routeDetailsButton);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setTextColor(getResources().getColor(R.color.colorAccent,null));
        imageButton.setBackground(getDrawable(R.drawable.shape));
        stopService(servicio);
    }
    /**
     * Método que se ejecuta para posicionar en el centro del mapa la ubicación del usuario
     */
    public void locateMe(View v) {
        GeoPoint puntoActual = myPositionMarkerMapSolution.getPosition();
        myMapControllerMapSolution.setZoom(17);
        myMapControllerMapSolution.setCenter(puntoActual);
        myMapControllerMapSolution.animateTo(puntoActual);
        ImageButton positionButton = (ImageButton) findViewById(R.id.locateMeButton);
        ImageButton stationButton = (ImageButton) findViewById(R.id.locationStationButton);
        positionButton.setImageDrawable(getDrawable(R.drawable.position_pressed));
        stationButton.setImageDrawable(getDrawable(R.drawable.position_station_1));

    }
    /**
     * Método para posicionar el centro del mapa en la estación origen de la ruta.
     */
    public void centroMapaOrigen(View v) {
        GeoPoint origen = new GeoPoint(getEstacionAccesibleOrigen().getLatitud(), getEstacionAccesibleOrigen().getLongitud());
        myMapControllerMapSolution.setCenter(origen);
        myMapControllerMapSolution.animateTo(origen);
        myMapControllerMapSolution.setZoom(15);
        ImageButton positionButton = (ImageButton) findViewById(R.id.locateMeButton);
        ImageButton stationButton = (ImageButton) findViewById(R.id.locationStationButton);
        positionButton.setImageDrawable(getDrawable(R.drawable.position));
        stationButton.setImageDrawable(getDrawable(R.drawable.position_station_pressed));
    }

    /**
     * Método auxiliar para pintar las estaciones de la ruta en el mapa, añadiendo su descripción al icono.
     */
    private void pintaEstaciones() {
        String estacionSinArticulo = getResources().getString(R.string.estacionSinArticulo);
        String lineas = getResources().getString(R.string.infoLine);
        String change = getResources().getString(R.string.changeStation);
        String toLine = getResources().getString(R.string.toLine);
        Conexion auxConexion = (Conexion) getRutaFinal().get(0);
        String reciente = getEstacionAccesibleOrigen().getNombre();
        String descripcionEstacion;
        Marker estacionOrigen = new Marker(myOpenMapViewMapSolution);
        GeoPoint coordenadasOrigen = new GeoPoint(getEstacionAccesibleOrigen().getLatitud(), getEstacionAccesibleOrigen().getLongitud());
        String descripcionOrigen = estacionSinArticulo +" " + getEstacionAccesibleOrigen().getNombre() + "\n";
        if (auxConexion.getEstacionOrigen().getNombre().equals(reciente)) {

            if (auxConexion.getEstacionOrigen().getLinea() == 50) {
                descripcionOrigen = descripcionOrigen + "\n" + lineas + " : R";
            } else {
                descripcionOrigen = descripcionOrigen + "\n" + lineas + " : " + auxConexion.getEstacionOrigen().getLinea();
            }
        }
        else{
            if (auxConexion.getEstacionDestino().getLinea() == 50) {
                descripcionOrigen = descripcionOrigen + "\n" + lineas + " : R";
            } else {
                descripcionOrigen = descripcionOrigen + "\n" + lineas + " : " + auxConexion.getEstacionDestino().getLinea();
            }
        }
        estacionOrigen.setPosition(coordenadasOrigen);
        estacionOrigen.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        estacionOrigen.setIcon(getResources().getDrawable(R.drawable.estacion,null));
        estacionOrigen.setTitle(descripcionOrigen);
        myOpenMapViewMapSolution.getOverlays().add(estacionOrigen);
        Marker aux = estacionOrigen;
        for (int i = 0; i < getRutaFinal().size(); i++) {
            Conexion c = (Conexion) getRutaFinal().get(i);
            Marker estacion = new Marker(myOpenMapViewMapSolution);
            descripcionEstacion = "";
            GeoPoint coordenadasEstacion = new GeoPoint(0.0, 0.0);
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())) {
                Conexion auxAnterior = (Conexion) getRutaFinal().get(i - 1);
                Conexion auxSiguiente = (Conexion) getRutaFinal().get(i + 1);
                descripcionEstacion = estacionSinArticulo+" " + c.getEstacionOrigen().getNombre().toUpperCase() + "\n";
                if (auxAnterior.getEstacionDestino().getLinea()!=50 && auxSiguiente.getEstacionOrigen().getLinea()!=50) {
                    descripcionEstacion = descripcionEstacion + "\n" + change + " " + auxAnterior.getEstacionDestino().getLinea() + " " + toLine + " " + auxSiguiente.getEstacionOrigen().getLinea();
                }
                else {
                    if (auxAnterior.getEstacionDestino().getLinea()==50){
                        descripcionEstacion = descripcionEstacion + "\n" + change + " " + "R" + " " + toLine + " " + auxSiguiente.getEstacionOrigen().getLinea();
                    }
                    if (auxSiguiente.getEstacionOrigen().getLinea()==50){
                        descripcionEstacion = descripcionEstacion + "\n" + change + " " + auxAnterior.getEstacionDestino().getLinea() + " " + toLine + " " + "R";
                    }
                }
                    coordenadasEstacion.setCoords(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
                if (aux.getPosition()==coordenadasEstacion) {
                    myOpenMapViewMapSolution.getOverlays().remove(aux);
                }
            } else {
                if (reciente.equals(c.getEstacionOrigen().getNombre())) {
                    descripcionEstacion = estacionSinArticulo+" " + c.getEstacionDestino().getNombre().toUpperCase() + "\n";
                    coordenadasEstacion.setCoords(c.getEstacionDestino().getLatitud(), c.getEstacionDestino().getLongitud());
                    if (c.getEstacionDestino().getLinea() == 50) {
                        descripcionEstacion = descripcionEstacion +"\n" + lineas +" : R";
                    } else {
                        descripcionEstacion = descripcionEstacion +"\n" + lineas +" : " + c.getEstacionDestino().getLinea();
                    }
                    reciente = c.getEstacionDestino().getNombre();

                } else if (reciente.equals(c.getEstacionDestino().getNombre())) {
                    descripcionEstacion = estacionSinArticulo+" " + c.getEstacionOrigen().getNombre().toUpperCase() + "\n";
                    coordenadasEstacion.setCoords(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
                    if (c.getEstacionOrigen().getLinea() == 50) {
                        descripcionEstacion = descripcionEstacion +"\n"+ lineas +" : R";
                    } else {
                        descripcionEstacion = descripcionEstacion +"\n"+ lineas +" : " + c.getEstacionOrigen().getLinea();
                    }
                    reciente = c.getEstacionOrigen().getNombre();
                }
            }
            estacion.setPosition(coordenadasEstacion);
            estacion.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            if (reciente.equals(getEstacionAccesibleDestino().getNombre())){
                estacion.setIcon(getResources().getDrawable(R.drawable.estacion, null));
            }
            else {
                estacion.setIcon(getResources().getDrawable(R.drawable.estacioncirculo, null));
            }
            estacion.setTitle(descripcionEstacion);
            myOpenMapViewMapSolution.getOverlays().add(estacion);
            aux = estacion;
        }
    }
    /**
     * Método auxiliar para pintar las líneas que conectan las estaciones de la ruta según el color de la línea.
     */
    private void pintaRuta() {
        for (int i = 0; i < getRutaFinal().size(); i++) {
            Conexion c = (Conexion) getRutaFinal().get(i);
            Polyline linea = new Polyline();
            ArrayList<GeoPoint> puntos = new ArrayList();
            GeoPoint punto1 = new GeoPoint(c.getEstacionOrigen().getLatitud(), c.getEstacionOrigen().getLongitud());
            GeoPoint punto2 = new GeoPoint(c.getEstacionDestino().getLatitud(), c.getEstacionDestino().getLongitud());
            if (c.getEstacionOrigen().getNombre().equals(c.getEstacionDestino().getNombre())) {
                Conexion auxAnterior = (Conexion) getRutaFinal().get(i-1);
                Conexion auxSiguiente = (Conexion) getRutaFinal().get(i+1);
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
            linea.setWidth(15);
            linea.setVisible(true);
            if (c.getEstacionOrigen().getNombre().equals(c.getEstacionDestino().getNombre())){
               Estacion aux = ((Conexion) getRutaFinal().get(i)).getEstacionOrigen();
                switch (aux.getLinea()){
                    case 1:
                        linea.setColor(getResources().getColor(R.color.colorLinea1,null));
                        break;
                    case 2:
                        linea.setColor(getResources().getColor(R.color.colorLinea2,null));
                        break;
                    case 3:
                        linea.setColor(getResources().getColor(R.color.colorLinea3,null));
                        break;
                    case 4:
                        linea.setColor(getResources().getColor(R.color.colorLinea4,null));
                        break;
                    case 5:
                        linea.setColor(getResources().getColor(R.color.colorLinea5,null));
                        break;
                    case 6:
                        linea.setColor(getResources().getColor(R.color.colorLinea6,null));
                        break;
                    case 7:
                        linea.setColor(getResources().getColor(R.color.colorLinea7,null));
                        break;
                    case 8:
                        linea.setColor(getResources().getColor(R.color.colorLinea8,null));
                        break;
                    case 9:
                        linea.setColor(getResources().getColor(R.color.colorLinea9,null));
                        break;
                    case 10:
                        linea.setColor(getResources().getColor(R.color.colorLinea10,null));
                        break;
                    case 11:
                        linea.setColor(getResources().getColor(R.color.colorLinea11,null));
                        break;
                    case 12:
                        linea.setColor(getResources().getColor(R.color.colorLinea12,null));
                        break;
                    case 50:
                        linea.setColor(getResources().getColor(R.color.colorLineaR,null));
                        break;
                }
            }
            else {
                switch (c.getEstacionOrigen().getLinea()) {
                    case 1:
                        linea.setColor(getResources().getColor(R.color.colorLinea1, null));
                        break;
                    case 2:
                        linea.setColor(getResources().getColor(R.color.colorLinea2, null));
                        break;
                    case 3:
                        linea.setColor(getResources().getColor(R.color.colorLinea3, null));
                        break;
                    case 4:
                        linea.setColor(getResources().getColor(R.color.colorLinea4, null));
                        break;
                    case 5:
                        linea.setColor(getResources().getColor(R.color.colorLinea5, null));
                        break;
                    case 6:
                        linea.setColor(getResources().getColor(R.color.colorLinea6, null));
                        break;
                    case 7:
                        linea.setColor(getResources().getColor(R.color.colorLinea7, null));
                        break;
                    case 8:
                        linea.setColor(getResources().getColor(R.color.colorLinea8, null));
                        break;
                    case 9:
                        linea.setColor(getResources().getColor(R.color.colorLinea9, null));
                        break;
                    case 10:
                        linea.setColor(getResources().getColor(R.color.colorLinea10, null));
                        break;
                    case 11:
                        linea.setColor(getResources().getColor(R.color.colorLinea11, null));
                        break;
                    case 12:
                        linea.setColor(getResources().getColor(R.color.colorLinea12, null));
                        break;
                    case 50:
                        linea.setColor(getResources().getColor(R.color.colorLineaR, null));
                        break;
                }
            }
            myOpenMapViewMapSolution.getOverlays().add(linea);
        }
    }


    }




