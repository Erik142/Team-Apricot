package com.teamapricot.projectwalking.controller;

import android.Manifest;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.teamapricot.projectwalking.BuildConfig;
import com.teamapricot.projectwalking.handlers.PermissionHandler;
import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.model.CameraModel;
import com.teamapricot.projectwalking.observe.Observer;
import com.teamapricot.projectwalking.view.dialogs.PermissionRejectedDialog;
import com.teamapricot.projectwalking.handlers.CameraHandler;
import com.teamapricot.projectwalking.handlers.ImageFileHandler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Erik Wahlberger
 * @version 2021-09-27
 *
 * A controller class (In the MVC sense) for using the camera of the device
 */
public class CameraController {
    private final String TAG = "PhotoController";
    private final String FILE_PROVIDER_SUFFIX = ".fileprovider";

    private PermissionHandler permissionHandler;
    private CameraHandler cameraHandler;
    private StorageHandler storageHandler;
    private AppCompatActivity activity;

    private CameraModel cameraModel;

    /**
     * Creates a new {@code CameraController} instance using the specified {@code AppCompatActivity}, {@code PermissionHandler} and {@code CameraHandler}
     *
     * @param activity The {@code AppCompatActivity} used for creating dialogs, retrieving directory paths and creating {@code Toast}s
     */
    public CameraController(AppCompatActivity activity) {
        this.activity = activity;
<<<<<<< HEAD:Project Walking/Source code/app/src/main/java/com/teamapricot/projectwalking/photos/PhotoController.java
        this.permissionHandler = permissionHandler;
        this.cameraHandler = cameraHandler;
        this.storageHandler = new StorageHandler(this.activity);
=======
        this.permissionHandler = new PermissionHandler(this.activity);
        this.cameraHandler = new CameraHandler(this.activity);
        this.imageFileHandler = new ImageFileHandler(this.activity);
        this.cameraModel = new CameraModel();
    }

    /**
     * Registers an OnClickListener to open the external camera application
     * @param button The button that should be used to open the camera application
     */
    public void registerOnClickListener(View button) {
        if (!button.hasOnClickListeners()) {
            button.setOnClickListener(view -> {
                if (view.getId() == R.id.open_camera_fab) {
                    captureImageAsync();
                }
            });
        }
    }

    /**
     * Registers the specified observer in the corresponding {@code CameraModel}
     * @param observer The observer that should be registered
     */
    public void registerObserver(Observer<CameraModel> observer) {
        cameraModel.addObserver(observer);
>>>>>>> origin/master:Project Walking/Source code/app/src/main/java/com/teamapricot/projectwalking/controller/CameraController.java
    }

    /**
     * Requests the necessary permissions, opens the camera applications, captures and saves an image to the device asynchronously
     */
    private void captureImageAsync() {
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
        cameraModel.reset();

        File tempFile;
        Uri uri;

        try {
            tempFile = storageHandler.createSharableImageFile();
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
                    File outputFile = storageHandler.moveImageToExternalStorage(tempFile);

                    cameraModel.setImageFile(outputFile);
                    cameraModel.setStatus(CameraModel.CameraStatus.Done);

                    Log.d(TAG, "Successfully saved image to external storage: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e(TAG, "An error occurred while copying image to external storage: " + e.getMessage());
                    cameraModel.setStatus(CameraModel.CameraStatus.ErrorSavingFinalPhoto);

                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                }
                catch (Exception e) {
                    Log.e(TAG, "An error occurred while showing Toast: " + e.getMessage());
                }
            } else {
                Log.d(TAG, "The camera was discarded.");
                cameraModel.setStatus(CameraModel.CameraStatus.Discarded);
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
