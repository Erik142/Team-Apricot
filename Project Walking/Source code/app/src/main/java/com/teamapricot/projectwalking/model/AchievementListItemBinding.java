package com.teamapricot.projectwalking.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamapricot.projectwalking.R;

public class AchievementListItemBinding {
    public TextView content;
    public TextView points;
    private View parent;

    public AchievementListItemBinding(TextView contentView, TextView pointsView, View parent) {
        this.content = contentView;
        this.points = pointsView;
        this.parent = parent;
    }

    public View getRoot() {
        return this.parent;
    }

    public static AchievementListItemBinding inflate(LayoutInflater from, ViewGroup parent, boolean attachToRoot) {
        View listItem = from.inflate(R.layout.achievement_list_item, parent, attachToRoot);

        TextView contentView = listItem.findViewById(R.id.achievement_title);
        TextView pointsView = listItem.findViewById(R.id.achievement_points);

        return new AchievementListItemBinding(contentView, pointsView, listItem);
    }
}
