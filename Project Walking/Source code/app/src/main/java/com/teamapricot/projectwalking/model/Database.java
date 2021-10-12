package com.teamapricot.projectwalking.model;

import androidx.room.RoomDatabase;

import com.teamapricot.projectwalking.model.dao.PhotoDao;
import com.teamapricot.projectwalking.model.dao.RouteDao;

@androidx.room.Database(entities = { Achievement.class, AchievementType.class, Photo.class, Route.class}, version = 4)
public abstract class Database extends RoomDatabase {
    public abstract PhotoDao photoDao();
    public abstract RouteDao routeDao();
}
