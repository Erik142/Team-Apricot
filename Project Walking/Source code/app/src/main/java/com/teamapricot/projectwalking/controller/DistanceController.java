package com.teamapricot.projectwalking.controller;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.model.database.dao.RouteDao;

public class DistanceController extends AppCompatActivity {
    private RouteDao routeDao;
    double TotalDistance = 0;



    public DistanceController(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    public void DistanceTravelled(){
        TotalDistance = routeDao.getTotalDist();
    }

    public double getTotalDistance(){
        return TotalDistance;
    }
    /**
     * button to get the statistics and new screen.

    public void registerOnClickListener(View button, AppCompatActivity activity) {
        if (!button.hasOnClickListeners()) {
            button.setOnClickListener(view -> {
                if (view.getId() == R.id.statistics) {
                    Intent intent = new Intent (activity, DistanceActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
     */
}


