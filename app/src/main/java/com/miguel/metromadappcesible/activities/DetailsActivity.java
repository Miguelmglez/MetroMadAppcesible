package com.miguel.metromadappcesible.activities;


import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.miguel.metromadappcesible.code.Conexion;
import com.miguel.metromadappcesible.code.Estacion;
import com.miguel.metromadappcesible.code.EstacionConIcono;

import java.util.ArrayList;
import java.util.List;

import static com.miguel.metromadappcesible.activities.RoutesActivity.ESTACION_DESTINO;
import static com.miguel.metromadappcesible.activities.RoutesActivity.ESTACION_ORIGEN;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleDestino;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleOrigen;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionDestinoSeleccionada;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionOrigenSeleccionada;
import static com.miguel.metromadappcesible.activities.RoutesActivity.rutaFinal;

/**
 * Created by Miguel Maroto González on 8-12-16.
 *
 * Clase que representa la actividad que representa la pantalla acrivity_details.xml
 *
 */
public class DetailsActivity extends AppCompatActivity {

    public String reciente;
    /**
     * Método que se ejecuta cuando se crea una instancia de esta actividad.
     *
     * Escribe la información relativa a la ruta:
     * Nombre de la ruta
     * Información sobre estaciones y transbordos
     * Información sobre accesibilidad de las estaciones seleccionadas
     * Rellena la lista con objetos EstacionConIcono de las estaciones de la ruta calculada
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int transbordos =0 ;
        TextView textViewBetween = (TextView) findViewById(R.id.textViewBetween2Stations);
        TextView textAccesible = (TextView) findViewById(R.id.textViewIsAccesible);
        ArrayList<EstacionConIcono> listaEstaciones = new ArrayList<>();
        ListView lista = (ListView) findViewById(R.id.listEstaciones);
        TextView infoRuta = (TextView) findViewById(R.id.infoRoute);


        String noAccesible = getResources().getString(R.string.noAccesible);
        String totalEstaciones = getResources().getString(R.string.totalEstaciones);
        String totalTransbordos = getResources().getString(R.string.totalTransbordos);
        String nombreEstacion;
        Drawable iconoEstacion;

        textViewBetween.append(ESTACION_ORIGEN.toUpperCase() + " - " + ESTACION_DESTINO.toUpperCase());
        if (!(estacionOrigenSeleccionada.isAccesible())){
            textAccesible.append("\n");
            textAccesible.append(ESTACION_ORIGEN + " " + noAccesible + " "+ estacionAccesibleOrigen.getNombre()+".");
            textAccesible.append("\n");
        }
        if (!(estacionDestinoSeleccionada.isAccesible())){
            textAccesible.append("\n");
            textAccesible.append(ESTACION_DESTINO + " " + noAccesible + " "+ estacionAccesibleDestino.getNombre()+".");
            textAccesible.append("\n");
        }


        nombreEstacion = estacionAccesibleOrigen.getNombre();
        reciente = estacionAccesibleOrigen.getNombre();
        Conexion aux = (Conexion) rutaFinal.get(0);
        if (aux.getEstacionOrigen().equals(estacionAccesibleOrigen.getNombre())){
            iconoEstacion = dameIconoLinea(aux.getEstacionOrigen());
        }
        else{
            iconoEstacion = dameIconoLinea(aux.getEstacionDestino());
        }
        EstacionConIcono estacionOrigen = new EstacionConIcono(iconoEstacion, nombreEstacion);
        listaEstaciones.add(estacionOrigen);
        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            Boolean eraTransbordo = false;
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())) {
                transbordos++;
                reciente = c.getEstacionDestino().getNombre();
                nombreEstacion = c.getEstacionDestino().getNombre();
                iconoEstacion = getDrawable(R.drawable.trans);
                eraTransbordo = true;
            } else {
                if (reciente.equals(c.getEstacionOrigen().getNombre())) {
                    reciente = c.getEstacionDestino().getNombre();
                    nombreEstacion = c.getEstacionDestino().getNombre();
                    iconoEstacion = dameIconoLinea(c.getEstacionDestino());
                } else if (reciente.equals(c.getEstacionDestino().getNombre())) {
                    reciente = c.getEstacionOrigen().getNombre();
                    nombreEstacion = c.getEstacionOrigen().getNombre();
                    iconoEstacion = dameIconoLinea(c.getEstacionOrigen());
                }
            }
            EstacionConIcono estacion = new EstacionConIcono(iconoEstacion, nombreEstacion);
            listaEstaciones.add(estacion);
            if (eraTransbordo){
                Conexion conexionAux = (Conexion) rutaFinal.get(i+1);
                if (reciente.equals(conexionAux.getEstacionOrigen().getNombre())){
                    iconoEstacion = dameIconoLinea(conexionAux.getEstacionOrigen());
                    nombreEstacion = reciente;
                }
                if (reciente.equals(conexionAux.getEstacionDestino().getNombre())){
                    iconoEstacion = dameIconoLinea(conexionAux.getEstacionDestino());
                    nombreEstacion = reciente;
                }
                EstacionConIcono estacionSiguiente = new EstacionConIcono(iconoEstacion, nombreEstacion);
                listaEstaciones.add(estacionSiguiente);
            }
        }
        ListAdapter adapter = new ListAdapter(this, R.layout.row, listaEstaciones);
        lista.setAdapter(adapter);

        infoRuta.setText(totalEstaciones + " " + (listaEstaciones.size()-transbordos)+ "    "+totalTransbordos + " " + transbordos);
        infoRuta.append("\n");


        Button imageButton = (Button) findViewById(R.id.buttonNewRoute);

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button imageButton = (Button) findViewById(R.id.buttonNewRoute);

                imageButton.setTextColor(Color.WHITE);
                imageButton.setBackground(getDrawable(R.drawable.shapeonclick));

                return false;
            }
        });
    }
    /**
     * Método que se ejecuta para crear una instancia de la clase MapsActivity.
     */
    public void map (View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        Button imageButton = (Button) findViewById(R.id.buttonNewRoute);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setTextColor(getResources().getColor(R.color.colorAccent,null));
        imageButton.setBackground(getDrawable(R.drawable.shape));
    }
    /**
     * Método auxiliar para pintar la lista de estaciones de manera customizada.
     */
 public class ListAdapter extends ArrayAdapter<EstacionConIcono> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<EstacionConIcono> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row, null);
        }

        EstacionConIcono p = getItem(position);

        if (p != null) {
            TextView textRow = (TextView) v.findViewById(R.id.estacion);
            ImageView imageRow = (ImageView) v.findViewById(R.id.iconoEstacion);
            if (textRow != null) {
                textRow.setText(p.getDescripcionEstacion());
            }
            if (imageRow != null) {
                imageRow.setImageDrawable(p.getIconoLinea());
            }
        }
        return v;
    }
}

    /**
     * Método auxiliar que devuelve el icono a pintar según la línea de la estación introducida.
     */
    public Drawable dameIconoLinea (Estacion e){
        Drawable icono  = getResources().getDrawable(R.drawable.estacioncirculo,null) ;
        switch (e.getLinea()){
            case 1:
                icono = getResources().getDrawable(R.drawable.linea1,null) ;
                break;
            case 2:
                icono = getResources().getDrawable(R.drawable.linea2,null) ;
                break;
            case 3:
                icono = getResources().getDrawable(R.drawable.linea3,null) ;
                break;
            case 4:
                icono = getResources().getDrawable(R.drawable.linea4,null) ;
                break;
            case 5:
                icono = getResources().getDrawable(R.drawable.linea5,null) ;
                break;
            case 6:
                icono = getResources().getDrawable(R.drawable.linea6,null) ;
                break;
            case 7:
                icono = getResources().getDrawable(R.drawable.linea7,null) ;
                break;
            case 8:
                icono = getResources().getDrawable(R.drawable.linea8,null) ;
                break;
            case 9:
                icono = getResources().getDrawable(R.drawable.linea9,null) ;
                break;
            case 10:
                icono = getResources().getDrawable(R.drawable.linea10,null) ;
                break;
            case 11:
                icono = getResources().getDrawable(R.drawable.linea11,null) ;
                break;
            case 12:
                icono = getResources().getDrawable(R.drawable.linea12,null) ;
                break;
            case 50:
                icono = getResources().getDrawable(R.drawable.linea_r,null) ;
                break;
        }
        return icono;
    }
}

