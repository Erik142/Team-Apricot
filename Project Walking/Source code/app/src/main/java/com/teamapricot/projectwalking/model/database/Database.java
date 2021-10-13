package com.teamapricot.projectwalking.model.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for creating the database.
 * When adding new tables add dao's and class names here.
 */

@androidx.room.Database(entities = { Achievement.class, AchievementType.class, Photo.class, Route.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME = "fun-walking-database";

    private static Database database;

    public abstract PhotoDao photoDao();
    public abstract RouteDao routeDao();

    public static CompletableFuture<Database> getDatabase(Context context) {
        return CompletableFuture.supplyAsync(() -> {
            if (database == null) {
                database = Room.databaseBuilder(context, Database.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                database.clearAllTables();
            }

            return database;
        }, Executors.newSingleThreadExecutor());
    }
}

