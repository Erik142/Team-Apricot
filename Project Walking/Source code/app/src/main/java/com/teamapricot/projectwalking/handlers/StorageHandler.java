package com.teamapricot.projectwalking.handlers;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.Photo;
import com.teamapricot.projectwalking.model.database.Route;
import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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

    private static StorageHandler instance;

    private PhotoDao photoDao = null;
    private RouteDao routeDao = null;

    private File lastSavedPhoto = null;
    /**
     * Private constructor.
     * @param activity The associated activity
     */
    private StorageHandler(AppCompatActivity activity) {
        this.activity = activity;
        try {
            Database database = Database.getDatabase(activity.getApplicationContext()).get();
            photoDao = database.photoDao();
            routeDao = database.routeDao();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new instance of the {@code StorageHandler} class if necessary, using the
     * specified {@code AppCompatActivity}. If not necessary, it just returns the existing
     * instance.
     * @param activity The activity used for retrieving external storage directory paths
     */
    public static StorageHandler getInstance(AppCompatActivity activity) {
        if(instance == null) {
            instance = new StorageHandler(activity);
        }
        return instance;
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

        Route route = getOpenRoute();
        photoDao.insertPhoto(new Photo(outputFile.toString(), route.getRouteId()));
        route.setDone(true);
        routeDao.updateOne(route);
        lastSavedPhoto = outputFile;

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
     * Data about a photo.
     */
    public static class PhotoWithLocation {
        private final long photoId;
        private final String filename;
        private final double latitude;
        private final double longitude;
        private final long routeId;

        public PhotoWithLocation(long photoId, String filename, double latitude,
                                 double longitude, long routeId) {
            this.photoId = photoId;
            this.filename = filename;
            this.latitude = latitude;
            this.longitude = longitude;
            this.routeId = routeId;
        }

        public long getPhotoId() {
            return photoId;
        }

        public String getFilename() {
            return filename;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public long getRouteId() {
            return routeId;
        }
    }

    /**
     * Lists the photos in storage.
     * @return The list
     */
    public List<Photo> listPhotos() {
        Log.d(TAG, "List of photos requested");
        return photoDao.getAllPhotos();
    }

    /**
     * Lists the photos in storage with added location info.
     * @return The list
     */
    public List<PhotoWithLocation> listPhotosWithLocation() {
        Log.d(TAG, "List of photos with location requested");
        ArrayList<PhotoWithLocation> photosWithLocation = new ArrayList<>();
        for(Photo photo : listPhotos()) {
            PhotoWithLocation pwl = getPhotoWithLocation(photo);
            if(pwl == null) {
                continue;
            }
            photosWithLocation.add(pwl);
        }
        return photosWithLocation;
    }

    /**
     * Get an open route from the database (if one exists).
     */
    public Route getOpenRoute() {
        return routeDao.getOpenRoute();
    }

    /**
     * Get the last photo from the database.
     */
    public Photo getLastPhoto() {
        if(lastSavedPhoto == null) {
            return null;
        }
        return photoDao.getPhotoByFilename(lastSavedPhoto.toString());
    }

    /**
     * Get routeId and location for photo.
     * @param photo
     * @return A photo with extra data
     */
    public PhotoWithLocation getPhotoWithLocation(Photo photo) {
        if(photo == null) {
            return null;
        }
        Route route = routeDao.getRouteById(photo.getRouteId());
        if(route == null) {
            return null;
        }
        return new PhotoWithLocation(photo.getPhotoId(), photo.getFilename(),
                                     route.getEndX(), route.getEndY(), route.getRouteId());
    }
}
