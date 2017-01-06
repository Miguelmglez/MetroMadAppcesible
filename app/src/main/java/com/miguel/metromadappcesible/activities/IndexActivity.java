package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.miguel.metromadappcesible.code.Metro;
import com.miguel.metromadappcesible.code.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
/**
 * Created by Miguel Maroto González on 8-12-16.
 *
 * Clase que representa la actividad que representa la pantalla acrivity_index.xml
 *
 */
public class IndexActivity extends AppCompatActivity {
    public static Metro miMetro;
    public IndexActivity() throws IOException {
    }

    /**
     * Método que se ejecuta cuando se crea una instancia de esta actividad.
     *
     * Crea un objeto tipo Metro a partir de llamar a Parser.java
     *
     * Comprueba si la aplicación tiene permisos de localización. En caso de no tenerlos los solicita.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Button open = (Button) findViewById(R.id.buttonOpen);
        AssetManager am = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("metro.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = createFileFromInputStream(inputStream);
            Parser parser = new Parser();
            miMetro = parser.parsearMetro(file);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {

        }
        open.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button open= (Button) findViewById(R.id.buttonOpen);
                open.setTextColor(Color.WHITE);
                open.setBackground(getDrawable(R.drawable.shapeonclick));
                return false;
            }
        });
    }
    /**
     * Método auxiliar que crea el archivo metro.xml a partir del archivo /assets/metro.xml y lo almacena en el dispositivo.
     */
    private File createFileFromInputStream(InputStream inputStream) {
        try{
            File f = new File(getFilesDir(),"metro.xml");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }
            outputStream.close();
            inputStream.close();
            return f;
        }catch (IOException e) {

        }
        return null;
    }
    /**
     * Método que se ejecuta para crear una instancia de la clase MapsActivity.

     */
    public void map (View v){
        Intent intent = new Intent(IndexActivity.this, MapsActivity.class);
        startActivity(intent);
        Button open= (Button) findViewById(R.id.buttonOpen);
        open.setTextColor(getResources().getColor(R.color.colorAccent,null));
        open.setBackground(getDrawable(R.drawable.shape));
    }
    /**
     * Método que comprueba si se ha concedido el permiso de localización y lo vuelve a pedir en caso de haberse concedido
     * lanzando un mensaje Toast al usuario.
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        String permission = getResources().getString(R.string.permission);
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(IndexActivity.this, permission, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return;
            }


        }
    }

    @Override
    public void onBackPressed(){
        Intent thisIntent = this.getIntent();
        thisIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

}
