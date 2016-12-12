package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.miguel.metromadappcesible.code.Estacion;

import java.util.ArrayList;

import static com.miguel.metromadappcesible.activities.IndexActivity.miMetro;


public class RoutesActivity extends AppCompatActivity {
    public static AutoCompleteTextView text,text1;
    ArrayList<String> estacionesMetro = miMetro.getListaNombreEstaciones();
    public static boolean transbordos = true;
    public static String ESTACION_ORIGEN ;
    public static String ESTACION_DESTINO;
    public Estacion estacionOrigenSeleccionada;
    public Estacion estacionDestinoSeleccionada;
    public static ArrayList rutaFinal;
    public static Estacion estacionAccesibleOrigen;
    public static Estacion estacionAccesibleDestino;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView6);
        text1= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView7);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,estacionesMetro);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,estacionesMetro);
        text.setAdapter(adapter);
        text.setThreshold(1);
        text.setInputType(InputType.TYPE_NULL);
        text1.setAdapter(adapter2);
        text1.setThreshold(1);
        text1.setInputType(InputType.TYPE_NULL);
    }
    public void solution (View v){
        RadioButton opcionEstaciones = (RadioButton)findViewById(R.id.radioButton2);
        ESTACION_ORIGEN = text.getText().toString();
        ESTACION_DESTINO = text1.getText().toString();
        if (estacionesMetro.contains(ESTACION_ORIGEN) && (estacionesMetro.contains(ESTACION_DESTINO))) {
            estacionOrigenSeleccionada = miMetro.getMapaEstaciones().get(ESTACION_ORIGEN).get(0);
            estacionDestinoSeleccionada = miMetro.getMapaEstaciones().get(ESTACION_DESTINO).get(0);
            if (opcionEstaciones.isChecked()) {
                this.transbordos = false;
            } else {
                this.transbordos = true;
            }
            estacionAccesibleOrigen = miMetro.listaAccesibles(estacionOrigenSeleccionada,new ArrayList<Estacion>()).get(0);
            estacionAccesibleDestino = miMetro.listaAccesibles(estacionDestinoSeleccionada,new ArrayList<Estacion>()).get(0);
            rutaFinal = miMetro.calcularCaminoDefinitivo(estacionOrigenSeleccionada,estacionDestinoSeleccionada,transbordos);
            Intent intent = new Intent(this, SolutionActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(RoutesActivity.this,"You must choose two valid stations",Toast.LENGTH_LONG).show();
        }
    }




}
