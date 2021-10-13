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
    private String achievementTypeId;


/**
* @Constructor
 * @param achievementId Unique identifier for achievements.
 * @param achievementTypeId type of achievement.
 * @param status status true false to set if achievement have been achieved.
*/
    public Achievement(String achievementId, boolean status, String achievementTypeId){
        this.achievementId = achievementId;
        this.status = status;
        this.achievementTypeId = achievementTypeId;
    }

    public String getAchievementId(){
        return this.achievementId;
    }

    public String getDescription() { return this.description; }

    public boolean getStatus(){
        return this.status;
    }

    public String getTitle() { return this.title; }

    public String getAchievementTypeId(){
        return this.achievementTypeId;
    }

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
