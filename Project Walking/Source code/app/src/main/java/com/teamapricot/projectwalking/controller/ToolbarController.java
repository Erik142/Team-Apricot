package com.teamapricot.projectwalking.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.view.achievement.AchievementsActivity;

public class ToolbarController {
    public boolean onToolbarMenuItemClick(MenuItem item, Activity parent) {
        switch (item.getItemId()) {
            case R.id.action_achievements:
                return openAchievements(parent);
            default:
                return parent.onOptionsItemSelected(item);
        }
    }

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
