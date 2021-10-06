package com.teamapricot.projectwalking.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity
public class Achievement {
    @PrimaryKey
    private final int achievementId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "status")
    private boolean status;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    private User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "achivementTypeId"
    )
    private AchievementType type;

    public Achievement(int achievementId, boolean status, AchievementType type){
        this.achievementId = achievementId;
        this.status = status;
        this.type = type;
    }

    public int getAchievementId(){
        return this.achievementId;
    }

    public boolean getstatus(){
        return this.status;
    }

    public AchievementType getType(){
        return this.type;
    }

    public void changeStatus(String achivementId){
        this.status = true;
    }
}
