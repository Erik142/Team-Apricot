package com.teamapricot.projectwalking.model;

import androidx.room.RoomDatabase;

import com.teamapricot.projectwalking.model.dao.PhotoDao;
import com.teamapricot.projectwalking.model.dao.RouteDao;
import com.teamapricot.projectwalking.model.dao.UserDao;

@androidx.room.Database(entities = { Achievement.class, AchievementType.class, Photo.class, Route.class, User.class }, version = 3)
public abstract class Database extends RoomDatabase {
    public abstract PhotoDao photoDao();
    public abstract UserDao userDao();
    public abstract RouteDao routeDao();
}
