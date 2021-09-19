package com.teamapricot.projectwalking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LocationHandler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHandler = new LocationHandler(this, 2000);

        locationHandler.registerUpdateListener(() -> {
            ((TextView)findViewById(R.id.helloWorld))
                    .setText("(" + locationHandler.getLatitude() + "," + locationHandler.getLongitude() + ")");
        });
    }
}