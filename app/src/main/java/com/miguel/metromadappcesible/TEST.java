package com.miguel.metromadappcesible;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.miguel.metromadappcesible.activities.R;

public class TEST extends AppCompatActivity {
    AutoCompleteTextView text,text1;
    String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView6);
        text1= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView7);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);
        text.setAdapter(adapter);
        text.setThreshold(1);
        text1.setAdapter(adapter);
        text1.setThreshold(1);
    }
}
