package com.teamapricot.projectwalking.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Calendar;

@Entity(tableName = "Routes", primaryKeys = {"userId", "id"}, foreignKeys =
        {@ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE)})

public class Route {
    @ColumnInfo(name = "routeId")
    private String id;
    @ColumnInfo(name = "time")
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

}
