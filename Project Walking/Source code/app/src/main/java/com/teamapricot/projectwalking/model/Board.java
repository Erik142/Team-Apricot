package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.model.database.Photo;
import com.teamapricot.projectwalking.model.database.Route;
import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;
import com.teamapricot.projectwalking.observe.ObservableBase;

import java.util.List;

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
    private double rDist;
    private int nrPhotos;
    private List<Route> routes;
    private List<Photo> photos;


/**
* Constructor for creating Board-object.
 * @param ph database access object for getting photo-objects from database.
 * @param rd database access object for getting Route-objects from database.
*/
    public Board(PhotoDao ph, RouteDao rd){
        this.ph = ph;
        this.rd = rd;
        this.rDist = rd.getTotalDist();
        this.nrPhotos = ph.getNrPhotos();
        this.routes = rd.getLatestRoutes();
        this.photos = ph.getAllPhotos();
        updateObservers(this);
    }

/**
* Method for checking for and updating achievements.
*/
private void updateAchievements(){
//INSERT

}
}
