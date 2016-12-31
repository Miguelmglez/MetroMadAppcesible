package com.miguel.metromadappcesible.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.miguel.metromadappcesible.code.Estacion;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.miguel.metromadappcesible.activities.IndexActivity.miMetro;


public class RoutesActivity extends AppCompatActivity {
    public static AutoCompleteTextView text,text1;
    ArrayList<String> estacionesMetro = miMetro.getListaNombreEstaciones();
    public static boolean transbordos = true;
    public static String ESTACION_ORIGEN ;
    public static String ESTACION_DESTINO;
    public static Estacion estacionOrigenSeleccionada;
    public static Estacion estacionDestinoSeleccionada;
    public static ArrayList rutaFinal;
    public static Estacion estacionAccesibleOrigen;
    public static Estacion estacionAccesibleDestino;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteOrigin);
        text1= (AutoCompleteTextView)findViewById(R.id.autoCompleteDestination);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,estacionesMetro);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,estacionesMetro);
        text.setAdapter(adapter);
        text.setThreshold(1);
        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });
        text1.setAdapter(adapter2);
        text1.setThreshold(1);
        text1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });
    }
    public void solution (View v){
        String errorEstaciones = getResources().getString(R.string.errorValidStations);
        String errorDisability = getResources().getString(R.string.errorValidDisability);
        RadioButton opcionEstaciones = (RadioButton)findViewById(R.id.radioButtonStations);
        ESTACION_ORIGEN = text.getText().toString();
        ESTACION_DESTINO = text1.getText().toString();
        Switch switchBlindness = (Switch) findViewById(R.id.switchBlindness);
        Switch switchDeafness = (Switch) findViewById(R.id.switchDeafness);
        Switch switchWheelChair = (Switch) findViewById(R.id.switchWheelChair);
        if (switchBlindness.isChecked()||switchDeafness.isChecked()||switchWheelChair.isChecked()) {
            if (estacionesMetro.contains(ESTACION_ORIGEN) && (estacionesMetro.contains(ESTACION_DESTINO))) {
                estacionOrigenSeleccionada = miMetro.getMapaEstaciones().get(ESTACION_ORIGEN).get(0);
                estacionDestinoSeleccionada = miMetro.getMapaEstaciones().get(ESTACION_DESTINO).get(0);
                if (opcionEstaciones.isChecked()) {
                    this.transbordos = false;
                } else {
                    this.transbordos = true;
                }
                estacionAccesibleOrigen = miMetro.accesibleList(estacionOrigenSeleccionada, new LinkedList(), new ArrayList<Estacion>()).get(0);
                estacionAccesibleDestino = miMetro.accesibleList(estacionDestinoSeleccionada, new LinkedList(), new ArrayList<Estacion>()).get(0);
                rutaFinal = miMetro.calcularCaminoDefinitivo(estacionOrigenSeleccionada, estacionDestinoSeleccionada, transbordos);
                Intent intent = new Intent(this, SolutionActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(RoutesActivity.this, errorEstaciones, Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(RoutesActivity.this, errorDisability, Toast.LENGTH_LONG).show();
        }
    }
    public void changeColorSwitchBlindness(View v){
        Switch switchBlindness = (Switch) findViewById(R.id.switchBlindness);
        if (switchBlindness.isChecked()){

        }

    }
    public void changeColorSwitchWheelChair(View v){
        Switch switchWheelChair = (Switch) findViewById(R.id.switchWheelChair);

    }
    public void changeColorSwitchDeafness(View v){
        Switch switchDeafness = (Switch) findViewById(R.id.switchDeafness);

    }
}
