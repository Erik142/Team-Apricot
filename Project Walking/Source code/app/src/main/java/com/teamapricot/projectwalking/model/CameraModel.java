package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.observe.ObservableBase;

import java.io.File;

/**
 * @author Erik Wahlberger
 * @version 2021-09-29
 */
public class CameraModel extends ObservableBase<CameraModel> {
    public enum CameraStatus {
        Unknown,
        Discarded,
        Done,
        ErrorSavingFinalPhoto,
    }

    private File imageFile;
    private CameraStatus status = CameraStatus.Unknown;

    public File getImageFile() {
        return imageFile;
    }

    public CameraStatus getStatus() {
        return status;
    }

    public void setImageFile(File file) {
        if (file != imageFile) {
            this.imageFile = file;
            updateObservers(this);
        }
    }

    public void setStatus(CameraStatus status) {
        this.status = status;
        updateObservers(this);
    }

    public void reset() {
        this.status = CameraStatus.Unknown;
        this.imageFile = null;
    }
}
