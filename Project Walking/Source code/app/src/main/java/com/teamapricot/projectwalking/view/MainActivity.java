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
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.view.View;
import android.widget.Toast;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.NavigationController;
import com.teamapricot.projectwalking.controller.CameraController;
import com.teamapricot.projectwalking.model.CameraModel;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.controller.NotificationController;
import com.teamapricot.projectwalking.observe.Observer;

public class MainActivity extends AppCompatActivity {
    private NavigationController navigationController;
    private CameraController cameraController;

    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;
    private NotificationController notificationController;

    boolean mapCentered;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
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
        notificationController = new NotificationController(getApplicationContext());
        notificationController.SendNotification(false);
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
    private Observer<CameraModel> createCameraObserver() {
        return model -> {
            this.runOnUiThread(() -> {
                String toastMessage = null;

                switch (model.getStatus()) {
                    case Done:
                        toastMessage = "Nice photo!";
                        break;
                    case ErrorSavingFinalPhoto:
                        toastMessage = "An error occurred while copying image to external storage";
                        break;
                    default:
                        break;
                }

                if (toastMessage != null) {
                    Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
                    toast.show();
                }
            });
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

                GeoPoint destination = model.getDestination();

                if (destination != null) {
                    addMarker(getApplicationContext(), map, destination);
                    mapCentered = true;
                }
            }
        };
    }

    /**
     * description: sets marker at given location on map
     */
    public static Marker addMarker(Context context, MapView map, GeoPoint position) {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }
}