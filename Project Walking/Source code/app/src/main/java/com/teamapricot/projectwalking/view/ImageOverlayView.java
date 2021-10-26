package com.teamapricot.projectwalking.view;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;

/**
 * @author Daniel Brännvall
 * @version 2021-10-04
 *
 * View for images overlaid on the map.
 */
public class ImageOverlayView {
    private static final String TAG = "ImageOverlayView";

    private static final int DEFAULT_THUMBNAIL_SIZE = 128;
    private int thumbnailSize;

    private final MapView mapView;
    private final AppCompatActivity activity;

    /**
     * Constructor.
     *
     * @param mapView       - The associated map view
     * @param thumbnailSize - The maximum width/height of thumbnails
     */
    public ImageOverlayView(AppCompatActivity activity, MapView mapView, int thumbnailSize) {
        this.activity = activity;
        this.mapView = mapView;
        this.thumbnailSize = thumbnailSize;
    }

    public ImageOverlayView(AppCompatActivity activity, MapView mapView) {
        this(activity, mapView, DEFAULT_THUMBNAIL_SIZE);
    }

    public int getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(int thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    /**
     * Adds an icon overlay to the map.
     *
     * @param iconOverlay - The icon overlay to add
     */
    public void addIconOverlay(IconOverlay iconOverlay) {
        this.activity.runOnUiThread(() -> {
            mapView.getOverlays().add(iconOverlay);
            mapView.invalidate();
        });
    }

    /**
     * Removes an icon overlay from the map.
     *
     * @param iconOverlay - The icon overlay to remove
     */
    public void removeIconOverlay(IconOverlay iconOverlay) {
        this.activity.runOnUiThread(() -> {
            mapView.getOverlays().remove(iconOverlay);
            mapView.invalidate();
        });
    }

    /**
     * Calculates the thumbnail width/height based on image size.
     *
     * @param width  - The width of the image
     * @param height - The height of the image
     * @return The width and height of the thumbnail
     */
    public int[] thumbnailSize(int width, int height) {
        int[] tnSize = {0, 0};
        if (width > height) {
            tnSize[0] = thumbnailSize;
            tnSize[1] = height * thumbnailSize / width;
        } else {
            tnSize[0] = width * thumbnailSize / height;
            tnSize[1] = thumbnailSize;
        }
        return tnSize;
    }
}
