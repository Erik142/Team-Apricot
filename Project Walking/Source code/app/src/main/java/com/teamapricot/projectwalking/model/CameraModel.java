package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.observe.ObservableBase;

import java.io.File;

/**
 * @author Erik Wahlberger
 * @version 2021-09-29
 */
public class CameraModel extends ObservableBase<CameraModel> {
    /**
     * Enumeration used to store the current status of the external camera application
     */
    public enum CameraStatus {
        Unknown,
        Discarded,
        Done,
        ErrorSavingFinalPhoto,
    }

    private File imageFile;
    private CameraStatus status = CameraStatus.Unknown;

    /**
     * Retrieves the taken image file in the camera application
     * @return The image file as a @{code File} object
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * Retrieves the current camera status
     * @return the status as a {@code CameraStatus} enumeration
     */
    public CameraStatus getStatus() {
        return status;
    }

    /**
     * Updates the value for the image file
     * @param file The new value for the image file
     */
    public void setImageFile(File file) {
        if (file != imageFile) {
            this.imageFile = file;
            updateObservers(this);
        }
    }

    /**
     * Updates the camera status
     * @param status The new value for the camera status
     */
    public void setStatus(CameraStatus status) {
        this.status = status;
        updateObservers(this);
    }

    /**
     * Resets the model to its initial state.
     */
    public void reset() {
        this.status = CameraStatus.Unknown;
        this.imageFile = null;
    }
}
