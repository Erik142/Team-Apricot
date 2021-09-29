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
import android.widget.Toast;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.Reminder;
import com.teamapricot.projectwalking.controller.NavigationController;
import com.teamapricot.projectwalking.controller.CameraController;
import com.teamapricot.projectwalking.model.CaptureImageModel;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.observe.Observer;

public class MainActivity extends AppCompatActivity {
    private NavigationController navigationController;
    private CameraController cameraController;

    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;

    boolean mapCentered;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

        createChannel();
        Reminder GetNotified = new Reminder(MainActivity.this);
        GetNotified.addNotification("new_spot", "new_challenge", "notify_message", 1);
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
        initNavigation();
        initCamera();
    }

    private void initCamera() {
        cameraController = new CameraController(this);

        View openCameraButton = findViewById(R.id.open_camera_fab);

        cameraController.registerOnClickListener(openCameraButton);
        cameraController.registerObserver(createCameraObserver());
    }

    private void initNavigation() {
        mapCentered = false;

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        navigationController = new NavigationController(this);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapController = map.getController();

        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        locationOverlay.enableMyLocation();

        map.getOverlays().add(locationOverlay);

        navigationController.registerObserver(createNavigationObserver());
        navigationController.start();
    }

    /**
     * Creates a new {@code Observer} object for the Camera functionality
     * @return
     */
    private Observer<CaptureImageModel> createCameraObserver() {
        return model -> {
            if (model.isFinished()) {
                if (model.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast toast = Toast.makeText(this, "Nice photo!", Toast.LENGTH_LONG);
                        toast.show();
                    });
                }
            }
        };
    }

    /**
     * Creates a new {@code Observer} object for the navigation functionality
     * @return An {@code Observer<NavigationModel>} object to observe changes in a {@code NavigationModel} and update the UI correspondingly
     */
    private Observer<NavigationModel> createNavigationObserver() {
        return model -> {
            mapController.setZoom(model.getZoomLevel());

            GeoPoint location = model.getUserLocation();

            if (!mapCentered && location != null) {
                mapController.setCenter(location);
                mapCentered = true;
            }
        };
    }
}
