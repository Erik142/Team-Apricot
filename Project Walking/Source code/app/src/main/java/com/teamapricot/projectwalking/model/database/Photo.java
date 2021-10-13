package com.teamapricot.projectwalking.model.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for representing a photo.
 */

@Entity(tableName = "Photos", primaryKeys = {"photoId"}, foreignKeys =
        {@ForeignKey(entity = Route.class,
                        parentColumns = "routeId",
                        childColumns = "photoId",
                        onDelete = ForeignKey.CASCADE)})
public class Photo {
    @NonNull
    private String photoId;
    @ColumnInfo(name = "filename")
    private String filename;

    public String getFilename() { return this.filename; }

    public String getPhotoId() { return this.photoId; }

    public void setFilename(String filename) { this.filename = filename; }

    public void setPhotoId(String photoId) { this.photoId = photoId; }
}
