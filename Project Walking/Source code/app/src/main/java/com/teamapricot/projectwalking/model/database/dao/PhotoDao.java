package com.teamapricot.projectwalking.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
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
     * Get the 10 latest photo filenames from database.
     */
    @Query("SELECT * FROM Photos LIMIT 10")
    List<Photo> getAllPhotos();

    /**
     * Get the number of rows in phototable.
     */
    @Query("SELECT COUNT(*) FROM Photos")
    int getNrPhotos();

    /**
     * Insert new photos.
     */
    @Insert
    void insertPhotos(Photo... photos);

}
