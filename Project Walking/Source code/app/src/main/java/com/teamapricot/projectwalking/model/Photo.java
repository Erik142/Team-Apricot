package com.teamapricot.projectwalking.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Photos", primaryKeys = {"userId", "photoId"}, foreignKeys =
        {@ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Route.class,
                        parentColumns = "routeId",
                        childColumns = "photoId",
                        onDelete = ForeignKey.CASCADE)})
public class Photo {
    @NonNull
    private String photoId;
    @NonNull
    private String userId;
    @ColumnInfo(name = "filename")
    private String filename;

    public String getFilename() { return this.filename; }

    public String getPhotoId() { return this.photoId; }

    public String getUserId() { return this.userId; }

    public void setFilename(String filename) { this.filename = filename; }

    public void setPhotoId(String photoId) { this.photoId = photoId; }

    public void setUserId(String userId) { this.userId = userId; }
}
