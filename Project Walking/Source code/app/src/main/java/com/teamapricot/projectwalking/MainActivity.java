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

import android.view.View;

import com.teamapricot.projectwalking.handlers.CameraHandler;
import com.teamapricot.projectwalking.photos.PhotoController;

public class MainActivity extends AppCompatActivity {
    private PhotoController photoController;
    private LocationHandler locationHandler;
    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;

    boolean mapInitialized = false;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionHandler permissionHandler = new PermissionHandler(this);
        CameraHandler cameraHandler = new CameraHandler(this);

        photoController = new PhotoController(this, permissionHandler, cameraHandler);

        locationHandler = new LocationHandler(this, 2000);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

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

    @Override
    protected void onResume() {
        super.onResume();

        map.onResume();
    }

    public void onPause() {
        super.onPause();

        map.onPause();
    }

    public void captureImage(View view) {
        photoController.captureImageAsync();
    }
}
