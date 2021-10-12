package com.teamapricot.projectwalking.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joakim Tubring, Erik Wahlberger
 * @version 2021-10-06
 *
 * A class for representing a user.
 */

public class User {

    private final String userId;
    private List<Route> routes;
    public User(String userId) {
        this.userId = userId;
    }

/**
* Constructor.
* Sets an id number and fetches the current time.
* @param userId The user-id.
* @param routes The users routes.
*/

    public User(String userId, List<Route> routes){
        this.userId = userId;
        this.routes = routes;
    }

    public String getUserId(){
        return this.userId;
    }

    public List getRoutes(){
        return this.routes;
    }

/**
* Gets the last route from the list.
*/

    public Route getLastRoute(){
        return routes.get(routes.size() - 1);
    }

   // public void addRoute(GeoPoint start, GeoPoint end){
    //    routes.add(new Route("" + generateRouteId(), start.getLongitude(), start.getLatitude(), end.getLongitude(), end.getLatitude()));
    //}

/**
* Generates a route-id based on the user-id.
*/

    private String generateRouteId(){
        return this.userId + (routes.size() + 1);
    }
}