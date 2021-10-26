package com.teamapricot.projectwalking.view.achievement;

import android.widget.Toolbar;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.AchievementViewPagerAdapter;

/**
 * @author Erik Wahlberger
 * @version 2021-10-26
 *
 * An activity for showing achievements that have been unlocked for the user
 */
public class AchievementsActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        initToolbar();
        ViewPager2 viewPager = findViewById(R.id.achievements_viewpager);
        TabLayout tabLayout = findViewById(R.id.achievements_tab_layout);
        AchievementViewPagerAdapter achievementViewPagerAdapter = new AchievementViewPagerAdapter(this);
        viewPager.setAdapter(achievementViewPagerAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(achievementViewPagerAdapter.getTitle(position)));
        tabLayoutMediator.attach();
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.achievements_toolbar);
        setActionBar(myToolbar);
        getActionBar().setTitle("Achievements and statistics");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
}