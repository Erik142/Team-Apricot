package com.teamapricot.projectwalking.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.teamapricot.projectwalking.model.database.Photo;

import java.util.List;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Database access-object for Photos-table.
 */
@Dao
public interface PhotoDao {
    /**
     * Get all photo filenames from database.
     */
    @Query("SELECT * FROM Photos")
    List<Photo> getAllPhotos();

    /**
     * Get the {@code limit} latest photo filenames from database.
     * @param limit The number of photos to get
     */
    @Query("SELECT * FROM Photos ORDER BY photoId DESC LIMIT :limit")
    List<Photo> getLatestPhotos(long limit);

    /**
     * Get the number of rows in phototable.
     */
    @Query("SELECT COUNT(*) FROM Photos")
    int getNrPhotos();

    /**
     * Insert a new photo.
     */
    @Insert
    long insertPhoto(Photo photo);

    /**
     * Insert new photos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPhotos(Photo... photos);

    /**
     * Get all photos and watch for updates.
     */
    @Query("SELECT * FROM Photos ORDER BY photoId DESC")
    LiveData<List<Photo>> getPhotosLive();
}
