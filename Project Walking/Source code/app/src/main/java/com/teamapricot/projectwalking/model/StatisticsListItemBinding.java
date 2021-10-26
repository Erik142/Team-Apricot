package com.teamapricot.projectwalking.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamapricot.projectwalking.R;

/**
 * @author Erik Wahlberger
 * @version 2021-10-26
 *
 * Helper class used to bind all {@link View}'s from the Statistics list item layout to {@link View} objects
 */
public class StatisticsListItemBinding {
    public TextView title;
    public TextView value;
    private View parent;

    /**
     * Creates a new StatisticsListItemBinding object and binds the two {@link TextView}s and the parent {@link View} to the corresponding properties in this class
     * @param titleView The {@link TextView} containing the Statistics title
     * @param valueView The {@link TextView} containing the Statistics value
     * @param parent The parent {@link View}
     */
    public StatisticsListItemBinding(TextView titleView, TextView valueView, View parent) {
        this.title = titleView;
        this.value = valueView;
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
     * Static method to inflate an StatisticsListItemBinding object from an existing layout file
     * @param from The {@link LayoutInflater} to use to inflate the view
     * @param parent The parent {@link ViewGroup} for the view
     * @param attachToRoot Attach the inflated view to the root view?
     * @return An AchievementListItemBinding object for the inflated view
     */
    public static StatisticsListItemBinding inflate(LayoutInflater from, ViewGroup parent, boolean attachToRoot) {
        View listItem = from.inflate(R.layout.achievement_list_item, parent, attachToRoot);

        TextView titleView = listItem.findViewById(R.id.achievement_title);
        TextView valueView = listItem.findViewById(R.id.achievement_points);

        return new StatisticsListItemBinding(titleView, valueView, listItem);
    }
}
