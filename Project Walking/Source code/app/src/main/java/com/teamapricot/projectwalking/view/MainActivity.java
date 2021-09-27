package com.teamapricot.projectwalking.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
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

import com.teamapricot.projectwalking.LocationHandler;
import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.Reminder;
import com.teamapricot.projectwalking.controller.PhotoController;

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


        locationHandler = new LocationHandler(this, 2000);
        init();

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);
        createChannel();
        Reminder GetNotified = new Reminder(MainActivity.this);
        GetNotified.addNotification("new_spot", "new_challenge", "notify_message", 1);

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

    /**
     * description: creating the notification_channel for higher versions
     */
    public void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_message", "new_spot", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        map.onPause();
    }

    private void init() {
        photoController = new PhotoController(this);

        View openCameraButton = findViewById(R.id.open_camera_fab);

        photoController.registerOnClickListener(openCameraButton);
    }
}
