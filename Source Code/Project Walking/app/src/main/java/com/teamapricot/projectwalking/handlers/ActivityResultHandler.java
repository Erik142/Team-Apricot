package com.teamapricot.projectwalking.handlers;

import android.os.SystemClock;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author Erik Wahlberger
 * @version 2021-09-18
 * @param <I> Type for the ActivityResultLauncher (Input type)
 * @param <O> Type for the parameter in the ActivityResultCallback (Output type)
 */
public abstract class ActivityResultHandler<I,O> {
    private static final int defaultSleepTimeMs = 500;
    private final int sleepTimeMs;
    private final String TAG = "ActivityResultHandler";

    protected AppCompatActivity activity;

    private ActivityResultLauncher<I> activityResultLauncher;
    private O outputResource = null;
    private ReentrantLock outputResourceLock;

    /**
     * Creates a new instance of ActivityResultHandler
     * @param activity The activity that will be used for registering activity results
     * @param activityResultContract The activity result contract (e.g. what should be done. Use {@code ActivityResultContracts.[...]} for pre-built contracts)
     * @param callback The callback function, used to modify the result received from the ActivityResultContract, before adding it to the return value
     * @param sleepTimeMs The time used to sleep (wait) for the result to be available before trying to access it again.
     */
    public ActivityResultHandler(AppCompatActivity activity, ActivityResultContract<I,O> activityResultContract, Function<O, O> callback, int sleepTimeMs) {
        this.sleepTimeMs = sleepTimeMs;
        outputResourceLock = new ReentrantLock();
        activity.runOnUiThread(()-> activityResultLauncher = activity.registerForActivityResult(activityResultContract, this.getActivityResultCallback(callback)));
        this.activity = activity;
    }

    /**
     * Creates a new instance of ActivityResultHandler, without any callback function
     * @param activity The activity that will be used for registering activity results
     * @param activityResultContract The activity result contract (e.g. what should be done. Use {@code ActivityResultContracts.[...]} for pre-built contracts)
     * @param sleepTimeMs The time used to sleep (wait) for the result to be available before trying to access it again.
     */
    public ActivityResultHandler(AppCompatActivity activity, ActivityResultContract<I,O> activityResultContract, int sleepTimeMs) {
        this(activity, activityResultContract, null, sleepTimeMs);
    }

    /**
     * Creates a new instance of ActivityResultHandler, with a sleep time of 500 ms
     * @param activity The activity that will be used for registering activity results
     * @param activityResultContract The activity result contract (e.g. what should be done. Use {@code ActivityResultContracts.[...]} for pre-built contracts)
     * @param callback The callback function, used to modify the result received from the ActivityResultContract, before adding it to the return value
     */
    public ActivityResultHandler(AppCompatActivity activity, ActivityResultContract<I,O> activityResultContract, Function<O, O> callback) {
        this(activity, activityResultContract, callback, defaultSleepTimeMs);
    }

    /**
     * Creates a new instance of ActivityResultHandler, without any callback function, and with a sleep time of 500 ms
     * @param activity The activity that will be used for registering activity results
     * @param activityResultContract The activity result contract (e.g. what should be done. Use {@code ActivityResultContracts.[...]} for pre-built contracts)
     */
    public ActivityResultHandler(AppCompatActivity activity, ActivityResultContract<I,O> activityResultContract) {
        this(activity, activityResultContract, null, defaultSleepTimeMs);
    }

    /**
     * Launches the ActivityResultLauncher for all the inputs in sequence
     * @param inputs Input values used for each execution of the ActivityResultLauncher
     * @return A {@code CompletableFuture} object, containing the results of the ActivityResultLauncher as a {@code Map}, where each input corresponds to one output
     */
    protected CompletableFuture<Map<I,O>> launch(I... inputs) {
        return CompletableFuture.supplyAsync(() -> {
            Map<I, O> output = new HashMap<>();

            for (I input : inputs) {
                O outputValue = null;
                activity.runOnUiThread(() -> activityResultLauncher.launch(input));
                waitForActivityResult();

                try {
                    if (outputResourceLock.tryLock()) {
                        outputValue = outputResource;
                        output.put(input, outputValue);
                        outputResource = null;
                    }
                } finally {
                    outputResourceLock.unlock();
                }
            }

            return output;
        });
    }

    /**
     * Launches the ActivityResultLauncher for a single input
     * @param input Input value used to launch the ActivityResultLauncher
     * @return A {@code CompletableFuture} object containing the return value of the ActivityResultLauncher
     */
    protected CompletableFuture<O> launchSingle(I input) {
        return launch(input).thenApply(outputs -> {
            if (outputs.keySet().contains(input)) {
                return outputs.get(input);
            }

            return null;
        });
    }

    /**
     * Creates an ActivityResultCallback from a generic {@code Function}
     * @param callback The callback to be used for modifying the result before using it
     * @return
     */
    private ActivityResultCallback<O> getActivityResultCallback(Function<O, O> callback) {
        return result -> {
            try {
                if (outputResourceLock.tryLock()) {
                    outputResource = callback != null ? callback.apply(result) : result;
                }
            } finally {
                outputResourceLock.unlock();
            }
        };
    }

    /**
     * Wait for the callback to activityResultLauncher to be triggered
     */
    private void waitForActivityResult() {
        while(true) {
            try {
                if (outputResourceLock.tryLock()) {
                    if (outputResource != null) {
                        break;
                    }
                }
            } finally {
                outputResourceLock.unlock();
            }

            SystemClock.sleep(sleepTimeMs);
        }
    }
}
