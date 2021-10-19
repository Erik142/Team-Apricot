package com.teamapricot.projectwalking.controller;

import android.Manifest;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.handlers.PermissionHandler;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;
import com.teamapricot.projectwalking.observe.Observer;
import com.teamapricot.projectwalking.view.dialogs.ChooseDistanceDialog;
import com.teamapricot.projectwalking.view.dialogs.PermissionRejectedDialog;
import com.teamapricot.projectwalking.view.dialogs.RemoveDestinationDialog;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * @author Erik Wahlberger
 * @version 2021-10-16
 *
 * Controller class for navigation functionality
 */
public class NavigationController {
    private final String TAG = "NavigationController";
    private final int LOCATION_UPDATE_INTERVAL_MS = 2000;
    private final double INITIAL_ZOOM_LEVEL = 16;

    private AppCompatActivity activity;
    private NavigationModel navigationModel;
    private RoadManager roadManager;

    private RouteDao routeDao = null;

    /**
     * Creates a new instance of the {@code NavigationController class} for the
     * specified {@code AppCompatActivity}.
     * 
     * @param activity The {@code AppCompatActivity} that will be used for activity
     *                 results
     */
    public NavigationController(NavigationModel navigationModel, AppCompatActivity activity) {
        this.navigationModel = navigationModel;
        this.activity = activity;
        roadManager = new OSRMRoadManager(activity, "Fun Walking");
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);
        Database.getDatabase(activity.getApplicationContext()).thenAccept(database -> {
            routeDao = database.routeDao();
            // navigationModel.initDestination(roadManager);
            routeDao.deleteOpenRoutes();
        });
    }

    /**
     * @author Erik Wahlberger, Daniel Brännvall
     * Starts to update the user position, and sets the initial zoom level
     * @param locationOverlay The {@link MyLocationNewOverlay} to use for location updates
     */
    public void start(MyLocationNewOverlay locationOverlay) {
        PermissionHandler permissionHandler = new PermissionHandler(this.activity);

        if (!permissionHandler.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionHandler.requestPermissionAsync(Manifest.permission.ACCESS_FINE_LOCATION)
                    .thenAccept(permissionGranted -> {
                        if (permissionGranted) {
                            start(locationOverlay);
                        } else {
                            PermissionRejectedDialog dialog = new PermissionRejectedDialog(activity,
                                    "Permission to access your device's location is required. Please grant access.");

                            dialog.show(activity.getSupportFragmentManager(), TAG);
                        }
                    });
        } else {
            startUpdateModel(locationOverlay);
        }
    }

    private void startUpdateModel(MyLocationNewOverlay locationOverlay) {
        this.navigationModel.setZoomLevel(INITIAL_ZOOM_LEVEL);
        this.navigationModel.enableLocationUpdates(locationOverlay, LOCATION_UPDATE_INTERVAL_MS);
    }

    /**
     * Registers an {@code Observer} in the model.
     * 
     * @param observer The {@code Observer<NavigationModel>} to be registered
     */
    public void registerObserver(Observer<NavigationModel> observer) {
        this.navigationModel.addObserver(observer);
    }

    /**
     * registers a button for triggering new destination, and removing destination
     * 
     * @param button triggers new destination, or remove destination based on the
     *               button id
     */
    public void registerOnClickListener(View button) {
        switch (button.getId()) {
            case R.id.add_destination_fab:
                addDestinationClick(button);
                break;
            case R.id.remove_destination_fab:
                removeDestinationClick(button);
                break;
        }
    }

    /**
     * Register onClickListener for adding a destination to the map
     * 
     * @param button The button that will be used to add a destination to the map
     */
    private void addDestinationClick(View button) {
        button.setOnClickListener(view -> {
            if (navigationModel.getDestination() == null) {
                navigationModel.createDestination(roadManager);
            }
        });

        button.setOnLongClickListener(view -> {
            ChooseDistanceDialog dialog = new ChooseDistanceDialog(this.activity, navigationModel.getDistanceChoice(),
                    (choice) -> navigationModel.setDistanceChoice(choice));
            dialog.show(activity.getSupportFragmentManager(), "NavigationController");
            return true;
        });
    }

    /**
     * Register onClickListener for removing a destination from the map
     * 
     * @param button The button that will be used to remove a destination from the
     *               map
     */
    private void removeDestinationClick(View button) {
        button.setOnClickListener(view -> {
            if (navigationModel.getDestination() != null) {
                RemoveDestinationDialog dialog = new RemoveDestinationDialog(this.activity, () -> {
                    navigationModel.removeDestination();
                });
                dialog.show(activity.getSupportFragmentManager(), "NavigationController");
            }
        });
    }

    public void removeDestination() {
        navigationModel.removeDestination();
    }
}
