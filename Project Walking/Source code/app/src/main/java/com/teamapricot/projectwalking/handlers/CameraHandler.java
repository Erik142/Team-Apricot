package com.teamapricot.projectwalking.handlers;

import android.net.Uri;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.handlers.ActivityResultHandler;

import java.util.concurrent.CompletableFuture;

/**
 * @author Erik Wahlberger
 * @version 2021-09-18
 *
 * CameraHandler is used for starting an external camera application,
 * capturing an image and finally saving that image to the device
 */
public class CameraHandler extends ActivityResultHandler<Uri, Boolean> {

    /**
     * Creates a new CameraHandler object using the given parameter for registering the activity result
     * @param activity The activity that will be used for registering activity result
     */
    public CameraHandler(AppCompatActivity activity) {
        super(activity, new ActivityResultContracts.TakePicture());
    }

    /**
     * Method used to start the camera application, capture an image and save it to the device
     * @param filePathUri The {@code Uri} to the path where the image will be saved
     * @return A {@code CompletableFuture} object, containing a {@code Boolean} value which will be set to {@code true} if the operation succeeded, or {@code false} if an error occurred
     */
    public CompletableFuture<Boolean> takePhoto(Uri filePathUri) {
        return this.launchSingle(filePathUri);
    }
}
