package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.observe.ObservableBase;

import java.io.File;

public class CaptureImageModel extends ObservableBase<CaptureImageModel> {
    private File imageFile;
    private boolean success;
    private boolean finished;

    public boolean isFinished() {
        return finished;
    }

    public boolean isSuccessful() {
        return success;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File file) {
        if (file != imageFile) {
            this.imageFile = file;
            setSuccess();
            updateObservers(this);
        }
    }

    public void setFinished(boolean finished) {
        if (this.finished != finished) {
            this.finished = finished;
            setSuccess();
            updateObservers(this);
        }
    }

    private void setSuccess() {
        this.success = this.finished && this.imageFile != null;
    }

    public void reset() {
        this.imageFile = null;
        this.finished = false;
        this.success = false;
    }
}
