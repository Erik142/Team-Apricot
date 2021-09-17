package com.teamapricot.projectwalking;

import android.content.pm.PackageManager;
import android.os.SystemClock;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Erik Wahlberger
 * @version 2021-09-17
 *
 * PermissionHandler is used to check for already granted permissions, as well as
 * request new permissions in an easier way than by using the standard implementation by Google.
 * Make sure to add each permission to the AndroidManifest.xml using {@literal <uses-permission android:name="..." />}.
 */
public class PermissionHandler {
    private final String TAG = "PermissionHandler";

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Boolean currentPermissionResult;
    private ReentrantLock currentPermissionResultLock;
    private AppCompatActivity activity;

    /**
     * Creates a new permission handler for the specified AppCompatActivity.
     * @param activity The activity where the permission handler will be used
     */
    public PermissionHandler(AppCompatActivity activity) {
        currentPermissionResult = null;
        currentPermissionResultLock = new ReentrantLock();
        this.activity = activity;

        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            // Set currentPermissionResult when a permission has been granted or denied
            try {
                if (currentPermissionResultLock.tryLock()) {
                    currentPermissionResult = isGranted;
                }
            } finally {
                currentPermissionResultLock.unlock();
            }
        });
    }

    /**
     * Checks if multiple permissions have already been granted.
     * @param permissions The permissions to be checked. Can be found within PackageManager.permissions
     * @return A {@code Map<String, Boolean> }. The keys ({@code String}) represents each permission. The values ({@code Boolean}) represent the current permission status for specific permissions.
     * To check if a permission has already been granted or not, use the {@code Map} return value with the searched permission as the key.
     */
    public Map<String, Boolean> checkPermissions(String[] permissions) {
        Map<String, Boolean> permissionResults = new HashMap<>();

        for (String permission : permissions) {
            if (permissionResults.keySet().contains(permission)) {
                continue;
            }

            Boolean isGranted = ContextCompat.checkSelfPermission(this.activity, permission) == PackageManager.PERMISSION_GRANTED;
            permissionResults.put(permission, isGranted);
        }

        return permissionResults;
    }

    /**
     * Checks if a permission has already been granted.
     * @param permission The permission to be checked. Can be found within PackageManager.permissions
     * @return A {@code boolean} value representing whether or not the permission has already been granted.
     */
    public boolean checkPermission(String permission) {
        Map<String, Boolean> permissionResults = checkPermissions(new String[]{permission});

        if (permissionResults.keySet().contains(permission)) {
            return permissionResults.get(permission);
        }

        return false;
    }

    /**
     * Request a single permission asynchronously.
     * @param permissions The permissions to be requested. Can be found within PackageManager.permissions
     * @return A {@code CompletableFuture} object containing a {@code Map<String, Boolean>}.
     * Each key ({@code String}) represents a certain permission. Each value ({@code Boolean}) shows if a particular permission was granted or rejected.
     * To check if a permission was granted or not, use the map return value with the searched permission as the key.
     */
    public CompletableFuture<Map<String, Boolean>> requestPermissionsAsync(String[] permissions) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Boolean> permissionResults = new HashMap<>();

            for (String permission : permissions) {
                // Launch the permission launcher on the UI thread, otherwise the permission dialog is not shown on the screen
                this.activity.runOnUiThread(() -> {
                    requestPermissionLauncher.launch(permission);
                });

                waitForPermissionResult();

                try {
                    if (currentPermissionResultLock.tryLock()) {
                        permissionResults.put(permission, currentPermissionResult);
                        currentPermissionResult = null;
                    }
                } finally {
                    currentPermissionResultLock.unlock();
                }
            }

            return permissionResults;
        });
    }

    /**
     * Request a single permission asynchronously.
     * @param permission The permission to be requested. Can be found within PackageManager.permissions
     * @return A {@code CompletableFuture} object containing a {@code Boolean} value that shows if the permission request was granted or rejected
     */
    public CompletableFuture<Boolean> requestPermissionAsync(String permission) {
        return requestPermissionsAsync(new String[]{permission}).thenApply(permissionResults -> {
            if (permissionResults.keySet().contains(permission)) {
                return permissionResults.get(permission);
            }

            return false;
        });
    }

    /**
     * Wait for the callback to requestPermissionLauncher to be triggered
     */
    private void waitForPermissionResult() {
        while(true) {
            try {
                if (currentPermissionResultLock.tryLock()) {
                    if (currentPermissionResult != null) {
                        break;
                    }
                }
            } finally {
                currentPermissionResultLock.unlock();
            }

            SystemClock.sleep(500);
        }
    }
}
