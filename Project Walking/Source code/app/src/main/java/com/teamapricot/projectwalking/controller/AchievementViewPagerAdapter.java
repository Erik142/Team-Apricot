package com.teamapricot.projectwalking.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.teamapricot.projectwalking.view.achievement.AchievementFragment;
import com.teamapricot.projectwalking.view.achievement.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class AchievementViewPagerAdapter extends FragmentStateAdapter {

    private final int NUM_FRAGMENTS = 2;

    private final List<String> fragmentTitles;

    public AchievementViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("Achievements");
        fragmentTitles.add("Statistics");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AchievementFragment();
            case 1:
                // TODO: Change to StatisticsFragment
                return new StatisticsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_FRAGMENTS;
    }

    public String getTitle(int position) {
        if (position >= fragmentTitles.size()) {
            return "";
        }

        return fragmentTitles.get(position);
    }
}
