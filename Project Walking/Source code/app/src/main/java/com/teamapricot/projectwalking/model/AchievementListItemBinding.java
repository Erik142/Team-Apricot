package com.teamapricot.projectwalking.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamapricot.projectwalking.R;

/**
 * @author Erik Wahlberger
 * @version 2021-10-12
 *
 * Helper class used to bind all {@link View}'s from the Achievement list item layout to {@link View} objects
 */
public class AchievementListItemBinding {
    public TextView content;
    public TextView points;
    private View parent;

    /**
     * Creates a new AchievementListItemBinding object and binds the two {@link TextView}s and the parent {@link View} to the corresponding properties in this class
     * @param contentView The {@link TextView} containing the Achievement title
     * @param pointsView The {@link TextView} containing the Achievement points
     * @param parent The parent {@link View}
     */
    public AchievementListItemBinding(TextView contentView, TextView pointsView, View parent) {
        this.content = contentView;
        this.points = pointsView;
        this.parent = parent;
    }

    /**
     * Retrieves the parent {@link View}
     * @return The parent {@link View}
     */
    public View getRoot() {
        return this.parent;
    }

    /**
     * Static method to inflate an AchievementListItemBinding object from an existing layout file
     * @param from The {@link LayoutInflater} to use to inflate the view
     * @param parent The parent {@link ViewGroup} for the view
     * @param attachToRoot Attach the inflated view to the root view?
     * @return An AchievementListItemBinding object for the inflated view
     */
    public static AchievementListItemBinding inflate(LayoutInflater from, ViewGroup parent, boolean attachToRoot) {
        View listItem = from.inflate(R.layout.achievement_list_item, parent, attachToRoot);

        TextView contentView = listItem.findViewById(R.id.achievement_title);
        TextView pointsView = listItem.findViewById(R.id.achievement_points);

        return new AchievementListItemBinding(contentView, pointsView, listItem);
    }
}
