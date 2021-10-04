package com.teamapricot.projectwalking.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.handlers.StorageHandler;
import com.teamapricot.projectwalking.handlers.StorageHandler.ImageFileData;
import com.teamapricot.projectwalking.view.ImageOverlayView;

import org.osmdroid.util.GeoPoint;
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

    private final Map<File, IconOverlay> iconOverlays = new HashMap<>();

    /**
     * Constructor.
     *
     * @param activity - The associated activity (required for StorageHandler)
     * @param view     - The associated image overlay view
     */
    public ImageOverlayController(AppCompatActivity activity, ImageOverlayView view) {
        this.activity = activity;
        this.imageOverlayView = view;
        this.storageHandler = new StorageHandler(activity);
    }

    /**
     * Adds a thumbnail to the map.
     *
     * @param file - Image file
     */
    public void addImageOverlay(File file) {
        if (iconOverlays.containsKey(file)) {
            Log.d(TAG, "Overlay for " + file.toString() + " already exists");
            return;
        }

        ImageFileData img = storageHandler.getImageFileData(file);
        if (img == null) {
            Log.d(TAG, "Not an image file");
            return;
        }

        GeoPoint location = new GeoPoint(img.getLatitude(), img.getLongitude());
        Bitmap image = BitmapFactory.decodeFile(img.getFile().getPath());
        int[] tnSize = imageOverlayView.thumbnailSize(image.getWidth(), image.getHeight());
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image, tnSize[0], tnSize[1]);
        Drawable icon = new BitmapDrawable(activity.getResources(), thumbnail);
        IconOverlay iconOverlay = new IconOverlay(location, icon);
        iconOverlays.put(file, iconOverlay);
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
        imageOverlayView.removeIconOverlay(iconOverlay);
    }

    /**
     * Initializes the image overlays.
     */
    public void initImageOverlays() {
        for (ImageFileData img : storageHandler.listImageFiles()) {
            File file = img.getFile();
            Log.d(TAG, "Adding overlay for " + file.toString());
            addImageOverlay(file);
        }
    }
}
