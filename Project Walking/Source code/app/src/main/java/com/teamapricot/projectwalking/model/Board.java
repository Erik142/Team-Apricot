package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.model.dao.PhotoDao;
import com.teamapricot.projectwalking.model.dao.RouteDao;
import com.teamapricot.projectwalking.observe.ObservableBase;

import java.util.List;

public class Board extends ObservableBase<Board> {

    private PhotoDao ph;
    private RouteDao rd;
    private double rDist;
    private int nrPhotos;
    private List<Route> routes;
    private List<Photo> photos;

    public Board(PhotoDao ph, RouteDao rd){
        this.ph = ph;
        this.rd = rd;
        this.rDist = rd.getTotalDist();
        this.nrPhotos = ph.getNrPhotos();
        this.routes = rd.getLatestRoutes();
        this.photos = ph.getAllPhotos();
        updateObservers(this);
    }

private void updateAchievements(){

}
}
