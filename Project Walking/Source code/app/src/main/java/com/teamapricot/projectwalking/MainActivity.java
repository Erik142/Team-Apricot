package com.teamapricot.projectwalking;

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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.view.View;

import com.teamapricot.projectwalking.handlers.CameraHandler;
import com.teamapricot.projectwalking.photos.PhotoController;

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
