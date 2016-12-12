package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.miguel.metromadappcesible.code.Metro;
import com.miguel.metromadappcesible.code.Parser;

import java.io.File;

public class IndexActivity extends AppCompatActivity {


    public static File xml = new File("mnt/sdcard/metro.xml");
    public static Parser parser = new Parser();
    public static Metro miMetro = parser.parsearMetro(xml);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

    }
    public void map (View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
