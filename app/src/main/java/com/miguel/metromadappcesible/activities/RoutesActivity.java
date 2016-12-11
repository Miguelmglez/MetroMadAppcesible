package com.miguel.metromadappcesible.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


public class RoutesActivity extends AppCompatActivity {
    public EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        editText = (EditText)findViewById(R.id.autoCompleteTextView6);

    }
    public void solution (View v){
        Intent intent = new Intent(this, SolutionActivity.class);
        startActivity(intent);
    }

}
