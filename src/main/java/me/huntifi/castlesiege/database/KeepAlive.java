package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Cheap solution to keep the database connected
 */
public class KeepAlive implements Runnable {

    /**
     * Get 0 rows from the player_stats table
     */
    @Override
    public void run() {
        try {
            PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                    "SELECT * FROM player_stats LIMIT 0");
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
