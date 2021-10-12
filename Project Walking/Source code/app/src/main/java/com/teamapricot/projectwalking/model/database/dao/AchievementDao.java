package com.teamapricot.projectwalking.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.teamapricot.projectwalking.model.database.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAchievements(Achievement... Achievements);

    @Query("UPDATE Achievement SET status = 1 WHERE ((value <= :input) AND" +
            "(type = '1'))")
    void updateAchievementsDistance(double input);

    @Query("UPDATE Achievement SET status = 1 WHERE ((value <= :input) AND" +
            "(type = '2'))")
    void updateAchievementsPhotos(int input);

    @Query("SELECT * FROM Achievement WHERE status = 1 ORDER BY nr DESC LIMIT 10")
    List<Achievement> getLatestAchievements();

    @Query("SELECT * FROM Achievement WHERE status = 1")
    List<Achievement> getAllAchievements();
}
