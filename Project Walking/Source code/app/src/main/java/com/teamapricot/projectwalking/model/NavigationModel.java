package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.model.database.Route;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;
import com.teamapricot.projectwalking.observe.ObservableBase;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * @author Erik Wahlberger
 * @version 2021-10-16
 *
 *          Model class for the navigation functionality
 */
public class NavigationModel extends ObservableBase<NavigationModel> {
    private final int[] DISTANCES = new int[] { 300, 750, 1500 };

    private GeoPoint userLocation;
    private GeoPoint destination;
    private Polyline routeOverlay;
    private BoundingBox boundingBox;
    private double zoomLevel;
    private int distanceChoice = 0;
    private boolean followLocation = false;
    private boolean locationUpdates = false;

    private RouteDao routeDao = null;

    private Route route = null;

    public NavigationModel(RouteDao routeDao) {
        this.routeDao = routeDao;
    }
    /**
     * Get the user location
     * 
     * @return The location as a {@code GeoPoint} object
     */
    public GeoPoint getUserLocation() {
        return this.userLocation;
    }

    public GeoPoint getDestination() {
        return this.destination;
    }

    public Polyline getRouteOverlay() {
        return this.routeOverlay;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public boolean getFollowLocation() {
        return this.followLocation;
    }

    /**
     * Get the distance choice
     * 
     * @return The distance choice (0 to 2 where 0 means short and 2 means long)
     */
    public int getDistanceChoice() {
        return distanceChoice;
    }

    /**
     * Get the zoom level for the map
     * 
     * @return The zoom level as a {@code double} value
     */
    public double getZoomLevel() {
        return zoomLevel;
    }

    /**
     * Maybe initialize the destination with route from the database.
     * @param roadManager
     */
    public void initDestination(RoadManager roadManager) {
        route = routeDao.getOpenRoute();
        if(route == null) {
            return;
        }
        destination = new GeoPoint(route.getEndX(), route.getEndY());
        updateDestination(roadManager);
    }

    public void createDestination(RoadManager roadManager) {
        createDestination(roadManager, DISTANCES[distanceChoice]);
    }

    public void createDestination(RoadManager roadManager, double radius) {
        if(userLocation == null) {
            return;
        }

        // deg is angle and len is distance
        double len = 0.8 * radius + 0.2 * radius * Math.random();
        double deg = Math.random() * 360;

        destination = userLocation.destinationPoint(len, deg);

        route = new Route(userLocation.getLatitude(), userLocation.getLongitude(),
                          destination.getLatitude(), destination.getLongitude(), radius);
        routeDao.insertOne(route);

        updateDestination(roadManager);
    }

    private void updateDestination(RoadManager roadManager) {
        CompletableFuture.runAsync(() -> {
            ArrayList<GeoPoint> points = new ArrayList<>();
            points.add(this.getUserLocation());
            points.add(this.getDestination());

            Road road = roadManager.getRoad(points);

            this.routeOverlay = RoadManager.buildRoadOverlay(road);
            BoundingBox routeBoundingBox = this.routeOverlay.getBounds();

            if (routeBoundingBox != null) {
                points.add(new GeoPoint(routeBoundingBox.getLatNorth(), routeBoundingBox.getLonWest()));
                points.add(new GeoPoint(routeBoundingBox.getLatNorth(), routeBoundingBox.getLonEast()));
                points.add(new GeoPoint(routeBoundingBox.getLatSouth(), routeBoundingBox.getLonWest()));
                points.add(new GeoPoint(routeBoundingBox.getLatSouth(), routeBoundingBox.getLonEast()));
            }

            this.boundingBox = calculateBoundingBox(points);
            this.followLocation = false;

        }).thenRun(() -> {
            updateObservers(this);
        });
    }

    /**
     * Enables user location updates, and updates all observers
     * @param locationOverlay The {@link MyLocationNewOverlay} to retrieve the location from
     * @param updateIntervalMs The location update interval in milliseconds
     */
    public void enableLocationUpdates(MyLocationNewOverlay locationOverlay, long updateIntervalMs) {
        setLocationUpdates(true);

        CompletableFuture.runAsync(() -> {
            while (getLocationUpdates()) {
                setUserLocation(locationOverlay.getMyLocation());
                try {
                    Thread.sleep(updateIntervalMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, Executors.newSingleThreadExecutor());
    }

    /**
     * Disables location updates
     */
    public void disableLocationUpdates() {
        setLocationUpdates(false);
    }

    private synchronized void setLocationUpdates(boolean locationUpdates) {
        this.locationUpdates = locationUpdates;
    }

    private synchronized boolean getLocationUpdates() {
        return this.locationUpdates;
    }

    /**
     * Sets the destination and routeOverlay to null, disables following user,
     * then updates all observers
     */
    public void removeDestination() {
        if(route != null) {
            routeDao.deleteOne(route);
        }
        this.destination = null;
        this.routeOverlay = null;
        this.followLocation = false;
        updateObservers(this);
    }

    /**
     * Sets the user location and updates all observers
     * 
     * @param location the value for the user location
     */
    public void setUserLocation(GeoPoint location) {
        this.userLocation = location;
        updateObservers(this);
    }

    public void setFollowLocation(boolean followLocation) {
        this.followLocation = followLocation;
        updateObservers(this);
    }

    /**
     * Sets the zoom level for the map and updates all observers
     * 
     * @param zoomLevel the zoom level as a {@code double} value
     */
    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
        updateObservers(this);
    }

    public void setDistanceChoice(int distanceChoice) {
        this.distanceChoice = distanceChoice;
    }

    /**
     * Calculates the BoundingBox for the specified points (Takes the
     * largest/smallest longitudes and latitudes for the specified points and
     * creates a BoundingBox for them)
     * 
     * @param points The points
     * @return A BoundingBox
     */
    private BoundingBox calculateBoundingBox(List<GeoPoint> points) {
        double north = -180;
        double south = 180;
        double west = 180;
        double east = -180;

        for (GeoPoint point : points) {
            if (point.getLatitude() > north) {
                north = point.getLatitude();
            }

            if (point.getLongitude() < south) {
                south = point.getLatitude();
            }

            if (point.getLongitude() > east) {
                east = point.getLongitude();
            }

            if (point.getLongitude() < west) {
                west = point.getLongitude();
            }
        }

        return new BoundingBox(north, east, south, west);
    }
}
