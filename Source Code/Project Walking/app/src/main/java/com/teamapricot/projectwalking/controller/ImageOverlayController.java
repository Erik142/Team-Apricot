package com.teamapricot.projectwalking.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.handlers.StorageHandler;
import com.teamapricot.projectwalking.handlers.StorageHandler.PhotoWithLocation;
import com.teamapricot.projectwalking.model.database.Photo;
import com.teamapricot.projectwalking.view.ImageOverlayView;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ClickableIconOverlay;
import org.osmdroid.views.overlay.IconOverlay;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Brännvall
 * @version 2021-10-04
 *
 * Controller for overlaying images on the map.
 */
public class ImageOverlayController {
    private static final String TAG = "ImageOverlayView";

    private final ImageOverlayView imageOverlayView;
    private final AppCompatActivity activity;
    private final StorageHandler storageHandler;
    private final ImageController imageController;

    private final Map<String, ImageMarker> iconOverlays = new HashMap<>();

    /**
     * @author Daniel Brännvall
     * @version 2021-10-06
     */
    private class ImageMarker extends ClickableIconOverlay<File> {
        protected ImageMarker(File file) {
            super(file);
        }

        @Override
        protected boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint position, File file) {
            imageController.viewImageFile(file);
            return true;
        }
    }

    /**
     * Constructor.
     *
     * @param activity - The associated activity (required for StorageHandler)
     * @param view     - The associated image overlay view
     */
    public ImageOverlayController(AppCompatActivity activity, ImageOverlayView view) {
        this.activity = activity;
        this.imageOverlayView = view;
        this.storageHandler = StorageHandler.getInstance(activity);
        this.imageController = new ImageController(activity);
    }

    /**
     * Adds a thumbnail to the map.
     * @param photo The file and position of the photo to use
     */
    public void addImageOverlay(PhotoWithLocation photo) {
        if(photo == null) {
            Log.d(TAG, "Photo not found");
            return;
        }

        String filename = photo.getFilename();

        if (iconOverlays.containsKey(filename)) {
            Log.d(TAG, "Overlay for " + filename + " already exists");
            return;
        }

        GeoPoint location = new GeoPoint(photo.getLatitude(), photo.getLongitude());
        Bitmap image = BitmapFactory.decodeFile(filename);
        int[] tnSize = imageOverlayView.thumbnailSize(image.getWidth(), image.getHeight());
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image, tnSize[0], tnSize[1]);
        Drawable icon = new BitmapDrawable(activity.getResources(), thumbnail);
        ImageMarker iconOverlay = new ImageMarker(new File(filename));
        iconOverlay.set(location, icon);
        iconOverlays.put(filename, iconOverlay);
        imageOverlayView.addIconOverlay(iconOverlay);
    }

    /**
     * Remove a thumbnail from the map.
     *
     * @param file - Image file
     */
    public void removeImageOverlay(File file) {
        IconOverlay iconOverlay = iconOverlays.get(file);
        if (iconOverlay == null) {
            Log.d(TAG, "No overlay to remove for " + file.toString());
            return;
        }
        iconOverlays.remove(file);
        imageOverlayView.removeIconOverlay(iconOverlay);
    }

    /**
     * Initializes the image overlays.
     */
    public void initImageOverlays() {
        storageHandler.listPhotos().thenAccept(photos -> {

            for (Photo photo : photos) {
                PhotoWithLocation pwl = storageHandler.getPhotoWithLocation(photo);
                Log.d(TAG, "Adding overlay for " + pwl.getFilename());
                addImageOverlay(pwl);
            }
        });
    }

    public void addNewImageOverlays() {
    }
}
