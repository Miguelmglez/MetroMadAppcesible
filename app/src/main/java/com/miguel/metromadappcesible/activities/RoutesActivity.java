package com.miguel.metromadappcesible.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.miguel.metromadappcesible.code.Estacion;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.miguel.metromadappcesible.activities.IndexActivity.miMetro;

/**
 * Created by Miguel Maroto González on 8-12-16.
 *
 * Clase que representa la actividad que representa la pantalla acrivity_routes.xml
 *
 */

public class RoutesActivity extends AppCompatActivity {
    private AutoCompleteTextView text,text1;
    private ArrayList<String> estacionesMetro = miMetro.getListaNombreEstaciones();
    private static boolean transbordos = true;
    private static String ESTACION_ORIGEN ;
    private static String ESTACION_DESTINO;
    private static Estacion estacionOrigenSeleccionada;
    private static Estacion estacionDestinoSeleccionada;
    private static ArrayList rutaFinal;
    private static Estacion estacionAccesibleOrigen;
    private static Estacion estacionAccesibleDestino;

    public static String getEstacionOrigen() {
        return ESTACION_ORIGEN;
    }

    public static String getEstacionDestino() {
        return ESTACION_DESTINO;
    }

    public static Estacion getEstacionOrigenSeleccionada() {
        return estacionOrigenSeleccionada;
    }

    public static Estacion getEstacionDestinoSeleccionada() {
        return estacionDestinoSeleccionada;
    }

    public static ArrayList getRutaFinal() {
        return rutaFinal;
    }

    public static Estacion getEstacionAccesibleOrigen() {
        return estacionAccesibleOrigen;
    }

    public static Estacion getEstacionAccesibleDestino() {
        return estacionAccesibleDestino;
    }

    /**
     * Método que se ejecuta cuando se crea una instancia de esta actividad.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Button buttonGo = (Button) findViewById(R.id.buttonGo);
        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteOrigin);
        text1= (AutoCompleteTextView)findViewById(R.id.autoCompleteDestination);
        final String hintText = getResources().getString(R.string.example);
        final String hintText2 = getResources().getString(R.string.example2);
        text.setCursorVisible(false);
        text1.setCursorVisible(false);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,estacionesMetro);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,estacionesMetro);
        text.setAdapter(adapter);
        text.setThreshold(1);
        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                text.setCursorVisible(false);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);

            }
        });
        text1.setAdapter(adapter2);
        text1.setThreshold(1);
        text1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                text1.setCursorVisible(false);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });
        buttonGo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button buttonGo = (Button) findViewById(R.id.buttonGo);

                buttonGo.setTextColor(Color.WHITE);
                buttonGo.setBackground(getDrawable(R.drawable.shapeonclick));
                return false;
            }
        });

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                text.setHint("");
                text.setCursorVisible(true);
                text1.setCursorVisible(false);
                text1.setHint(hintText2);
                return false;
            }
        });
        text1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                text1.setHint("");
                text1.setCursorVisible(true);
                text.setCursorVisible(false);
                text.setHint(hintText);
                return false;
            }
        });
    }
    /**
     * Método que calcula la ruta accesible entre las dos estaciones introducidas dependiendo de los criterios de ruta
     * marcados por el usuario.
     */
    public void solution (View v){
        String errorEstaciones = getResources().getString(R.string.errorValidStations);
        String errorDisability = getResources().getString(R.string.errorValidDisability);
        String errorSameStations =getResources().getString(R.string.errorSameStations);
        String errorNoExisteRuta =getResources().getString(R.string.errorNoExisteRuta);
        RadioButton opcionEstaciones = (RadioButton)findViewById(R.id.radioButtonStations);
        ESTACION_ORIGEN = text.getText().toString();
        ESTACION_DESTINO = text1.getText().toString();
        Switch switchBlindness = (Switch) findViewById(R.id.switchBlindness);
        Switch switchDeafness = (Switch) findViewById(R.id.switchDeafness);
        Switch switchWheelChair = (Switch) findViewById(R.id.switchWheelChair);
        Button buttonGo = (Button) findViewById(R.id.buttonGo);
        buttonGo.setBackgroundColor(Color.WHITE);
        buttonGo.setTextColor(getResources().getColor(R.color.colorAccent,null));
        buttonGo.setBackground(getDrawable(R.drawable.shape));
        if (!ESTACION_ORIGEN.equals(ESTACION_DESTINO)){
        if (switchBlindness.isChecked()||switchDeafness.isChecked()||switchWheelChair.isChecked()) {
            if (estacionesMetro.contains(ESTACION_ORIGEN) && (estacionesMetro.contains(ESTACION_DESTINO))) {
                estacionOrigenSeleccionada = miMetro.getMapaEstaciones().get(ESTACION_ORIGEN).get(0);
                estacionDestinoSeleccionada = miMetro.getMapaEstaciones().get(ESTACION_DESTINO).get(0);
                if (opcionEstaciones.isChecked()) {
                    transbordos = false;
                } else {
                    transbordos = true;
                }
                estacionAccesibleOrigen = miMetro.accesibleList(estacionOrigenSeleccionada, new LinkedList(), new ArrayList<Estacion>()).get(0);
                estacionAccesibleDestino = miMetro.accesibleList(estacionDestinoSeleccionada, new LinkedList(), new ArrayList<Estacion>()).get(0);
                rutaFinal = miMetro.calcularCaminoDefinitivo(estacionOrigenSeleccionada, estacionDestinoSeleccionada, transbordos);
                if (rutaFinal.size()!=0) {
                    Intent intent = new Intent(this, SolutionActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RoutesActivity.this, errorNoExisteRuta, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(RoutesActivity.this, errorEstaciones, Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(RoutesActivity.this, errorDisability, Toast.LENGTH_LONG).show();
        }
    }
        else{
            Toast.makeText(RoutesActivity.this, errorSameStations, Toast.LENGTH_LONG).show();
        }
    }
}
