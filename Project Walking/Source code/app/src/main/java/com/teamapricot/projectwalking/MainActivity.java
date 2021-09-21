package com.teamapricot.projectwalking;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {
    LocationHandler locationHandler;
    IMapController mapController;
    MyLocationNewOverlay locationOverlay;

    boolean mapInitialized = false;

    MapView map = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        locationHandler = new LocationHandler(this, 2000);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapController = map.getController();
        mapController.setZoom(19.5);

        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        locationOverlay.enableMyLocation();
        map.getOverlays().add(locationOverlay);

        locationHandler.registerUpdateListener(position -> {
            GeoPoint point = new GeoPoint(position.getLatitude(), position.getLongitude());
            if(!mapInitialized) {
                mapController.setCenter(point);
                mapInitialized = true;
            }
        });
    }

    public void onResume() {
        super.onResume();

        map.onResume();
    }

    public void onPause() {
        super.onPause();

        map.onPause();
    }
}
