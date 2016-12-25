package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miguel.metromadappcesible.code.Conexion;

import static com.miguel.metromadappcesible.activities.RoutesActivity.ESTACION_DESTINO;
import static com.miguel.metromadappcesible.activities.RoutesActivity.ESTACION_ORIGEN;
import static com.miguel.metromadappcesible.activities.RoutesActivity.estacionAccesibleOrigen;
import static com.miguel.metromadappcesible.activities.RoutesActivity.rutaFinal;


public class DetailsActivity extends AppCompatActivity {
    public int transbordos =0 ;
    public String reciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView textView = (TextView) findViewById(R.id.textRuta);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setTextColor(Color.BLACK);
        textView.setText("Accesible route between:");
        textView.append("\n");
        textView.append(ESTACION_ORIGEN.toUpperCase() + " and " + ESTACION_DESTINO.toUpperCase());
        textView.append("\n");
        if (!(((Conexion)rutaFinal.get(0)).getEstacionOrigen().getNombre().equals(ESTACION_ORIGEN) || ((Conexion)rutaFinal.get(0)).getEstacionDestino().getNombre().equals(ESTACION_ORIGEN))){
            textView.append("\n");
            textView.append("Station " + ESTACION_ORIGEN + " is NOT accesible. Route has been recalculated with the nearest station.");
            textView.append("\n");
        }
        if (!(((Conexion)rutaFinal.get(rutaFinal.size()-1)).getEstacionOrigen().getNombre().equals(ESTACION_DESTINO) || ((Conexion)rutaFinal.get(rutaFinal.size()-1)).getEstacionDestino().getNombre().equals(ESTACION_DESTINO))){
            textView.append("\n");
            textView.append("Station " + ESTACION_DESTINO + " is NOT accesible. Route has been recalculated with the nearest station.");
            textView.append("\n");
        }
        for (int i = 0; i < rutaFinal.size(); i++) {
            Conexion c = (Conexion) rutaFinal.get(i);
            if (i>=1){
                textView.append("\n");
            }
            else{
                textView.append("\n");
                textView.append("Start:");
                textView.append("\n");
                textView.append("\n");
                if ((c.getEstacionDestino().getLinea()== 50)){
                    textView.append("Line: R");
                }
                else {
                    textView.append("Line: " + c.getEstacionDestino().getLinea());
                }
                textView.append("  "+estacionAccesibleOrigen.getNombre());
                textView.append("\n");
                textView.append("\n");
                reciente = estacionAccesibleOrigen.getNombre();
            }
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())) {
                textView.append("CHANGE IN: " + c.getEstacionDestino().getNombre());
                this.transbordos++;
                reciente = c.getEstacionDestino().getNombre();
                textView.append("\n");
            } else {
                if (reciente.equals(c.getEstacionOrigen().getNombre())){
                    if ((c.getEstacionDestino().getLinea()== 50)){
                        textView.append("Line: R");
                    }
                    else{
                    textView.append("Line: "+ c.getEstacionDestino().getLinea());
                    }
                    textView.append("  "+ c.getEstacionDestino().getNombre());
                    textView.append("\n");
                    reciente = c.getEstacionDestino().getNombre();
                }
                else if (reciente.equals(c.getEstacionDestino().getNombre())){
                    if ((c.getEstacionOrigen().getLinea()== 50)){
                        textView.append("Line: R");
                    }
                    else{
                        textView.append("Line: "+ c.getEstacionOrigen().getLinea());
                    }
                    textView.append("  "+c.getEstacionOrigen().getNombre());
                    textView.append("\n");
                    reciente = c.getEstacionOrigen().getNombre();
                }
            }
        }
        textView.append("\n");
        textView.append("Total stations:  " + (rutaFinal.size()));
        textView.append("\n");
        textView.append("Total changes:  " + transbordos);
        textView.append("\n");
    }
    public void routes (View v){
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }
    public void map (View v){
        this.finish();
    }
    public void dropView (View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
