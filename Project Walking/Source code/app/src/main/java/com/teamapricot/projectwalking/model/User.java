package com.teamapricot.projectwalking.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Relation;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * @author Joakim Tubring, Erik Wahlberger
 * @version 2021-10-06
 *
 * A class for representing a user.
 */

@Entity
public class User {
    @ColumnInfo(name = "userId")
    private final int userId;
    private ArrayList<Route> routes;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    private ArrayList<Achievement> achievements;

/**
* Constructor.
* Sets an id number and fetches the current time.
* @param userId The user-id.
* @param routes The users routes.
*/

    public User(int userId, ArrayList<Route> routes){
        this.userId = userId;
        this.routes = routes;
    }

    public int getUserId(){
        return this.userId;
    }

    public ArrayList getRoutes(){
        return this.routes;
    }

/**
* Gets the last route from the list.
*/

    public Route getLastRoute(){
        return routes.get(routes.size() - 1);
    }

/**
* Adds a route (placeholder).
* Will be adding to this later.
*/

    public void addRoute(GeoPoint start, GeoPoint end){
        routes.add(new Route("" + generateRouteId(), start.getLongitude(), start.getLatitude(), end.getLongitude(), end.getLatitude()));
    }

/**
* Generates a route-id based on the user-id.
*/

    private int generateRouteId(){
        return this.userId + (routes.size() + 1);
    }

}