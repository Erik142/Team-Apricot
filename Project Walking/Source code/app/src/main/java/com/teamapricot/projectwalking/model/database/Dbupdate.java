package com.teamapricot.projectwalking.model.database;

import com.teamapricot.projectwalking.model.database.dao.AchievementDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;


/**
 * Class for handling updates in databasetables, add methods if you need more.
 */
public class Dbupdate {

/**
* Method to readfile-input to update achievementboard
*/
    private void insertAchievementData(AchievementDao ad) throws IOException {
        URL url = getClass().getResource("AchievementUpdate.txt");
        assert url != null;
        BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
        String line;

        while ((line = br.readLine()) != null) {
            String[] tmp = line.split(",");
            Achievement aTmp = new Achievement(tmp[0], tmp[1], tmp[2], false,
                    Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]),
                    Integer.parseInt(tmp[5]), Integer.parseInt(tmp[6]));
            ad.insertAchievements(aTmp);
        }
    }
}
