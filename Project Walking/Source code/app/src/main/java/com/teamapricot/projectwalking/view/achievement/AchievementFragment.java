package com.teamapricot.projectwalking.view.achievement;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.AchievementRecyclerViewAdapter;
import com.teamapricot.projectwalking.model.database.Achievement;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class AchievementFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Achievement> achievements;
    private RecyclerView.Adapter recyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AchievementFragment() {
        achievements = new ArrayList<>();
        achievements.add(new Achievement("1", "Achievement 1", "Description for achievement 1",false, 1, 1, 100, 1));
        achievements.add(new Achievement("2", "Achievement 2", "Description for achievement 2",false, 1, 1, 100, 2));
        achievements.add(new Achievement("3", "Achievement 3", "Description for achievement 3",false, 1, 1, 100, 3));
        achievements.add(new Achievement("4", "Achievement 4", "Description for achievement 4",false, 1, 1, 100, 4));
        achievements.add(new Achievement("5", "Achievement 5", "Description for achievement 5",false, 1, 1, 100, 5));
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AchievementFragment newInstance(int columnCount) {
        AchievementFragment fragment = new AchievementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.achievement_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new AchievementRecyclerViewAdapter(achievements));
            this.recyclerViewAdapter = recyclerView.getAdapter();

            // TODO: Register observer
        }
        return view;
    }
}