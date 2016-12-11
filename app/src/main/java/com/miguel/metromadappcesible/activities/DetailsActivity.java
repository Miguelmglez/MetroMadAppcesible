package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
    public void routes (View v){
        setContentView(R.layout.activity_solution);
    }
    public void map (View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void dropView (View v){
        this.finish();
    }

}
