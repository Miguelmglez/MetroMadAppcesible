package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import android.widget.TextView;

import com.miguel.metromadappcesible.code.Conexion;

import static com.miguel.metromadappcesible.activities.RoutesActivity.ESTACION_DESTINO;
import static com.miguel.metromadappcesible.activities.RoutesActivity.ESTACION_ORIGEN;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleOrigen;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionDestinoSeleccionada;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionOrigenSeleccionada;
import static com.miguel.metromadappcesible.activities.RoutesActivity.rutaFinal;


public class DetailsActivity extends AppCompatActivity {
    public int transbordos =0 ;
    public String reciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView textView = (TextView) findViewById(R.id.textRuta);
        TextView textViewBetween = (TextView) findViewById(R.id.textViewBetween2Stations);
        TextView textAccesible = (TextView) findViewById(R.id.textViewIsAccesible);

        textView.setMovementMethod(new ScrollingMovementMethod());
        String infoLinea = getResources().getString(R.string.infoLine);
        String noAccesible = getResources().getString(R.string.noAccesible);
        String estacionStr = getResources().getString(R.string.estacion);
        String inicio = getResources().getString(R.string.inicio);
        String transbordo = getResources().getString(R.string.transbordo);
        String totalEstaciones = getResources().getString(R.string.totalEstaciones);
        String totalTransbordos = getResources().getString(R.string.totalTransbordos);
        textViewBetween.append("\n");
        textViewBetween.append(ESTACION_ORIGEN.toUpperCase() + " - " + ESTACION_DESTINO.toUpperCase());
        textViewBetween.append("\n");
        if (!(estacionOrigenSeleccionada.isAccesible())){
            textAccesible.append(estacionStr + " " + ESTACION_ORIGEN + " " + noAccesible);
            textAccesible.append("\n");
        }
        if (!(estacionDestinoSeleccionada.isAccesible())){
            textAccesible.append("\n");
            textAccesible.append(estacionStr + " " + ESTACION_DESTINO + " " + noAccesible);
            textAccesible.append("\n");
        }
        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            if (i>=1){
                textView.append("\n");
            }
            else{
                textView.append(inicio);
                textView.append("\n");
                textView.append("\n");
                if ((c.getEstacionDestino().getLinea()== 50)){
                    textView.append(infoLinea +" R:  ");
                }
                else {
                    textView.append(infoLinea + " " + c.getEstacionDestino().getLinea()+":  ");
                }
                textView.append(estacionAccesibleOrigen.getNombre());
                textView.append("\n");
                textView.append("\n");
                reciente = estacionAccesibleOrigen.getNombre();
            }
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())) {
                textView.append(transbordo + " " + c.getEstacionDestino().getNombre());
                this.transbordos++;
                reciente = c.getEstacionDestino().getNombre();
                textView.append("\n");
            } else {
                if (reciente.equals(c.getEstacionOrigen().getNombre())){
                    if ((c.getEstacionDestino().getLinea()== 50)){
                        textView.append(infoLinea + " R:  ");
                    }
                    else{
                    textView.append(infoLinea + " "  + c.getEstacionDestino().getLinea()+":  ");
                    }
                    textView.append(c.getEstacionDestino().getNombre());
                    textView.append("\n");
                    reciente = c.getEstacionDestino().getNombre();
                }
                else if (reciente.equals(c.getEstacionDestino().getNombre())){
                    if ((c.getEstacionOrigen().getLinea()== 50)){
                        textView.append(infoLinea + " R:  ");
                    }
                    else{
                        textView.append(infoLinea + " " + c.getEstacionOrigen().getLinea()+ ":  ");
                    }
                    textView.append(c.getEstacionOrigen().getNombre());
                    textView.append("\n");
                    reciente = c.getEstacionOrigen().getNombre();
                }
            }
        }
        textView.append("\n");
        textView.append(totalEstaciones + " " + (rutaFinal.size()));
        textView.append("\n");
        textView.append(totalTransbordos + " " + transbordos);
        textView.append("\n");
    }
    public void routes (View v){
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }
    public void map (View v){
        this.finish();
    }


}
