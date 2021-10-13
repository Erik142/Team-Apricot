package com.teamapricot.projectwalking.view.achievement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.ToolbarController;

/**
 * @author Erik Wahlberger
 * @version 2021-10-12
 *
 * An activity for showing achievements that have been unlocked for the user
 */
public class AchievementsActivity extends AppCompatActivity {
    private ToolbarController toolbarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        initToolbar();
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.achievements_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbarController = new ToolbarController();
    }
}