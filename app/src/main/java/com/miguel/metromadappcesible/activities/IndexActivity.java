package com.miguel.metromadappcesible.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class IndexActivity extends AppCompatActivity {
    public static Metro miMetro;
    public IndexActivity() throws IOException {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
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
            Intent intent = new Intent(IndexActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }
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
            //Logging exception
        }
        return null;
    }
    public void map (View v){
        Intent intent = new Intent(IndexActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(IndexActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(IndexActivity.this, "Metro Madrid Appcesible needs this permission to work.", Toast.LENGTH_LONG).show();

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
