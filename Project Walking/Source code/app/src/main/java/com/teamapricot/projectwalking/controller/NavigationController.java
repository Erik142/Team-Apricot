package com.teamapricot.projectwalking.controller;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.handlers.LocationHandler;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.observe.Observer;
import com.teamapricot.projectwalking.view.dialogs.ChooseDistanceDialog;
import com.teamapricot.projectwalking.view.dialogs.ReplaceDestinationDialog;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

/**
 * @author Erik Wahlberger
 * @version 2021-09-28ä
 *
 * Controller class for navigation functionality
 */
public class NavigationController {
    private final int LOCATION_UPDATE_INTERVAL_MS = 2000;
    private final double INITIAL_ZOOM_LEVEL = 16;

    private AppCompatActivity activity;
    private LocationHandler locationHandler;
    private NavigationModel navigationModel;
    private RoadManager roadManager;

    /**
     * Creates a new instance of the {@code NavigationController class} for the specified {@code AppCompatActivity}.
     * @param activity The {@code AppCompatActivity} that will be used for activity results
     */
    public NavigationController(AppCompatActivity activity) {
        this.navigationModel = new NavigationModel();
        this.activity = activity;
        roadManager = new OSRMRoadManager(activity, "Fun Walking");
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);
    }

    /**
     * Starts to update the user position, and sets the initial zoom level
     */
    public void start() {
        locationHandler = new LocationHandler(this.activity, LOCATION_UPDATE_INTERVAL_MS);

        locationHandler.registerUpdateListener(position -> {
            GeoPoint point = new GeoPoint(position.getLatitude(), position.getLongitude());

            navigationModel.setUserLocation(point);
        });

        this.navigationModel.setZoomLevel(INITIAL_ZOOM_LEVEL);
    }

    /**
     * Registers an {@code Observer} in the model.
     * @param observer The {@code Observer<NavigationModel>} to be registered
     */
    public void registerObserver(Observer<NavigationModel> observer) {
        this.navigationModel.addObserver(observer);
    }

    /**
     * registers a button for triggering new destination, and removing destination
     * @param button triggers new destination, or remove destination based on the button id
     */
    public void registerOnClickListener(View button) {
        switch (button.getId()) {
            case R.id.add_destination_fab:
                addDestination(button);
                break;
            case R.id.remove_destination_fab:
                removeDestination(button);
                break;
        }
    }

    /**
     * Register onClickListener for adding a destination to the map
     * @param button The button that will be used to add a destination to the map
     */
    private void addDestination(View button) {
        button.setOnClickListener(view -> {
            if(navigationModel.getDestination() == null) {
                navigationModel.createDestination(roadManager);
            }
        });

        button.setOnLongClickListener(view -> {
            ChooseDistanceDialog dialog =
                    new ChooseDistanceDialog(
                            this.activity, navigationModel.getDistanceChoice(),
                            (choice) -> navigationModel.setDistanceChoice(choice));
            dialog.show(activity.getSupportFragmentManager(), "NavigationController");
            return true;
        });
    }

    /**
     * Register onClickListener for removing a destination from the map
     * @param button The button that will be used to remove a destination from the map
     */
    private void removeDestination(View button) {
        button.setOnClickListener(view -> {
            if(navigationModel.getDestination() != null) {
                ReplaceDestinationDialog dialog =
                        new ReplaceDestinationDialog(this.activity, () -> { navigationModel.createDestination(roadManager); });
                dialog.show(activity.getSupportFragmentManager(), "NavigationController");
            }
        });
    }
}
