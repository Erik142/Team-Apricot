package com.teamapricot.projectwalking.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
     * Insert a new route.
     */
    @Insert
    Long insertOne(Route route);

    /**
     * Insert new routes.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Route... routes);

    /**
     * Get the total distance from routes table.
     */
    @Query("SELECT SUM(distance) FROM Routes")
    double getTotalDist();

    /**
     * Get the {@code limit} latest routes from route table.
     * @param limit The number of routes to get
     */
    @Query("SELECT * FROM Routes ORDER BY routeId DESC LIMIT :limit")
    List<Route> getLatestRoutes(long limit);

    /**
     * Update observers when unfinished routes change.
     */
    @Query("SELECT * FROM Routes WHERE done = 0")
    LiveData<List<Route>> getNotDoneRoutesLive();
}
