package com.teamapricot.projectwalking.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.teamapricot.projectwalking.model.database.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {

    @Insert
    void insertAchievements(Achievement... Achievements);

    @Query("UPDATE Achievement SET status = 'true' WHERE ((value >= :input) AND" +
            "(type = :inputType))")
    void updateAchievements(int input, int inputType);

    @Query("SELECT * FROM Achievement ORDER BY nr DESC LIMIT 10")
    List<Achievement> getLatestAchievements();

}
