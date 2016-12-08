package com.example.miguel.urjctfgappmetromad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }
    public void map (View v){
        setContentView(R.layout.activity_maps);
    }
    public void solution (View v){setContentView(R.layout.activity_solution);}
    public void routes (View v){setContentView(R.layout.activity_routes);}
    public void details (View v){setContentView(R.layout.activity_details);}
    public void dropView (View v){finishActivity(0);}

}
