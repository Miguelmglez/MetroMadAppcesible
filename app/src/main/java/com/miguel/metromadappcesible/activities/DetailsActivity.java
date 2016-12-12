package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        String estacionOrigen = RoutesActivity.ESTACION_ORIGEN;
        String estacionDestino = RoutesActivity.ESTACION_DESTINO;
        TextView textView = (TextView) findViewById(R.id.textRuta);
        textView.setText(estacionOrigen);
        textView.setText ("\n");
        textView.setText (estacionDestino);

    }
    public void routes (View v){
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }
    public void map (View v){
        setContentView(R.layout.activity_solution);
    }
    public void dropView (View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);


    }

}
