package com.teamapricot.projectwalking.view;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

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

    /**
     * Constructor.
     *
     * @param mapView       - The associated map view
     * @param thumbnailSize - The maximum width/height of thumbnails
     */
    public ImageOverlayView(MapView mapView, int thumbnailSize) {
        this.mapView = mapView;
        this.thumbnailSize = thumbnailSize;
    }

    public ImageOverlayView(MapView mapView) {
        this(mapView, DEFAULT_THUMBNAIL_SIZE);
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
        mapView.getOverlays().add(iconOverlay);
        makeSureUserLocationIsVisible();
        mapView.invalidate();
    }

    public void makeSureUserLocationIsVisible() {
        List<Overlay> overlays = mapView.getOverlays();
        Overlay locationOverlay = overlays
                .stream()
                .filter(o -> o instanceof MyLocationNewOverlay)
                .findAny()
                .orElse(null);
        if(locationOverlay != null) {
            overlays.remove(locationOverlay);
            overlays.add(locationOverlay);
        }
    }

    /**
     * Removes an icon overlay from the map.
     *
     * @param iconOverlay - The icon overlay to remove
     */
    public void removeIconOverlay(IconOverlay iconOverlay) {
        mapView.getOverlays().remove(iconOverlay);
        mapView.invalidate();
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
