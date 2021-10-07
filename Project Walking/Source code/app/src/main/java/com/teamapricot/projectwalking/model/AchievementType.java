package com.teamapricot.projectwalking.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AchievementType {
    @NonNull
    @PrimaryKey
    private String typeId;
    private int points;
    private String headline;
    private String description;

    public AchievementType(String typeId, int points, String headline, String description){
        this.typeId = typeId;
        this.points = points;
        this.headline = headline;
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
    public String getHeadline() { return this.headline; }
    public String getTypeId(){
        return this.typeId;
    }
    public int getPoints(){
        return this.points;
    }
}
