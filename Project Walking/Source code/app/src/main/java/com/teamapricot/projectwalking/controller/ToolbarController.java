package com.teamapricot.projectwalking.controller;

import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.model.NavigationModel;
import com.teamapricot.projectwalking.view.achievement.AchievementsActivity;

/**
 * @author Erik Wahlberger
 * @version 2021-10-14
 *
 * A Controller class used to decide what happens when buttons are clicked on the {@link Toolbar}
 */
public class ToolbarController {
    private NavigationModel navigationModel;

    public ToolbarController(NavigationModel navigationModel) {
        this.navigationModel = navigationModel;
    }

    /**
     * Decides what to do when a menu item has been clicked in the Toolbar
     * @param item The {@link MenuItem} that has been clicked
     * @param parent The parent {@link Activity} on which the Toolbar resides
     * @return {@code true} if operation succeeded, {@code false} otherwise
     */
    public boolean onToolbarMenuItemClick(MenuItem item, Activity parent) {
        switch (item.getItemId()) {
            case R.id.action_achievements:
                return openAchievements(parent);
            case R.id.action_follow_location:
                navigationModel.setFollowLocation(!item.isChecked());
                return true;
            default:
                return false;
        }
    }

    /**
     * Opens a new {@link AchievementsActivity} from the specified {@link Activity}
     * @param activity The {@link Activity} to open the new {@link AchievementsActivity} from
     * @return {@code true} if the operation was successful, {@code false} otherwise
     */
    private boolean openAchievements(Activity activity) {
        try {
            Intent intent = new Intent(activity, AchievementsActivity.class);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
