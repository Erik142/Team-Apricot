package com.teamapricot.projectwalking.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * @author Erik Wahlberger, Joakim Tubring
 * @version 2021-10-12
 */

/**
 * Class for representing an achievement-type.
 */
@Entity
public class AchievementType {
    @NonNull
    @PrimaryKey
    private String typeId;
    private int points;
    private String headline;
    private String description;

/**
* @Constructor
 * @param description Description of current achievement.
 * @param headline Headline of current achievement.
 * @param points number of points for achievement.
 * @param typeId type of achievement 1 = total distance routes : 2 = number of photos.
*/
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
