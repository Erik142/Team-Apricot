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
 *
 * Database access object for Photos table.
 */
@Dao
public interface PhotoDao {
    /**
     * Get all photos after the specified photoId.
     * @param photoId The photoId
     */
    @Query("SELECT * FROM Photos WHERE photoId > :photoId")
    List<Photo> getPhotosAfter(long photoId);

    /**
     * Get all photos from database.
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
     * Get the number of rows in photo table.
     */
    @Query("SELECT COUNT(*) FROM Photos")
    int getNrPhotos();

    /**
     * Get photo by filename.
     * @param filename The filename to look for
     */
    @Query("SELECT * FROM Photos WHERE filename = :filename")
    Photo getPhotoByFilename(String filename);

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
}
