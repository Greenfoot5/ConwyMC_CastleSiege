package me.huntifi.castlesiege.stats.MVP;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MVPstats implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static HashMap<UUID, Double> Score = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> Deaths = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> Kills = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> Captures = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> Heals = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> Supports = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> Assists = new HashMap<UUID, Double>();
	public static HashMap<UUID, Integer> Killstreak = new HashMap<UUID, Integer>();

	//SCORE

	public static void addScore(UUID uuid, Double value) {
		
		final double save = Score.get(uuid);

		if (Score.containsKey(uuid)) {

			Score.remove(uuid);

		}

		Score.put(uuid, save + value);

	}

	public static void removeScore(UUID uuid, Double value) {
		
		final double save = Score.get(uuid);

		if (Score.containsKey(uuid)) {

			Score.remove(uuid);

		}

		Score.put(uuid, save - value);

	}

	public static void setScore(UUID uuid, Double value) {

		if (Score.containsKey(uuid)) {

			Score.remove(uuid);

		}

		Score.put(uuid, value);

	}

	public static Double getScore(UUID uuid) {

		if (Score.containsKey(uuid)) {

			return Score.get(uuid);

		}

		return null;

	}

	//KILLS


	public static void addKills(final UUID uuid, final Double value) {
		
		final double save = Kills.get(uuid);

		if (Kills.containsKey(uuid)) {

			Kills.remove(uuid);

		}

		Kills.put(uuid, save + value);
	}

	public static void removeKills(final UUID uuid, final Double value) {
		
		final double save = Kills.get(uuid);

		if (Kills.containsKey(uuid)) {

			Kills.remove(uuid);

		}

		Kills.put(uuid, save - value);
	}

	public static void setKills(final UUID uuid, final Double value) {

		if (Kills.containsKey(uuid)) {

			Kills.remove(uuid);

		}

		Kills.put(uuid, value);

	}

	public static Double getKills(UUID uuid) {

		if (Kills.containsKey(uuid)) {

			return Kills.get(uuid);

		}

		return 0.0;

	}

	//DEATHS

	public static void addDeaths(UUID uuid, double value) {
		
		final double save = Deaths.get(uuid);

		if (Deaths.containsKey(uuid)) {

			
			Deaths.remove(uuid);
			
		}

		Deaths.put(uuid, save + value);
	}

	public static void removeDeaths(UUID uuid, double value) {

		final double save = Deaths.get(uuid);

		if (Deaths.containsKey(uuid)) {

			Deaths.remove(uuid);

		}

		Deaths.put(uuid, save - value);

	}

	public static void setDeaths(UUID uuid, Double value) {

		if (Deaths.containsKey(uuid)) {

			Deaths.remove(uuid);

		}

		Deaths.put(uuid, value);

	}

	public static Double getDeaths(UUID uuid) {

		if (Deaths.containsKey(uuid)) {

			return Deaths.get(uuid);

		}

		return 0.0;

	}

	//CAPTURES


	public static void addCaptures(UUID uuid, Double value) {
		
		final double save = Captures.get(uuid);

		if (Captures.containsKey(uuid)) {

			Captures.remove(uuid);

		}

		Captures.put(uuid, save + value);
	}

	public static void removeCaptures(UUID uuid, Double value) {
		
		final double save = Captures.get(uuid);

		if (Captures.containsKey(uuid)) {

			Captures.remove(uuid);

		}

		Captures.put(uuid, save - value);

	}

	public static void setCaptures(UUID uuid, Double value) {

		if (Captures.containsKey(uuid)) {

			Captures.remove(uuid);

		}

		Captures.put(uuid, value);

	}

	public static Double getCaptures(UUID uuid) {

		if (Captures.containsKey(uuid)) {

			return Captures.get(uuid);

		}

		return 0.0;

	}


	//ASSISTS


	public static void addAssists(UUID uuid, Double value) {
		
		final double save = Assists.get(uuid);

		if (Assists.containsKey(uuid)) {

			Assists.remove(uuid);

		}

		Assists.put(uuid, save + value);
	}

	public static void removeAssists(UUID uuid, Double value) {
		
		final double save = Assists.get(uuid);

		if (Assists.containsKey(uuid)) {

			Assists.remove(uuid);

		}

		Assists.put(uuid, save - value);

	}

	public static void setAssists(UUID uuid, Double value) {

		if (Assists.containsKey(uuid)) {

			Assists.remove(uuid);

		}

		Assists.put(uuid, value);
	}

	public static Double getAssists(UUID uuid) {

		if (Assists.containsKey(uuid)) {

			return Assists.get(uuid);

		}

		return 0.0;
	}


	//HEALS


	public static void addHeals(UUID uuid, double value) {
		
		final double save = Heals.get(uuid);

		if (Heals.containsKey(uuid)) {

			Heals.remove(uuid);

		}

		Heals.put(uuid, save + value);
	}

	public static void removeHeals(UUID uuid, double value) {
		
		final double save = Heals.get(uuid);

		if (Heals.containsKey(uuid)) {

			Heals.remove(uuid);

		}

		Heals.put(uuid, save - value);

	}

	public static void setHeals(UUID uuid, double value) {

		if (Heals.containsKey(uuid)) {

			Heals.remove(uuid);

		}

		Heals.put(uuid, value);
	}

	public static double getHeals(UUID uuid) {

		if (Heals.containsKey(uuid)) {

			return Heals.get(uuid);

		}

		return 0.0;
	}


	//SUPPORTS


	public static void addSupports(UUID uuid, Double value) {
		
		final double save = Supports.get(uuid);

		if (Supports.containsKey(uuid)) {

			Supports.remove(uuid);

		}

		Supports.put(uuid, save + value);
	}

	public static void removeSupports(final UUID uuid, final Double value) {
		
		final double save = Supports.get(uuid);

		if (Supports.containsKey(uuid)) {

			Supports.remove(uuid);

		}

		Supports.put(uuid, save - value);

	}

	public static void setSupports(final UUID uuid, final Double value) {

		if (Supports.containsKey(uuid)) {

			Supports.remove(uuid);

		}

		Supports.put(uuid, value);
	}

	public static Double getSupports(UUID uuid) {

		if (Supports.containsKey(uuid)) {

			return Supports.get(uuid);

		}

		return 0.0;

	}


	//KILLSTREAKS


	public static void addKillstreak(UUID uuid, int value) {
		
		final int save = Killstreak.get(uuid);

		if (Killstreak.containsKey(uuid)) {

			Killstreak.remove(uuid);

		}
		
		Killstreak.put(uuid, save + value);

	}

	public static void removeKillstreak(UUID uuid, int value) {
		
		final int save = Killstreak.get(uuid);

		if (Killstreak.containsKey(uuid)) {

			Killstreak.remove(uuid);

		}

		Killstreak.put(uuid, save - value);

	}

	public static void setKillstreak(UUID uuid, int value) {

		if (Killstreak.containsKey(uuid)) {

			Killstreak.remove(uuid);

		}

		Killstreak.put(uuid, value);
	}

	public static int getKillstreak(UUID uuid) {

		if (Killstreak.containsKey(uuid)) {

			return Killstreak.get(uuid);

		}

		return 0;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		if (!Score.containsKey(p.getUniqueId())) {

			Score.put(p.getUniqueId(), getScore(p.getUniqueId()));
		} else {

			Score.remove(p.getUniqueId(), getScore(p.getUniqueId()));
			Score.put(p.getUniqueId(), getKills(p.getUniqueId()));
		}

		if (!Kills.containsKey(p.getUniqueId())) {

			Kills.put(p.getUniqueId(), getKills(p.getUniqueId()));
		} else {

			Kills.remove(p.getUniqueId(), getKills(p.getUniqueId()));
			Kills.put(p.getUniqueId(), getKills(p.getUniqueId()));
		}

		if (!Deaths.containsKey(p.getUniqueId())) {

			Deaths.put(p.getUniqueId(), getDeaths(p.getUniqueId()));
		} else {

			Deaths.remove(p.getUniqueId(), getDeaths(p.getUniqueId()));
			Deaths.put(p.getUniqueId(), getDeaths(p.getUniqueId()));
		}


		if (!Heals.containsKey(p.getUniqueId())) {

			Heals.put(p.getUniqueId(), getHeals(p.getUniqueId()));
		} else {

			Heals.remove(p.getUniqueId(), getHeals(p.getUniqueId()));
			Heals.put(p.getUniqueId(), getHeals(p.getUniqueId()));
		}

		if (!Supports.containsKey(p.getUniqueId())) {

			Supports.put(p.getUniqueId(), getSupports(p.getUniqueId()));
		} else {

			Supports.remove(p.getUniqueId(), getSupports(p.getUniqueId()));
			Supports.put(p.getUniqueId(), getSupports(p.getUniqueId()));
		}

		if (!Assists.containsKey(p.getUniqueId())) {

			Assists.put(p.getUniqueId(), getAssists(p.getUniqueId()));

		} else {

			Assists.remove(p.getUniqueId(), getAssists(p.getUniqueId()));
			Assists.put(p.getUniqueId(), getAssists(p.getUniqueId()));
		}

		if (!Captures.containsKey(p.getUniqueId())) {

			Captures.put(p.getUniqueId(), getCaptures(p.getUniqueId()));
		} else {

			Captures.remove(p.getUniqueId(), getCaptures(p.getUniqueId()));
			Captures.put(p.getUniqueId(), getCaptures(p.getUniqueId()));
		}

		if (!Killstreak.containsKey(p.getUniqueId())) {

			Killstreak.put(p.getUniqueId(), getKillstreak(p.getUniqueId()));
		} else {

			Killstreak.remove(p.getUniqueId(), getKillstreak(p.getUniqueId()));
			Killstreak.put(p.getUniqueId(), getKillstreak(p.getUniqueId()));
		}

	}

	public static void removeMvpStats(Player p) {

		new BukkitRunnable() {

			@Override
			public void run() {

				if (Bukkit.getOnlinePlayers().contains(p)) {

					Score.remove(p.getUniqueId());
					Kills.remove(p.getUniqueId());
					Deaths.remove(p.getUniqueId());
					Captures.remove(p.getUniqueId());
					Assists.remove(p.getUniqueId());
					Heals.remove(p.getUniqueId());
					Supports.remove(p.getUniqueId());
					Killstreak.remove(p.getUniqueId());

				}
			}

		}.runTaskLaterAsynchronously(plugin, 1200);

	}
	
	public static double returnMainMvpScore (Player p) {
		
		UUID uuid = p.getUniqueId();
		
		double kills = MVPstats.getKills(uuid);
		double heals = MVPstats.getHeals(uuid);
		double deaths = MVPstats.getDeaths(uuid);
		double assists = MVPstats.getAssists(uuid);
		double supports = MVPstats.getSupports(uuid);
		double captures = MVPstats.getCaptures(uuid);
		
		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);
		
		return totalScore;
		
		
	}

}
