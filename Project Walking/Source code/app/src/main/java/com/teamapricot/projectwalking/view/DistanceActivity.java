package com.teamapricot.projectwalking.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.R;
import com.teamapricot.projectwalking.controller.DistanceController;
import com.teamapricot.projectwalking.model.database.Database;
import com.teamapricot.projectwalking.model.database.dao.RouteDao;

import java.util.concurrent.ExecutionException;

public class DistanceActivity extends AppCompatActivity {
    TextView txt;
    private DistanceController distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        try {
            Database database = Database.getDatabase(this).get();
            RouteDao routeDao = database.routeDao();
            distance = new DistanceController(routeDao);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return;
        }


        distance.DistanceTravelled();
        double Dtn = distance.getTotalDistance();
        txt = findViewById(R.id.Number);
        txt.setText(Double.toString(Dtn));

    }
}