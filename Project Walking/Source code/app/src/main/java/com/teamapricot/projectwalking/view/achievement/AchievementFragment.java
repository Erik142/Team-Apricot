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
import com.teamapricot.projectwalking.model.Board;
import com.teamapricot.projectwalking.model.database.Achievement;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.dao.AchievementDao;
import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;
import com.teamapricot.projectwalking.observe.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Erik Wahlberger
 * @version 2021-10-13
 * A fragment representing a list of Items.
 */
public class AchievementFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<Achievement> achievements;
    private RecyclerView.Adapter recyclerViewAdapter;
    private Board board;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AchievementFragment() {
        achievements = new ArrayList<>();
    }

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

            Database.getDatabase(getActivity().getApplicationContext()).thenAccept(database -> {
                PhotoDao photoDao = database.photoDao();
                RouteDao routeDao = database.routeDao();
                AchievementDao achievementDao = database.achievementDao();
                board = new Board(photoDao, routeDao, achievementDao);
                board.addObserver(createBoardObserver());
                board.init();
            });
        }
        return view;
    }

    private Observer<Board> createBoardObserver() {
        return model -> {
            getActivity().runOnUiThread(() -> {
                achievements.clear();
                achievements.addAll(model.getAchievements());
                this.recyclerViewAdapter.notifyDataSetChanged();
            });
        };
    }
}