package com.teamapricot.projectwalking.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamapricot.projectwalking.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM Photos LIMIT 10")
    List<Photo> getAllPhotos();

    @Query("SELECT COUNT(*) FROM Photos")
    int getNrPhotos();

    @Insert
    void insertPhotos(Photo... photos);
}
