package com.teamapricot.projectwalking.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.teamapricot.projectwalking.handlers.PermissionHandler;
import com.teamapricot.projectwalking.handlers.StorageHandler;
import com.teamapricot.projectwalking.model.CameraModel;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.Photo;
import com.teamapricot.projectwalking.observe.Observer;
import com.teamapricot.projectwalking.view.dialogs.MapLoadingDialog;
import com.teamapricot.projectwalking.view.dialogs.PermissionRejectedDialog;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final double ALLOWED_DISTANCE = 20;

    private NavigationController navigationController;
    private CameraController cameraController;
    private ImageOverlayController imageOverlayController;

    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;
    private NavigationModel navigationModel;

    private ToolbarController toolbarController;

    private Marker destinationMarker = null;
    private GeoPoint oldDestination = null;
    private Polyline routeOverlay = null;

    MenuItem checkboxItem;

    private boolean mapCentered;
    private boolean previousFollowLocation;

    private MapView map = null;

    private final MapLoadingDialog mapLoadingDialog = new MapLoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            map.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (map != null) {
            map.onPause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        checkboxItem = menu.findItem(R.id.action_follow_location);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toolbarController.onToolbarMenuItemClick(item, this);
    }

    private void init() {
        PermissionHandler permissionHandler = new PermissionHandler(this);
        if (permissionHandler.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initPostPermissions();
        } else {
            permissionHandler.requestPermissionAsync(Manifest.permission.ACCESS_FINE_LOCATION)
                    .thenAccept(permissionGranted -> {
                        if (!permissionGranted) {
                            PermissionRejectedDialog dialog = new PermissionRejectedDialog(this,
                                    "Permission to access your device's location is required. Please grant access.");
                            dialog.show(getSupportFragmentManager(), "MainActivity");
                            while (true) {
                                SystemClock.sleep(1000);
                            }
                        }
                        restart(this);
                    });
        }
    }

    private void initPostPermissions() {
        initModels();
        initToolbar();
        initNavigation();
        initCamera();
        initImageOverlay();
        initCameraButtonVisibility();
        // GetDistance();
        navigationController.start(this.locationOverlay);
    }

    private void initModels() {
        try {
            Database database = Database.getDatabase(this.getApplicationContext()).get();
            this.navigationModel = new NavigationModel(database.routeDao());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        toolbarController = new ToolbarController(this.navigationModel);
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

        navigationController = new NavigationController(navigationModel, this);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        showLoadingScreen(map.getOverlayManager().getTilesOverlay());

        mapController = map.getController();

        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        locationOverlay.enableMyLocation();
        locationOverlay.setEnableAutoStop(false);

        map.getOverlays().add(locationOverlay);

        View addDestinationButton = findViewById(R.id.add_destination_fab);
        View removeDestinationButton = findViewById(R.id.remove_destination_fab);
        navigationController.registerObserver(createNavigationObserver());
        navigationController.registerOnClickListener(addDestinationButton);
        navigationController.registerOnClickListener(removeDestinationButton);
    }

    /**
     * Shows a loading screen if the map has not been initialized
     *
     * @param tilesOverlay The {@link TilesOverlay} to use to check whether or not
     *                     the map has been initialized
     */
    private void showLoadingScreen(TilesOverlay tilesOverlay) {
        CompletableFuture.runAsync(() -> {
            if (!tilesOverlay.getTileStates().isDone()) {
                runOnUiThread(() -> mapLoadingDialog.show(getSupportFragmentManager(), "MapLoading"));
            }
        }, Executors.newSingleThreadExecutor());
    }

    private void initImageOverlay() {
        if (map == null) {
            Log.e("initImageOverlay", "MapView not initialized");
            return;
        }

        imageOverlayController = new ImageOverlayController(this, new ImageOverlayView(this, map));
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
                    if (checkboxItem != null) {
                        checkboxItem.setEnabled(false);
                    }
                    setButtonVisibility(R.id.open_camera_fab, View.GONE);
                    setButtonVisibility(R.id.add_destination_fab, View.VISIBLE);
                    setButtonVisibility(R.id.remove_destination_fab, View.GONE);
                    return;
                }
                if (checkboxItem != null) {
                    checkboxItem.setEnabled(true);
                }
                if (location == null) {
                    setButtonVisibility(R.id.open_camera_fab, View.GONE);
                    setButtonVisibility(R.id.add_destination_fab, View.VISIBLE);
                    setButtonVisibility(R.id.remove_destination_fab, View.GONE);
                    return;
                }
                if (location.distanceToAsDouble(destination) > ALLOWED_DISTANCE) {
                    setButtonVisibility(R.id.open_camera_fab, View.GONE);
                    setButtonVisibility(R.id.add_destination_fab, View.GONE);
                    setButtonVisibility(R.id.remove_destination_fab, View.VISIBLE);
                    return;
                }
                setButtonVisibility(R.id.open_camera_fab, View.VISIBLE);
                setButtonVisibility(R.id.add_destination_fab, View.GONE);
                setButtonVisibility(R.id.remove_destination_fab, View.GONE);
            });
        });
    }

    /**
     * Updates the visibility for the specified button if it would change.
     *
     * @param visibility - The wanted visibility (e.g. {@code View.VISIBLE})
     */
    public void setButtonVisibility(int buttonId, int visibility) {
        View button = this.findViewById(buttonId);
        if (button.getVisibility() != visibility) {
            button.setVisibility(visibility);
            button.invalidate();
        }
    }

    /**
     * Creates a new {@code Observer} object for the Camera functionality
     *
     * @return
     */
    private Observer<CameraModel> createCameraObserver() {
        return model -> {
            this.runOnUiThread(() -> {
                String toastMessage = null;

                switch (model.getStatus()) {
                case Done:
                    toastMessage = "Nice photo!";
                    navigationController.removeDestination();
                    StorageHandler sh = StorageHandler.getInstance(this);
                    sh.getLastPhoto().thenAccept(photo -> {
                        if (photo != null) {
                            imageOverlayController.addImageOverlay(sh.getPhotoWithLocation(photo));
                        }
                    });
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
     *
     * @return An {@code Observer<NavigationModel>} object to observe changes in a
     *         {@code NavigationModel} and update the UI correspondingly
     */
    private Observer<NavigationModel> createNavigationObserver() {
        return model -> {
            runOnUiThread(() -> {
                GeoPoint location = model.getUserLocation();
                GeoPoint destination = model.getDestination();

                if (!mapCentered) {
                    previousFollowLocation = model.getFollowLocation();
                    mapController.setZoom(model.getZoomLevel());
                    if (location != null) {
                        mapController.setCenter(location);
                        mapCentered = true;
                        oldDestination = destination;
                        mapLoadingDialog.dismiss();
                        return;
                    }
                }

                if (destination != null && destination != oldDestination) {
                    // center on user movement
                    if (!model.getFollowLocation() && model.getBoundingBox() != null) {
                        locationOverlay.disableFollowLocation();
                        map.zoomToBoundingBox(model.getBoundingBox(), true, 150);
                    }
                    map.invalidate();
                }

                if (model.getFollowLocation() != previousFollowLocation) {
                    if (model.getFollowLocation()) {
                        locationOverlay.enableFollowLocation();
                    } else {
                        locationOverlay.disableFollowLocation();
                    }
                    map.invalidate();
                    previousFollowLocation = model.getFollowLocation();
                    checkboxItem.setChecked(model.getFollowLocation());
                }

                if (destination == oldDestination) {
                    return;
                }

                if (destinationMarker != null && destination == null) {
                    map.getOverlays().remove(destinationMarker);
                    map.invalidate();
                    destinationMarker = null;
                } else if (destination != null) {
                    destinationMarker = addMarker(map, destination);
                }

                if (routeOverlay != null) {
                    map.getOverlays().remove(routeOverlay);
                    map.invalidate();
                }

                routeOverlay = model.getRouteOverlay();

                if (routeOverlay != null) {
                    map.getOverlays().add(0, routeOverlay);
                    map.invalidate();
                }

                oldDestination = destination;
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

    /**
     * Restarts the app.
     *
     * @param context The context of the app.
     */
    private static void restart(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }
}
