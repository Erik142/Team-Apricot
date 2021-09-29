package com.teamapricot.projectwalking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.handlers.CameraHandler;
import com.teamapricot.projectwalking.photos.PhotoController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private PhotoController photoController;
    private LocationHandler locationHandler;
    private IMapController mapController;
    private MyLocationNewOverlay locationOverlay;

    boolean mapInitialized = false;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionHandler permissionHandler = new PermissionHandler(this);
        CameraHandler cameraHandler = new CameraHandler(this);

        photoController = new PhotoController(this, permissionHandler, cameraHandler);

        locationHandler = new LocationHandler(this, 2000);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);
        createChannel();
        Reminder GetNotified = new Reminder(MainActivity.this);
        GetNotified.addNotification("new_spot", "new_challenge", "notify_message", 1);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapController = map.getController();
        mapController.setZoom(19.5);

        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        locationOverlay.enableMyLocation();

        map.getOverlays().add(locationOverlay);






        locationHandler.registerUpdateListener(position -> {
            GeoPoint point = new GeoPoint(position.getLatitude(), position.getLongitude());

            if(!mapInitialized) {
                mapController.setCenter(point);
                mapInitialized = true;
            }
            //create a circle around users current location
            Polygon polygon = new Polygon();
            final double radius = 50;
            List<GeoPoint> circlePoints = new ArrayList<>();
            for (float f = 0; f < 360; f += 1){
                circlePoints.add(new GeoPoint(position.getLatitude() ,position.getLongitude()).destinationPoint(radius, f));
            }
            polygon.setPoints(circlePoints);
            map.getOverlays().add(polygon);

            //deg is angle and len is distance
            double len = Math.sqrt(Math.random()) * radius;
            double deg = Math.random() * 2 * Math.PI;
            double x = position.getLatitude() + len * Math.cos(deg);
            double y = position.getLongitude() + len * Math.sin(deg);
            GeoPoint nextPoint = new GeoPoint(x,y);

                addMarker(ctx, map, nextPoint);

        });

    }

    /**
     * description: sets marker at given location on map
     */


    public static Marker addMarker(Context context, MapView map, GeoPoint position) {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }



    /**
     * description: generates Random location


    public getRandomPointInCircle() {

        int radius = 50;
        //deg is angle and len is distance
        double len = Math.sqrt(Math.random()) * radius;
        double deg = Math.random() * 2 * Math.PI;
        double x = location.getLatitude() + len * Math.cos(deg); //
        double y = location.getLongitude() + len * Math.sin(deg);

     //create another geopoint with random lat and longitude and check if they are within the radius
     if (startPoint.getLongitude() < -180)
     startPoint.setLongitude(startPoint.getLongitude() + 360);
     if (startPoint.getLongitude() > 180)
     startPoint.setLongitude(startPoint.getLongitude() - 360);

     if (startPoint.getLatitude() > 85.05112877980659)
     startPoint.setLatitude(85.05112877980659);
     if (startPoint.getLatitude() < -85.05112877980659)
     startPoint.setLatitude(-85.05112877980659);
     /**
     * description: sets marker at given location on map

     addMarker(ctx, map, point);
     addMarker(ctx, map, startPoint);
     List<GeoPoint> geoPoints = new ArrayList<>();
     geoPoints.add(point);
     // GeoPoint startPoint = new GeoPoint(position.getLatitude()+1, position.getLongitude()-1);
     geoPoints.add(startPoint);
     Polygon polygon = new Polygon();
     polygon.setPoints(geoPoints);
     map.getOverlays().add(polygon);

     */

    /**
     * description: generates Random location

    public Location getRandomLocation() {
        Location location = new Location("");
        GeoPoint latLng = new GeoPoint(location.getLatitude(), location.getLongitude());
        int radius = 10;


        double x0 = location.getLatitude();
        double y0 = location.getLongitude();

        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);
     locatoin newLocation = new locait

    }

     */

/**
     * description: creating the notification_channel for higher versions
     */


    public void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_message", "new_spot", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        map.onPause();
    }

    public void captureImage(View view) {
        photoController.captureImageAsync();
    }
}
