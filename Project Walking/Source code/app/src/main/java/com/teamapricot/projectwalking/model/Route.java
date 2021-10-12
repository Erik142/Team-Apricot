package com.teamapricot.projectwalking.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "Routes", primaryKeys = {"routeId"},
        indices = {@Index(value = { "routeId" }, unique = true)})

public class Route {
    @NonNull
    @ColumnInfo(name = "routeId")
    private String id;
    //@ColumnInfo(name = "time")
    @Ignore
    private Calendar time;
    @ColumnInfo(name = "startX")
    private double startX;
    @ColumnInfo(name = "startY")
    private double startY;
    @ColumnInfo(name = "endX")
    private double endX;
    @ColumnInfo(name = "endY")
    private double endY;

    public Route(String id, double startX, double startY, double endX, double endY){
        this.id = id;
        this.time = Calendar.getInstance();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

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
}
