package com.teamapricot.projectwalking.model;

import androidx.room.RoomDatabase;

import com.teamapricot.projectwalking.model.dao.PhotoDao;

@androidx.room.Database(entities = { Achievement.class, AchievementType.class, Photo.class, Route.class, User.class }, version = 2)
public abstract class Database extends RoomDatabase {
    public abstract PhotoDao photoDao();
}
