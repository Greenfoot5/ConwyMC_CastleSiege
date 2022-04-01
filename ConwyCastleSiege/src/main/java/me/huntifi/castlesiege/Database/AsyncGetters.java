package me.huntifi.castlesiege.Database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncGetters {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static interface StringCallback{
		public void gotString(String string);
	}


	public static void performLookupKit(final StringCallback callback, final Player player, final String stat, final boolean syncCallback){
		new BukkitRunnable() {
			@Override
			public void run() {
				String line = SQLStats.getKit(player.getUniqueId());

				if(!syncCallback) {
					callback.gotString(line);
				}


				if(syncCallback) {
					new BukkitRunnable() {
						@Override
						public void run() { callback.gotString(stat); }

					}.runTask(plugin);
				}
			}
		}.runTaskAsynchronously(plugin);
	}



	public static interface BooleanCallback{
		public void gotBoolean(Boolean string);
	}



	public static void performLookupRank(final BooleanCallback callback, final Boolean rank, final Player player, final boolean syncCallback){
		new BukkitRunnable() {
			@Override
			public void run() {
				Boolean line = true;

				if(!syncCallback) {callback.gotBoolean(line); }



				if(syncCallback) {
					new BukkitRunnable() {
						@Override
						public void run() { callback.gotBoolean(rank); }

					}.runTask(plugin);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
}


