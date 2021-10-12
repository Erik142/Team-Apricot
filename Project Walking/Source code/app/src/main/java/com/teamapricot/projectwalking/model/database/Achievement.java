package com.teamapricot.projectwalking.model.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;


/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for representing an achievement.
 */
@Entity
public class Achievement {
    @NonNull
    @PrimaryKey
    private final String achievementId;
    private String title;
    private String description;
    private boolean status;
    private int type;
    private int value;
    private int points;
    private int nr;


/**
* @Constructor
 * @param achievementId Unique identifier for achievements.
 * @param status status true false to set if achievement have been achieved.
*/
    public Achievement(String achievementId, String title, String description,
                       boolean status, int type, int value, int points, int nr){
        this.achievementId = achievementId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.value = value;
        this.points = points;
        this.nr = nr;

    }

    public String getAchievementId(){
        return this.achievementId;
    }

    public String getDescription() { return this.description; }

    public boolean getStatus(){
        return this.status;
    }

    public String getTitle() { return this.title; }

    public int getValue(){
        return this.value;
    }

    public int getPoints() { return this.points; }

    public int getNr() { return this.nr; }

    public int getType() { return this.type; }

    public void changeStatus(String achivementId){
        this.status = true;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
