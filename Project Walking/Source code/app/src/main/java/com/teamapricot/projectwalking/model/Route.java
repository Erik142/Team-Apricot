package com.teamapricot.projectwalking.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import java.util.Calendar;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * class for representing a route.
 */
@Entity(tableName = "Routes", primaryKeys = {"routeId"},
        indices = {@Index(value = { "routeId" }, unique = true)})

public class Route {
    @NonNull
    @ColumnInfo(name = "routeId")
    private String id;
    //@ColumnInfo(name = "time")
    @Ignore
    private Calendar time;
    @ColumnInfo(name = "startLatitude")
    private double startX;
    @ColumnInfo(name = "startLongitude")
    private double startY;
    @ColumnInfo(name = "endLatitude")
    private double endX;
    @ColumnInfo(name = "endLongitude")
    private double endY;
    private double  distance;

/**
* @Constructor
 * @param id The id for current route.
 * @param startX The startpoint X-value.
 * @param startY The startpoint Y-value.
 * @param endX The endpoint X-value.
 * @param endY The endpoint Y-value
 * @param distance The distance of the route.
*/
    public Route(String id, double startX, double startY, double endX, double endY, double distance){
        this.id = id;
        this.time = Calendar.getInstance();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.distance = distance;

    }

    public String getId(){
        return this.id;
    }

    public Calendar getTime(){
        return this.time;
    }

    public double getEndX() {
        return this.endX;
    }

    public double getEndY() {
        return this.endY;
    }

    public double getStartX() {
        return this.startX;
    }

    public double getStartY() {
        return this.startY;
    }
    public double getDistance(){
        return this.distance;
    }
}
