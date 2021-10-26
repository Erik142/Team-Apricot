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

/**
 * @author Erik Wahlberger
 * @version 2021-10-26
 *
 * This is the FragmentStateAdapter used to enable swiping functionality in the AchievementActivity class
 */
public class AchievementViewPagerAdapter extends FragmentStateAdapter {

    private final int NUM_FRAGMENTS = 2;

    private final List<String> fragmentTitles;

    /**
     * Creates a new instance of the AchievementViewPagerAdapter for the specified {@link FragmentActivity}
     * @param fragmentActivity The FragmentActivity that this FragmentStateAdapter will be used in.
     */
    public AchievementViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("Achievements");
        fragmentTitles.add("Statistics");
    }

    /**
     * Returns the {@link Fragment} at the specified position. Position 0 is an {@link AchievementFragment}, position 1 is a {@link StatisticsFragment}
     * @param position The zero-indexed position
     * @return The {@link Fragment} for the specified position, or null if the position is invalid
     */
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
