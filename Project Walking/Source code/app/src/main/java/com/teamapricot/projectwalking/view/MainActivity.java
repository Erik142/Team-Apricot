package com.teamapricot.projectwalking.view;

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

import com.teamapricot.projectwalking.LocationHandler;
import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.NotificationController;
import com.teamapricot.projectwalking.controller.PhotoController;
import com.teamapricot.projectwalking.model.CaptureImageModel;
import com.teamapricot.projectwalking.observe.Observer;

public class MainActivity extends AppCompatActivity {
    private PhotoController photoController;

    private LocationHandler locationHandler;
    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;
    private NotificationController notificationController;

    boolean mapInitialized = false;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


        notificationController = new NotificationController(ctx);

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

    @Override
    protected void onPause() {
        super.onPause();

        map.onPause();
    }

    private void init() {
        photoController = new PhotoController(this);
        locationHandler = new LocationHandler(this, 2000);
       notificationController = new NotificationController(getApplicationContext());


        View openCameraButton = findViewById(R.id.open_camera_fab);

        photoController.registerOnClickListener(openCameraButton);
        photoController.registerObserver(createCameraObserver());
        notificationController.SendNotification(false);
    }

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
}
