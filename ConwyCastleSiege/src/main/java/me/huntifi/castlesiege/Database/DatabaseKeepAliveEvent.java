package me.huntifi.castlesiege.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.huntifi.castlesiege.Main;

public class DatabaseKeepAliveEvent implements Runnable {

	@Override
	public void run() {
		try {
			PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT SCORE FROM castlesiegestats LIMIT 1;");
			ps.execute();
			SQLStats.ClosePreparedStatement(ps);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Did a preparedstatement to the database to keep it connected.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
