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

        locationHandler.registerUpdateListener(position -> {
            ((TextView)findViewById(R.id.helloWorld))
                    .setText("(" + position.getLatitude() + "," + position.getLongitude() + ")");
        });
    }
}