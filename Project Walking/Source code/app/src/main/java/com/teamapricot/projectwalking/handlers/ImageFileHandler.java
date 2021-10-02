package com.teamapricot.projectwalking.handlers;

import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Erik Wahlberger
 * @version 2021-09-18
 *
 * Used to create sharable image files, as well as for moving files to the external storage on the device
 */
public class ImageFileHandler {
    private static final String TAG = "ImageFileHandler";

    private final String PHOTO_SUBFOLDER = "ProjectWalking";
    private final String FILE_NAME_FORMAT = "yyyyMMddHHmmssSSS";

    private AppCompatActivity activity;

    /**
     * Creates a new instance of the {@code ImageFileHandler} class, using the specified {@code AppCompatActivity}
     * @param activity The activity used for retrieving external storage directory paths
     */
    public ImageFileHandler(AppCompatActivity activity) {
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
        File cameraDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File outputDirectoryPath = new File(cameraDirectoryPath, PHOTO_SUBFOLDER);

        outputDirectoryPath.mkdirs();

        String fileName = file.getName();
        File outputFile = File.createTempFile(fileName, ".jpg", outputDirectoryPath);

        try (FileChannel inputChannel = new FileInputStream(file).getChannel(); FileChannel outputChannel = new FileOutputStream(outputFile).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        }

        file.delete();

        return outputFile;
    }
}
