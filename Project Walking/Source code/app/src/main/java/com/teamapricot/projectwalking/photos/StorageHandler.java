package com.teamapricot.projectwalking.photos;

import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Brännvall
 * @version 2021-09-27
 *
 * Class for handling access to the stored images.
 */
public class StorageHandler {
    private final String TAG = "StorageHandler";

    private final File directory = ImageFileHandler.getImageDirectory();

    /**
     * @author Daniel Brännvall
     * @version 2021-09-27
     *
     * Data about an image file.
     */
    public static class ImageFileData {
        private final File file;
        private final double latitude;
        private final double longitude;

        public ImageFileData(File file, double latitude, double longitude) {
            this.file = file;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public File getFile() {
            return file;
        }
    }

    /**
     * Reads the location data of a file and creates an ImageFileData object.
     *
     * @param file - The file to examine
     * @return The image file data
     */
    private ImageFileData getImageFileData(File file) {
        try {
            FileInputStream input = new FileInputStream(file);
            ExifInterface exif = new ExifInterface(input);
            double[] latLong = exif.getLatLong();
            if (latLong != null) {
                Log.d(TAG, "Created file data object for " + file.getName());
                return new ImageFileData(file, latLong[0], latLong[1]);
            }
        } catch (IOException e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Creates a stream of File objects for the files in storage.
     *
     * @return The stream
     */
    private Stream<ImageFileData> imageFileStream() {
        File[] files = directory.listFiles();
        if (files == null) {
            return Stream.empty();
        }
        return Arrays.stream(files).map(this::getImageFileData).filter(Objects::nonNull);
    }

    /**
     * Lists the image files in storage.
     *
     * @return The list
     */
    public List<ImageFileData> listImageFiles() {
        Log.d(TAG, "List of image files requested");
        return imageFileStream().collect(Collectors.toList());
    }

    /**
     * Calculates the squared distance between the image location and the specified point.
     *
     * @param img - Image data
     * @param lat - Latitude of point
     * @param lon - Longitude of point
     * @return The distance squared
     */
    private double distanceSq(ImageFileData img, double lat, double lon) {
        double lat2 = img.getLatitude();
        double lon2 = img.getLongitude();
        return Math.abs(lat * lat + lon * lon - lat2 * lat2 - lon2 * lon2);
    }

    /**
     * Lists the image files within a certain distance of a point.
     *
     * @param latitude  - Latitude of point
     * @param longitude - Longitude of point
     * @param distance  - Maximum distance from the point
     * @return The list
     */
    public List<ImageFileData> listImageFilesNearPoint(double latitude, double longitude, double distance) {
        Log.d(TAG, "List of nearby image files requested");
        return imageFileStream().filter(image -> distanceSq(image, latitude, longitude) <= distance * distance)
                .collect(Collectors.toList());
    }
}
