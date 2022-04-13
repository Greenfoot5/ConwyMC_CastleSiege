package me.huntifi.castlesiege.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.huntifi.castlesiege.Main;

public class DatabaseKeepAliveEvent implements Runnable {

	@Override
	public void run() {
		try {
			PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT SCORE FROM player_stats LIMIT 1;");
			ps.execute();
			SQLStats.ClosePreparedStatement(ps);
			Main.instance.getLogger().fine("Did a prepared statement to the database to keep it connected.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
