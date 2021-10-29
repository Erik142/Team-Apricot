package com.teamapricot.projectwalking.model.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * class for representing a route.
 */
@Entity(tableName = "Routes")
public class Route {
    @PrimaryKey(autoGenerate = true)
    private long routeId;
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
    private double distance;
    private boolean done = false;

    /**
     * @Constructor
     * @param startX The startpoint X-value.
     * @param startY The startpoint Y-value.
     * @param endX The endpoint X-value.
     * @param endY The endpoint Y-value
     * @param distance The distance of the route.
     */
    public Route(double startX, double startY, double endX, double endY, double distance) {
        this.time = Calendar.getInstance();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.distance = distance;
    }

    public long getRouteId() {
        return this.routeId;
    }

    public Calendar getTime() {
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

    public double getDistance() {
        return this.distance;
    }

    public boolean isDone() { return this.done; }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDone(boolean done) { this.done = done; }
}
