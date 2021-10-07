package com.teamapricot.projectwalking.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.teamapricot.projectwalking.observe.ObservableBase;

import java.io.File;


@Entity(tableName = "Photos", primaryKeys = {"userId", "routeId"}, foreignKeys =
        {@ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
         @ForeignKey(entity = Route.class,
                 parentColumns = "routeId",
                 childColumns = "routeId",
                 onDelete = ForeignKey.CASCADE)})
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
    @ColumnInfo(name = "filename")
    private String filename;
    private CameraStatus status = CameraStatus.Unknown;


    public CameraModel(File imageFile, CameraStatus status){
        this.imageFile = imageFile;
        this.filename = imageFile.getName();
        this.status = status;
    }
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
