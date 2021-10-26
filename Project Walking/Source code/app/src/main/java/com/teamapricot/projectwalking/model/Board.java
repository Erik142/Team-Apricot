package com.teamapricot.projectwalking.model;

import android.util.Log;

import com.teamapricot.projectwalking.model.database.Achievement;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.Photo;
import com.teamapricot.projectwalking.model.database.Route;
import com.teamapricot.projectwalking.model.database.dao.AchievementDao;
import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;
import com.teamapricot.projectwalking.observe.ObservableBase;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for representing the board.
 */
public class Board extends ObservableBase<Board> {

    private PhotoDao ph;
    private RouteDao rd;
    private AchievementDao ad;
    private double rDist;
    private int nrPhotos;
    private List<Route> routes;
    private List<Photo> photos;
    private List<Achievement> achievements;


    /**
     * Constructor for creating Board-object.
     *
     * @param ph database access object for getting photo-objects from database.
     * @param rd database access object for getting Route-objects from database.
     */
    public Board(PhotoDao ph, RouteDao rd, AchievementDao ad) {
        this.ph = ph;
        this.rd = rd;
        this.ad = ad;
    }

    public void init() {
        try {
            this.rDist = Database.performQuery(rd::getTotalDist).get();
            this.nrPhotos = Database.performQuery(ph::getNrPhotos).get();
            this.routes = Database.performQuery(() -> rd.getLatestRoutes(10)).get();
            this.photos = Database.performQuery(() -> ph.getLatestPhotos(10)).get();
            updateAchievements(rDist, nrPhotos);
            this.achievements = Database.performQuery(() -> ad.getAllAchievements()).get();
            updateObservers(this);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Achievement> getAchievements() {
        return this.achievements;
    }

    public double getTotalDistance() {
        return this.rDist;
    }

    public int getNumberOfPhotos() {
        return this.nrPhotos;
    }

    /**
     * Method for checking for and updating achievements.
     */
    private void updateAchievements(double distance, int nrPhotos) {
        try {
            Database.performQuery(() -> ad.updateAchievementsDistance(distance)).get();
            Database.performQuery(() -> ad.updateAchievementsPhotos(nrPhotos)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void insertAchievementData() {

    }
}
