package com.teamapricot.projectwalking.controller;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.LocationHandler;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.observe.Observer;

import org.osmdroid.util.GeoPoint;

/**
 * @author Erik Wahlberger
 * @version 2021-09-28
 *
 * Controller class for navigation functionality
 */
public class NavigationController {
    private final int LOCATION_UPDATE_INTERVAL_MS = 2000;
    private final double INITIAL_ZOOM_LEVEL = 19.5;

    private AppCompatActivity activity;
    private LocationHandler locationHandler;
    private NavigationModel navigationModel;

    /**
     * Creates a new instance of the {@code NavigationController class} for the specified {@code AppCompatActivity}.
     * @param activity The {@code AppCompatActivity} that will be used for activity results
     */
    public NavigationController(AppCompatActivity activity) {
        this.navigationModel = new NavigationModel();
        this.activity = activity;
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
}
