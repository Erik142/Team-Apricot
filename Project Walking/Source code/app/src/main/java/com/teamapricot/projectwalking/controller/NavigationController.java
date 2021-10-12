package com.teamapricot.projectwalking.controller;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.handlers.LocationHandler;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.observe.Observer;

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

    private boolean isOnetimeExecutionDone = false;

    /**
     * Creates a new instance of the {@code NavigationController class} for the specified {@code AppCompatActivity}.
     * @param activity The {@code AppCompatActivity} that will be used for activity results
     */
    public NavigationController(AppCompatActivity activity) {
        this.navigationModel = new NavigationModel();
        this.activity = activity;
        roadManager = new OSRMRoadManager(activity, "Fun Walking");
    }

    /**
     * Starts to update the user position, and sets the initial zoom level
     */
    public void start() {
        locationHandler = new LocationHandler(this.activity, LOCATION_UPDATE_INTERVAL_MS);

        locationHandler.registerUpdateListener(position -> {
            GeoPoint point = new GeoPoint(position.getLatitude(), position.getLongitude());

            if(!isOnetimeExecutionDone) {
                navigationModel.setUserLocation(point);
                isOnetimeExecutionDone = true;
            }
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
     * registers a button for triggering new destination
     * @param button triggers new destination
     */
    public void registerNewDestinationButtonListeners(View button) {
        button.setOnClickListener(view -> {
            // if(navigationModel.getDestination() == null) {
                navigationModel.createDestination(roadManager);
            // } else {
            //     TODO: are you sure?
            // }
        });

        // button.setOnLongClickListener(view -> {
        //     TODO: choose distance dialog
        // });
    }
}
