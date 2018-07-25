package com.veeradash.justvish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PlaceInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        TextView tv = ((TextView)findViewById(R.id.address));
        tv.setText(getIntent().getStringExtra("Address"));
    }
}
