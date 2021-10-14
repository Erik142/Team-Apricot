package com.teamapricot.projectwalking.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.teamapricot.projectwalking.model.database.Route;

import java.util.List;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Database access-object for Routes-table.
 */
@Dao
public interface RouteDao {

    /**
     * Insert new routes.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Route... routes);

    /**
     * Get the total distance from routes table.
     */
    @Query("SELECT SUM(distance) FROM Routes")
    double getTotalDist();

    /**
     * Get the 10 last routes from route table.
     */
    @Query("SELECT * FROM Routes LIMIT 10")
    List<Route> getLatestRoutes();
}
