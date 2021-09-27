package com.teamapricot.projectwalking.photos;

import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Erik Wahlberger, Daniel Brännvall
 * @version 2021-09-27
 *
 * Handles access to the external storage, with methods for creating sharable images and listing
 * images.
 */
public class StorageHandler {
    private static final String TAG = "StorageHandler";

    private static final String PHOTO_SUBFOLDER = "ProjectWalking";
    private static final String FILE_NAME_FORMAT = "yyyyMMddHHmmssSSS";

    private final AppCompatActivity activity;

    /**
     * Creates a new instance of the {@code ImageFileHandler} class, using the specified {@code AppCompatActivity}
     * @param activity The activity used for retrieving external storage directory paths
     */
    public StorageHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Create a sharable image file with the file creation time as the file name
     * @return A {@code File} object for the created file
     * @throws IOException If the file can not be created
     */
    public File createSharableImageFile() throws IOException {
        String fileName = new SimpleDateFormat(FILE_NAME_FORMAT, Locale.getDefault()).format(new Date());
        File cameraDirectoryPath = this.activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File outputDirectoryPath = new File(cameraDirectoryPath, PHOTO_SUBFOLDER);

        outputDirectoryPath.mkdirs();

        File outputFile = File.createTempFile(fileName, ".jpg", outputDirectoryPath);
        Log.d(TAG, "Temp file path: " + outputFile.getAbsolutePath());

        return outputFile;
    }

    /**
     * Moves a file to the external storage on the device
     * @param file The file that will be moved to the external storage
     * @return A {@code File} object for the file on the external storage
     * @throws IOException If the file cannot be created on the external storage
     */
    public File moveImageToExternalStorage(File file) throws IOException {
        File outputDirectoryPath = getImageDirectory();

        outputDirectoryPath.mkdirs();

        String fileName = file.getName();
        File outputFile = File.createTempFile(fileName, ".jpg", outputDirectoryPath);

        try (FileChannel inputChannel = new FileInputStream(file).getChannel(); FileChannel outputChannel = new FileOutputStream(outputFile).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        }

        file.delete();

        return outputFile;
    }

    /**
     * Gets the directory where images are stored.
     *
     * @return The directory
     */
    public static File getImageDirectory() {
        File cameraDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(cameraDirectoryPath, PHOTO_SUBFOLDER);
    }

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
        File[] files = getImageDirectory().listFiles();
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
