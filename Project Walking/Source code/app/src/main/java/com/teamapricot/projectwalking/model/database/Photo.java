package com.teamapricot.projectwalking.model.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for representing a photo.
 */

@Entity(tableName = "Photos",
        indices = {@Index(value = "routeId", unique = true),
                   @Index(value = "filename", unique = true)},
        foreignKeys = {@ForeignKey(entity = Route.class,
                                   parentColumns = "routeId",
                                   childColumns = "routeId",
                                   onDelete = ForeignKey.CASCADE)})
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private long photoId;
    private String filename;
    private long routeId;

    public Photo(String filename, long routeId) {
        this.filename = filename;
        this.routeId = routeId;
    }

    public long getPhotoId() { return photoId; }

    public String getFilename() { return filename; }

    public long getRouteId() { return routeId; }

    public void setPhotoId(long photoId) { this.photoId = photoId; }

    public void setFilename(String filename) { this.filename = filename; }

    public void setRouteId(long routeId) { this.routeId = routeId; }
}
