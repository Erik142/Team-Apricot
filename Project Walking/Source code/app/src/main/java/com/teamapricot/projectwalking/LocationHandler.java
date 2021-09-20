package com.teamapricot.projectwalking;

import android.Manifest;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Brännvall
 * @version 2021-09-20
 *
 * A class for accessing location data.
 */
public class LocationHandler {
    private final PermissionHandler permissionHandler;

    private Position position = null;

    private final LocationManager locationManager;
    private LocationListener locationListener = null;

    private final int updateInterval;

    private final List<UpdateListener> updateListeners;

    /**
     * Constructor.
     *
     * @param activity The associated Activity
     * @param interval Interval (in ms) between location updates
     */
    LocationHandler(AppCompatActivity activity, int interval) {
        permissionHandler = new PermissionHandler(activity);
        locationManager = (LocationManager) activity.getApplicationContext()
                .getSystemService(AppCompatActivity.LOCATION_SERVICE);
        updateListeners = new ArrayList<>();
        updateInterval = interval;

        if (permissionHandler.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initializeLocationHandler();
        } else {
            permissionHandler.requestPermissionAsync(Manifest.permission.ACCESS_FINE_LOCATION)
                    .thenAccept(permissionGranted -> {
                        if (permissionGranted) {
                            initializeLocationHandler();
                        } else {
                            // TODO: Permission not granted, do something!
                            System.out.println("permission not granted");
                        }
                    });
        }
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Register an UpdateListener to get location updates.
     *
     * @param listener The listener to register
     */
    public void registerUpdateListener(UpdateListener listener) {
        updateListeners.add(listener);
    }

    /**
     * Unregister an UpdateListener.
     *
     * @param listener The listener to unregister
     */
    public void unregisterUpdateListener(UpdateListener listener) {
        updateListeners.remove(listener);
    }

    private LocationListener createLocationListener() {
        return location -> {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double accuracy = location.getAccuracy();

            position = new Position(latitude, longitude, accuracy);

            for (UpdateListener l : updateListeners) {
                l.onPositionUpdated(position);
            }
        };

    }

    private void initializeLocationHandler() {
        if (locationListener == null) {
            locationListener = createLocationListener();
        }

        if (permissionHandler.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    updateInterval, 0, locationListener);
        }
    }

    /**
     * @author Daniel Brännvall
     * @version 2021-09-20*
     *
     * The interface to use for receiving location updates.
     */
    public interface UpdateListener {
        void onPositionUpdated(Position position);
    }

    /**
     * @author Daniel Brännvall
     * @version 2021-09-20
     *
     * A position.
     */
    public static class Position {
        private final double latitude;
        private final double longitude;
        private final double accuracy;

        Position(double latitude, double longitude, double accuracy) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.accuracy = accuracy;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getAccuracy() {
            return accuracy;
        }
    }
}
