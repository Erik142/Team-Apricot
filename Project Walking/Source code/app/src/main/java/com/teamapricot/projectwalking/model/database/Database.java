package com.teamapricot.projectwalking.model.database;

import androidx.room.RoomDatabase;

import com.teamapricot.projectwalking.model.dao.PhotoDao;
import com.teamapricot.projectwalking.model.dao.RouteDao;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for creating the database.
 * When adding new tables add dao's and class names here.
 */

@androidx.room.Database(entities = { Achievement.class, AchievementType.class, Photo.class, Route.class}, version = 4)
public abstract class Database extends RoomDatabase {
    public abstract PhotoDao photoDao();
    public abstract RouteDao routeDao();
}
