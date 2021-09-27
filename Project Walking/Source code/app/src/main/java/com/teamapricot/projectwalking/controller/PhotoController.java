package com.teamapricot.projectwalking.controller;

import android.Manifest;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.teamapricot.projectwalking.BuildConfig;
import com.teamapricot.projectwalking.PermissionHandler;
import com.teamapricot.projectwalking.dialogs.PermissionRejectedDialog;
import com.teamapricot.projectwalking.handlers.CameraHandler;
import com.teamapricot.projectwalking.photos.ImageFileHandler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Erik Wahlberger
 * @version 2021-09-18
 *
 * A controller class (In the MVC sense) for taking photos using the camera of the device
 */
public class PhotoController {
    private final String TAG = "PhotoController";
    private final String FILE_PROVIDER_SUFFIX = ".fileprovider";

    private PermissionHandler permissionHandler;
    private CameraHandler cameraHandler;
    private ImageFileHandler imageFileHandler;
    private AppCompatActivity activity;

    /**
     * Creates a new {@code PhotoController} instance using the specified {@code AppCompatActivity}, {@code PermissionHandler} and {@code CameraHandler}
     *
     * @param activity The {@code AppCompatActivity} used for creating dialogs, retrieving directory paths and creating {@code Toast}s
     * @param permissionHandler The {@code PermissionHandler} used to check for, and request app permissions
     * @param cameraHandler The {@code CameraHandler} used to open the camera app, as well as to capture and save images
     */
    public PhotoController(AppCompatActivity activity, PermissionHandler permissionHandler, CameraHandler cameraHandler) {
        this.activity = activity;
        this.permissionHandler = permissionHandler;
        this.cameraHandler = cameraHandler;
        this.imageFileHandler = new ImageFileHandler(this.activity);
    }

    /**
     * Requests the necessary permissions, opens the camera applications, captures and saves an image to the device asynchronously
     */
    public void captureImageAsync() {
        requestPermissions().thenAccept(isGranted -> {
            if (isGranted) {
                Log.d(TAG, "All required permissions have been accepted.");
                openCameraAndCaptureImageAsync();
            }
            else {
                Log.d(TAG, "All required permissions have not been accepted.");
                PermissionRejectedDialog dialog = new PermissionRejectedDialog(this.activity,
                        "Permission to write to the external storage is needed so that photos can be saved properly. Please grant access to files and media.");

                dialog.show(activity.getSupportFragmentManager(), TAG);
            }
        });
    }

    /**
     * Opens the camera application, captures an image and saves it to the device asynchronously
     */
    private void openCameraAndCaptureImageAsync() {
        File tempFile;
        Uri uri;

        try {
            tempFile = imageFileHandler.createSharableImageFile();
            uri = FileProvider.getUriForFile(this.activity, BuildConfig.APPLICATION_ID + FILE_PROVIDER_SUFFIX, tempFile);
        } catch (IOException | IllegalArgumentException e) {
            Log.e(TAG, "An error occurred while creating temporary image file: " + e.getMessage());

            this.activity.runOnUiThread(() -> {
                Toast toast = Toast.makeText(this.activity, "An error occurred while creating temporary image file", Toast.LENGTH_LONG);
                toast.show();
            });

            return;
        }

        cameraHandler.takePhoto(uri).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "Successfully captured image!");
                try {
                    File outputFile = imageFileHandler.moveImageToExternalStorage(tempFile);

                    this.activity.runOnUiThread(() -> {
                        Toast toast = Toast.makeText(this.activity, "Nice photo!", Toast.LENGTH_LONG);
                        toast.show();
                    });

                    Log.d(TAG, "Successfully saved image to external storage: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e(TAG, "An error occurred while copying image to external storage: " + e.getMessage());

                    if (tempFile.exists()) {
                        tempFile.delete();
                    }

                    this.activity.runOnUiThread(() -> {
                        Toast toast = Toast.makeText(this.activity, "An error occurred while copying image to external storage", Toast.LENGTH_LONG);
                        toast.show();
                    });
                }
                catch (Exception e) {
                    Log.e(TAG, "An error occurred while showing Toast: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Something went wrong when capturing the image");

                this.activity.runOnUiThread(() -> {
                    Toast toast = Toast.makeText(this.activity, "Something went wrong when capturing the image", Toast.LENGTH_LONG);
                    toast.show();
                });
            }
        });
    }

    /**
     * Requests the necessary permissions for the camera functionality
     * @return A {@code CompletableFuture} object with a {@code Boolean} result. The {@code Boolean} is set to {@code true} if the required permissions have been granted, and {@code false} otherwise
     */
    private CompletableFuture<Boolean> requestPermissions() {
        boolean isWriteStoragePermissionGranted = this.permissionHandler.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!isWriteStoragePermissionGranted) {
            return this.permissionHandler.requestPermissionAsync(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        return CompletableFuture.supplyAsync(() -> true);
    }
}
