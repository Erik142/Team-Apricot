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
    private final int[] DISTANCES = new int[] {300, 750, 1500};

    private GeoPoint userLocation;
    private GeoPoint destination;
    private Polyline routeOverlay;
    private double zoomLevel;
    private int distanceChoice = 0;

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
     * Get the distance choice
     * @return The distance choice (0 to 2 where 0 means short and 2 means long)
     */
    public int getDistanceChoice() {
        return distanceChoice;
    }

    /**
     * Get the zoom level for the map
     * @return The zoom level as a {@code double} value
     */
    public double getZoomLevel() {
        return zoomLevel;
    }

    public void createDestination(RoadManager roadManager) {
        createDestination(roadManager, DISTANCES[distanceChoice]);
    }

    public void createDestination(RoadManager roadManager, double radius ) {
        if (userLocation == null) {
            return;
        }

        //deg is angle and len is distance
        double len = 0.8 * radius + 0.2 * radius * Math.random();
        double deg = Math.random() * 360;

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
     * Sets the destination and routeOverlay to null, then updates all observers
     */
    public void removeDestination() {
        this.destination = null;
        this.routeOverlay = null;
        updateObservers(this);
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

    public void setDistanceChoice(int distanceChoice) {
        this.distanceChoice = distanceChoice;
    }
}
