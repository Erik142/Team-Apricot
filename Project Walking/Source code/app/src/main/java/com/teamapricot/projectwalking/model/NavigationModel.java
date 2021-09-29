package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.observe.ObservableBase;

import org.osmdroid.util.GeoPoint;

/**
 * @author Erik Wahlberger
 * @version 2021-09-28
 *
 * Model class for the navigation functionality
 */
public class NavigationModel extends ObservableBase<NavigationModel> {
    private final int DESTINATION_RADIUS = 50;

    private GeoPoint userLocation;
    private GeoPoint destination;
    private double zoomLevel;

    /**
     * Get the user location
     * @return The location as a {@code GeoPoint} object
     */
    public GeoPoint getUserLocation() {
        return this.userLocation;
    }

    public GeoPoint getDestination() { return this.destination; }

    /**
     * Get the zoom level for the map
     * @return The zoom level as a {@code double} value
     */
    public double getZoomLevel() {
        return zoomLevel;
    }

    public void createDestination() {
        //deg is angle and len is distance
        double len = Math.sqrt(Math.random()) * DESTINATION_RADIUS;
        double deg = Math.random() * 360;

        GeoPoint userLocation = getUserLocation();

        if (userLocation != null) {
            GeoPoint location = userLocation.destinationPoint(len, deg);

            this.destination = location;
            updateObservers(this);
        }
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
