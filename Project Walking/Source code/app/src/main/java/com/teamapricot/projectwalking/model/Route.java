package com.teamapricot.projectwalking.model;

import java.util.Calendar;

/**
 * @author Joakim Tubring
 * @version 2021-09-21
 *
 * A class for representing a route.
 */

public class Route {
    private String id;
    private Calendar time;

/**
* Constructor.
* Sets an id number and fetches the current time.
* @param id The route id.
*/

    public Route(String id){
        this.id = id;
        this.time = Calendar.getInstance();
    }

    public String getId(){
        return this.id;
    }

    public Calendar getTime(){
        return this.time;
    }

}