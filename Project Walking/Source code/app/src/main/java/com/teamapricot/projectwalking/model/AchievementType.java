package com.teamapricot.projectwalking.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class AchievementType {
    @ColumnInfo(name = "typeId")
    private int typeId;
    @ColumnInfo(name = "points")
    private int points;
    @ColumnInfo(name = "headline")
    private String headline;
    @ColumnInfo(name = "description")
    private String description;

    public AchievementType(int typeNr, int points, String headline, String description){
        this.typeId = typeNr;
        this.points = points;
        this.headline = headline;
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
    public String getHeadline() { return this.headline; }
    public int getId(){
        return this.typeId;
    }
    public int getPoints(){
        return this.points;
    }
}
