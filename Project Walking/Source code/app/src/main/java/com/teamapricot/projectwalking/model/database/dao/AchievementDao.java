package com.teamapricot.projectwalking.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamapricot.projectwalking.model.database.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {

    @Insert
    void insertAchievements(Achievement... Achievements);

    @Query("SELECT * FROM Achievement ORDER BY nr DESC LIMIT 10")
    List<Achievement> getLatestAchievements();

}
