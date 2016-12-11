package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.miguel.metromadappcesible.code.Metro;

import java.io.File;

public class IndexActivity extends AppCompatActivity {
    public static Metro miMetro = new Metro();
    File inputFile = new File("/mnt/sdcard/metro.xml");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
     //   this.miMetro = this.miMetro.nuevoMetroMadrid(inputFile, miMetro);

    }
    public void map (View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
