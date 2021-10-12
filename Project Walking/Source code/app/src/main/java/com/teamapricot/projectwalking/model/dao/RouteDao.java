package com.teamapricot.projectwalking.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamapricot.projectwalking.model.Route;

import java.util.List;

@Dao
public interface RouteDao {
    @Insert
    void insertAll(Route... routes);

    @Query("SELECT SUM(distance) FROM Routes")
    Double getTotalDist();
    @Query("SELECT * FROM Routes LIMIT 10")
    List<Route> getLatestRoutes();
}
