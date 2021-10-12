package com.teamapricot.projectwalking.model;

import com.teamapricot.projectwalking.model.dao.PhotoDao;
import com.teamapricot.projectwalking.model.dao.RouteDao;

import java.util.List;

public class dbUpdate {

    private RouteDao rd;
    private PhotoDao pd;
    private final double rDist;
    private final int nrPhotos;
    private final List<Route> routes;
    private final List<Photo> photos;

    public dbUpdate(){
        this.rDist = rd.getTotalDist();
        this.nrPhotos = pd.getNrPhotos();
        this.routes = rd.getLatestRoutes();
        this.photos = pd.getAllPhotos();
    }

    public double getRdist(){
        return this.rDist;
    }

    public int getNrsPhotos() {
        return this.nrPhotos;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }


}
