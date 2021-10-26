package com.teamapricot.projectwalking.model.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.teamapricot.projectwalking.model.database.dao.AchievementDao;
import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 *
 * Class for creating the database.
 * When adding new tables add dao's and class names here.
 */

@androidx.room.Database(entities = { Achievement.class, Photo.class, Route.class}, version = 3)
public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME = "fun-walking-database";

    private static Database database;
    private static ConcurrentLinkedQueue<UUID> queuedQueries = new ConcurrentLinkedQueue<>();

    public abstract PhotoDao photoDao();
    public abstract RouteDao routeDao();
    public abstract AchievementDao achievementDao();

    /**
     * Retrieves the current {@link Database} instance
     * @param context The {@link Context} from which to create the {@link Database} instance if it does not exists
     * @return A {@link CompletableFuture} object containing the {@link Database} instance.
     */
    public static CompletableFuture<Database> getDatabase(Context context) {
        return CompletableFuture.supplyAsync(() -> {
            if (database == null) {
                database = Room.databaseBuilder(context, Database.class, DATABASE_NAME)
                               .fallbackToDestructiveMigration()
                               .build();
            }

            return database;
        }, Executors.newSingleThreadExecutor());
    }

    public static <T> CompletableFuture<T> performQuery(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = UUID.randomUUID();
            queuedQueries.add(uuid);
            waitForQueries(uuid);
            T value = supplier.get();
            queuedQueries.poll();
            return value;
        }, Executors.newSingleThreadExecutor());
    }

    public static CompletableFuture<Void> performQuery(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            UUID uuid = UUID.randomUUID();
            queuedQueries.add(uuid);
            waitForQueries(uuid);
            runnable.run();
            queuedQueries.poll();
        }, Executors.newSingleThreadExecutor());
    }


    private static void waitForQueries(UUID uuid) {
        while (!queuedQueries.peek().equals(uuid)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                continue;
            }
        }

        return;
    }
}

