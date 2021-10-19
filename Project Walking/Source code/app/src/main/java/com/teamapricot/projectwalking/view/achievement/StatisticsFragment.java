package com.teamapricot.projectwalking.view.achievement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.AchievementRecyclerViewAdapter;
import com.teamapricot.projectwalking.controller.DistanceController;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.dao.PhotoDao;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class StatisticsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txt;
    private DistanceController distance;
    private int mColumnCount = 1;
    private RecyclerView.Adapter recyclerViewAdapter;
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StatisticsFragment() {
        // Required empty public constructor

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new AchievementRecyclerViewAdapter(distance));
            this.recyclerViewAdapter = recyclerView.getAdapter();

            Database.getDatabase(getActivity().getApplicationContext()).thenAccept(database -> {
                PhotoDao photoDao = database.photoDao();
                //RouteDao routeDao = database.routeDao();
                    RouteDao routeDao = database.routeDao();
                    distance = new DistanceController(routeDao);
                    distance.DistanceTravelled();
                    double Dtn = distance.getTotalDistance();

            });
        }
        return view;
    }
}
