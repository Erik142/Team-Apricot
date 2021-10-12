package com.teamapricot.projectwalking.model;

import android.content.Context;

import com.teamapricot.projectwalking.observe.ObservableBase;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * @author Erik Wahlberger
 * @version 2021-09-28
 *
 * Model class for the navigation functionality
 */
public class NavigationModel extends ObservableBase<NavigationModel> {
    private final int DESTINATION_RADIUS = 500;

    private GeoPoint userLocation;
    private GeoPoint destination;
    private Polyline routeOverlay;
    private double zoomLevel;

    /**
     * Get the user location
     * @return The location as a {@code GeoPoint} object
     */
    public GeoPoint getUserLocation() {
        return this.userLocation;
    }

    public GeoPoint getDestination() { return this.destination; }

    public Polyline getRouteOverlay() { return this.routeOverlay; }

    /**
     * Get the zoom level for the map
     * @return The zoom level as a {@code double} value
     */
    public double getZoomLevel() {
        return zoomLevel;
    }

    public void createDestination(RoadManager roadManager) {
        createDestination(roadManager, DESTINATION_RADIUS);
    }

    public void createDestination(RoadManager roadManager, double radius ) {
        //deg is angle and len is distance
        double len = radius + (0.1 * radius * (Math.random() - 0.5));
        double deg = Math.random() * 360;

        if (userLocation == null) {
            return;
        }

        GeoPoint newDestination = userLocation.destinationPoint(len, deg);

        this.destination = newDestination;

        CompletableFuture.runAsync(() -> {
            ArrayList<GeoPoint> points = new ArrayList<>();
            points.add(this.getUserLocation());
            points.add(this.getDestination());

            Road road = roadManager.getRoad(points);

            this.routeOverlay = RoadManager.buildRoadOverlay(road);
        }).thenRun(() -> {
            updateObservers(this);
        });
    }

    /**
     * Sets the user location and updates all observers
     * @param location the value for the user location
     */
    public void setUserLocation(GeoPoint location) {
        this.userLocation = location;
        updateObservers(this);
    }

    /**
     * Sets the zoom level for the map and updates all observers
     * @param zoomLevel the zoom level as a {@code double} value
     */
    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
        updateObservers(this);
    }
}
