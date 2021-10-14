package com.teamapricot.projectwalking.view;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.CameraController;
import com.teamapricot.projectwalking.controller.ImageOverlayController;
import com.teamapricot.projectwalking.controller.NavigationController;
import com.teamapricot.projectwalking.controller.NotificationController;
import com.teamapricot.projectwalking.controller.ToolbarController;
import com.teamapricot.projectwalking.model.CameraModel;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.observe.Observer;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final double ALLOWED_DISTANCE = 20;
    
    private NavigationController navigationController;
    private CameraController cameraController;
    private ImageOverlayController imageOverlayController;

    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;
    private NotificationController notificationController;

    private ToolbarController toolbarController;

    private Marker destinationMarker = null;
    private GeoPoint oldDestination = null;
    private Polyline routeOverlay = null;

    private Button button;

    private boolean mapCentered;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

        try {
            Database database = Database.getDatabase(this).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toolbarController.onToolbarMenuItemClick(item, this);
    }

    private void init() {
        initToolbar();
        initNavigation();
        initCamera();
        notificationController = new NotificationController(getApplicationContext());
        notificationController.SendNotification(false);
        initImageOverlay();
        initCameraButtonVisibility();
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        toolbarController = new ToolbarController();
    }

    private void initCamera() {
        cameraController = new CameraController(this);

        View openCameraButton = findViewById(R.id.open_camera_fab);
        setCameraButtonVisibility(View.INVISIBLE);

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

        button = findViewById(R.id.new_destination_button);
        navigationController.registerObserver(createNavigationObserver());
        navigationController.start();
        navigationController.registerNewDestinationButtonListeners(button);
    }

    private void initImageOverlay() {
        if (map == null) {
            Log.e("initImageOverlay", "MapView not initialized");
            return;
        }

        imageOverlayController = new ImageOverlayController(this, new ImageOverlayView(map));
        imageOverlayController.initImageOverlays();
    }

    private void initCameraButtonVisibility() {
        if (map == null || navigationController == null) {
            Log.e("initCameraButtonVisibility", "Initialization failed earlier");
            return;
        }

        navigationController.registerObserver(model -> {
            runOnUiThread(() -> {
                GeoPoint location = model.getUserLocation();
                GeoPoint destination = model.getDestination();
                if (destination == null) {
                    setCameraButtonVisibility(View.INVISIBLE);
                    return;
                }
                double distance = location.distanceToAsDouble(destination);
                Log.d("observer", "distance = " + distance);
                if (distance > ALLOWED_DISTANCE) {
                    setCameraButtonVisibility(View.INVISIBLE);
                    return;
                }
                setCameraButtonVisibility(View.VISIBLE);
            });
        });
    }

    /**
     * Updates the camera visibility if it would change.
     *
     * @param visibility - The wanted visibility (e.g. {@code View.VISIBLE})
     */
    public void setCameraButtonVisibility(int visibility) {
        View cameraButton = this.findViewById(R.id.open_camera_fab);
        if (cameraButton.getVisibility() != visibility) {
            cameraButton.setVisibility(visibility);
            cameraButton.invalidate();
        }
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
            runOnUiThread(() -> {
                mapController.setZoom(model.getZoomLevel());

                GeoPoint location = model.getUserLocation();

                if (!mapCentered && location != null) {
                    mapController.setCenter(location);
                }

                GeoPoint destination = model.getDestination();

                if (destination == null || destination == oldDestination) {
                    return;
                }

                if(destinationMarker != null) {
                    destinationMarker.setPosition(destination);
                } else {
                    destinationMarker = addMarker(map, destination);
                }

                if(routeOverlay != null) {
                    map.getOverlays().remove(routeOverlay);
                }

                routeOverlay = model.getRouteOverlay();
                map.getOverlays().add(0, routeOverlay);
                map.invalidate();
                mapCentered = true;
            });
        };
    }

    /**
     * description: sets marker at given location on map
     */
    public static Marker addMarker(MapView map, GeoPoint position) {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }
}
