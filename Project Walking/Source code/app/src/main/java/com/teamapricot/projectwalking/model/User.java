package com.teamapricot.projectwalking.model;

import java.util.ArrayList;

/**
 * @author Joakim Tubring
 * @version 2021-09-21
 *
 * A class for representing a user.
 */

public class User {

    private String userId;
    private ArrayList<Route> routes;

/**
* Constructor.
* Sets an id number and fetches the current time.
* @param userId The user-id.
* @param routes The users routes.
*/

    public User(String userId, ArrayList<Route> routes){
        this.userId = userId;
        this.routes = routes;
    }

    public String getUserId(){
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

    public void addRoute(){
        routes.add(new Route(generateRouteId()));
    }

/**
* Generates a route-id based on the user-id.
*/

    private String generateRouteId(){
        return this.userId + (routes.size() + 1);
    }

}