package com.miguel.metromadappcesible.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.miguel.metromadappcesible.activities.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button open = (Button) findViewById(R.id.buttonOpenMain);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {
            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
            startActivity(intent);
            this.finish();
        }
        open.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button open= (Button) findViewById(R.id.buttonOpenMain);
                open.setTextColor(Color.WHITE);
                open.setBackground(getDrawable(R.drawable.shapeonclick));
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        String permission = getResources().getString(R.string.permission);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(MainActivity.this, permission, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }
                return;
            }
        }
    }
    public void map (View v){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
        this.finish();
    }
}
