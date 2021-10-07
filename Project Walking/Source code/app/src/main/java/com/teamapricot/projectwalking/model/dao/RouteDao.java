package com.teamapricot.projectwalking.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.teamapricot.projectwalking.model.Route;

@Dao
public interface RouteDao {
    @Insert
    void insertAll(Route... routes);
}
