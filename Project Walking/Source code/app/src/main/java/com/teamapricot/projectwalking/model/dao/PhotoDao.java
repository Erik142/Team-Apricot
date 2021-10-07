package com.teamapricot.projectwalking.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamapricot.projectwalking.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM Photos")
    List<Photo> getAllPhotos();
    @Insert
    void insertPhotos(Photo... photos);
}
