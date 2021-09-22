package com.teamapricot.projectwalking.Navigation;

package com.teamapricot.projectwalking.Navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.teamapricot.projectwalking.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

public class MapController extends AppCompatActivity {

    //declarations
    private Context context;
    MapView map = null;


    //Constructor
    public MapController(Context context) {

        this.context = context;
    }



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
    }

    public void onResume() {

        super.onResume();
        map.onResume();
    }

    public void onPause() {

        super.onPause();
        map.onPause();

    }

}