package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
    }
    private File createFileFromInputStream(InputStream inputStream) {
        try{
            File f = new File("/mnt/sdcard/metro.xml");
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

}
