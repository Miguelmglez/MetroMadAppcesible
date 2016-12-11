package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;

import java.util.ArrayList;


public class RoutesActivity extends AppCompatActivity {
    AutoCompleteTextView text,text1;

    String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};
    ArrayList<String> aux = new ArrayList();
    boolean transbordos = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView6);
        text1= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView7);

        aux.add("Hola");
        aux.add("Adios");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,aux);
        text.setAdapter(adapter);
        text.setThreshold(1);
        text1.setAdapter(adapter2);
        text1.setThreshold(1);
    }
    public void solution (View v){
        String origen = text.getText().toString();
        String destino = text1.getText().toString();
        RadioButton opcionTransbordos = (RadioButton)findViewById(R.id.radioButton);
        RadioButton opcionEstaciones = (RadioButton)findViewById(R.id.radioButton2);
        if (opcionEstaciones.isChecked()){
            this.transbordos = false;
        }
        Intent intent = new Intent(this, SolutionActivity.class);
        startActivity(intent);
    }

}
