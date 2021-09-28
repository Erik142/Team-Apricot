package com.teamapricot.projectwalking.controller;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.LocationHandler;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.observe.Observer;

import org.osmdroid.util.GeoPoint;

public class NavigationController {
    private final int LOCATION_UPDATE_INTERVAL_MS = 2000;
    private final double INITIAL_ZOOM_LEVEL = 19.5;

    private LocationHandler locationHandler;
    private NavigationModel navigationModel;

    public NavigationController(AppCompatActivity activity) {
        this.navigationModel = new NavigationModel();
        initLocationHandler(activity);

        this.navigationModel.setZoomLevel(INITIAL_ZOOM_LEVEL);
    }

    private void initLocationHandler(AppCompatActivity activity) {
        locationHandler = new LocationHandler(activity, LOCATION_UPDATE_INTERVAL_MS);

        locationHandler.registerUpdateListener(position -> {
            GeoPoint point = new GeoPoint(position.getLatitude(), position.getLongitude());
            navigationModel.setUserLocation(point);
        });
    }

    public void registerObserver(Observer<NavigationModel> observer) {
        this.navigationModel.addObserver(observer);
    }
}
