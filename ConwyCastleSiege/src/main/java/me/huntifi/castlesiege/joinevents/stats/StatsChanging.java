package me.huntifi.castlesiege.joinevents.stats;

import java.util.UUID;

public class StatsChanging {

	//SCORE

	public static void addScore(UUID uuid, double value) {

		double save = StatsLoading.PlayerScore.get(uuid);

		if (StatsLoading.PlayerScore.containsKey(uuid)) {

			StatsLoading.PlayerScore.remove(uuid);

		}

		StatsLoading.PlayerScore.put(uuid, save + value);

	}

	public static void removeScore(UUID uuid, double value) {

		double save = StatsLoading.PlayerScore.get(uuid);

		if (StatsLoading.PlayerScore.containsKey(uuid)) {

			StatsLoading.PlayerScore.remove(uuid);

		}

		StatsLoading.PlayerScore.put(uuid, save - value);

	}

	public static void setScore(UUID uuid, double value) {

		if (StatsLoading.PlayerScore.containsKey(uuid)) {

			StatsLoading.PlayerScore.remove(uuid);

		}

		StatsLoading.PlayerScore.put(uuid, value);

	}

	public static double getScore(UUID uuid) {

		if (StatsLoading.PlayerScore.containsKey(uuid)) {

			return StatsLoading.PlayerScore.get(uuid);

		}

		return 0.0;

	}

	//KILLS


	public static void addKills(final UUID uuid, final double value) {

		double save = StatsLoading.PlayerKills.get(uuid);

		if (StatsLoading.PlayerKills.containsKey(uuid)) {

			StatsLoading.PlayerKills.remove(uuid);

		}

		StatsLoading.PlayerKills.put(uuid, save + value);
	}

	public static void removeKills(final UUID uuid, final double value) {

		double save = StatsLoading.PlayerKills.get(uuid);

		if (StatsLoading.PlayerKills.containsKey(uuid)) {

			StatsLoading.PlayerKills.remove(uuid);

		}

		StatsLoading.PlayerKills.put(uuid, save - value);
	}

	public static void setKills(final UUID uuid, final double value) {

		if (StatsLoading.PlayerKills.containsKey(uuid)) {

			StatsLoading.PlayerKills.remove(uuid);

		}

		StatsLoading.PlayerKills.put(uuid, value);

	}

	public static double getKills(UUID uuid) {

		if (StatsLoading.PlayerKills.containsKey(uuid)) {

			return StatsLoading.PlayerKills.get(uuid);

		}

		return 0.0;

	}

	//DEATHS

	public static void addDeaths(UUID uuid, double value) {

		double save = StatsLoading.PlayerDeaths.get(uuid);

		if (StatsLoading.PlayerDeaths.containsKey(uuid)) {

			StatsLoading.PlayerDeaths.remove(uuid);

		}

		StatsLoading.PlayerDeaths.put(uuid, save + value);
	}

	public static void removeDeaths(UUID uuid, double value) {

		final double save = StatsLoading.PlayerDeaths.get(uuid);

		if (StatsLoading.PlayerDeaths.containsKey(uuid)) {

			StatsLoading.PlayerDeaths.remove(uuid);

		}

		StatsLoading.PlayerDeaths.put(uuid, save - value);

	}

	public static void setDeaths(UUID uuid, double value) {

		if (StatsLoading.PlayerDeaths.containsKey(uuid)) {

			StatsLoading.PlayerDeaths.remove(uuid);

		}

		StatsLoading.PlayerDeaths.put(uuid, value);

	}

	public static double getDeaths(UUID uuid) {

		if (StatsLoading.PlayerDeaths.containsKey(uuid)) {

			return StatsLoading.PlayerDeaths.get(uuid);

		}

		return 0.0;

	}

	//CAPTURES


	public static void addCaptures(UUID uuid, double value) {

		double save = StatsLoading.PlayerCaptures.get(uuid);

		if (StatsLoading.PlayerCaptures.containsKey(uuid)) {

			StatsLoading.PlayerCaptures.remove(uuid);

		}

		StatsLoading.PlayerCaptures.put(uuid, save + value);
	}

	public static void removeCaptures(UUID uuid, double value) {

		double save = StatsLoading.PlayerCaptures.get(uuid);

		if (StatsLoading.PlayerCaptures.containsKey(uuid)) {

			StatsLoading.PlayerCaptures.remove(uuid);

		}

		StatsLoading.PlayerCaptures.put(uuid, save - value);

	}

	public static void setCaptures(UUID uuid, double value) {

		if (StatsLoading.PlayerCaptures.containsKey(uuid)) {

			StatsLoading.PlayerCaptures.remove(uuid);

		}

		StatsLoading.PlayerCaptures.put(uuid, value);

	}

	public static double getCaptures(UUID uuid) {

		if (StatsLoading.PlayerCaptures.containsKey(uuid)) {

			return StatsLoading.PlayerCaptures.get(uuid);

		}

		return 0.0;

	}


	//ASSISTS


	public static void addAssists(UUID uuid, double value) {

		double save = StatsLoading.PlayerAssists.get(uuid);

		if (StatsLoading.PlayerAssists.containsKey(uuid)) {

			StatsLoading.PlayerAssists.remove(uuid);

		}

		StatsLoading.PlayerAssists.put(uuid, save + value);
	}

	public static void removeAssists(UUID uuid, double value) {

		double save = StatsLoading.PlayerAssists.get(uuid);

		if (StatsLoading.PlayerAssists.containsKey(uuid)) {

			StatsLoading.PlayerAssists.remove(uuid);

		}

		StatsLoading.PlayerAssists.put(uuid, save - value);

	}

	public static void setAssists(UUID uuid, double value) {

		if (StatsLoading.PlayerAssists.containsKey(uuid)) {

			StatsLoading.PlayerAssists.remove(uuid);

		}

		StatsLoading.PlayerAssists.put(uuid, value);
	}

	public static double getAssists(UUID uuid) {

		if (StatsLoading.PlayerAssists.containsKey(uuid)) {

			return StatsLoading.PlayerAssists.get(uuid);

		}

		return 0.0;
	}


	//HEALS


	public static void addHeals(UUID uuid, double value) {

		double save = StatsLoading.PlayerHeals.get(uuid);

		if (StatsLoading.PlayerHeals.containsKey(uuid)) {

			StatsLoading.PlayerHeals.remove(uuid);

		}

		StatsLoading.PlayerHeals.put(uuid, save + value);
	}

	public static void removeHeals(UUID uuid, double value) {

		double save = StatsLoading.PlayerHeals.get(uuid);

		if (StatsLoading.PlayerHeals.containsKey(uuid)) {

			StatsLoading.PlayerHeals.remove(uuid);

		}

		StatsLoading.PlayerHeals.put(uuid, save - value);

	}

	public static void setHeals(UUID uuid, double value) {

		if (StatsLoading.PlayerHeals.containsKey(uuid)) {

			StatsLoading.PlayerHeals.remove(uuid);

		}

		StatsLoading.PlayerHeals.put(uuid, value);
	}

	public static double getHeals(UUID uuid) {

		if (StatsLoading.PlayerHeals.containsKey(uuid)) {

			return StatsLoading.PlayerHeals.get(uuid);

		}

		return 0.0;
	}


	//SUPPORTS


	public static void addSupports(UUID uuid, double value) {

		double save = StatsLoading.PlayerSupports.get(uuid);

		if (StatsLoading.PlayerSupports.containsKey(uuid)) {

			StatsLoading.PlayerSupports.remove(uuid);

		}

		StatsLoading.PlayerSupports.put(uuid, save + value);
	}

	public static void removeSupports(final UUID uuid, final double value) {

		double save = StatsLoading.PlayerSupports.get(uuid);

		if (StatsLoading.PlayerSupports.containsKey(uuid)) {

			StatsLoading.PlayerSupports.remove(uuid);

		}

		StatsLoading.PlayerSupports.put(uuid, save - value);

	}

	public static void setSupports(final UUID uuid, final double value) {

		if (StatsLoading.PlayerSupports.containsKey(uuid)) {

			StatsLoading.PlayerSupports.remove(uuid);

		}

		StatsLoading.PlayerSupports.put(uuid, value);
	}

	public static double getSupports(UUID uuid) {

		if (StatsLoading.PlayerSupports.containsKey(uuid)) {

			return StatsLoading.PlayerSupports.get(uuid);

		}

		return 0.0;

	}


	//KILLSTREAKS


	public static void addKillstreak(UUID uuid, int value) {

		int save = StatsLoading.PlayerKillstreak.get(uuid);

		if (StatsLoading.PlayerKillstreak.containsKey(uuid)) {

			StatsLoading.PlayerKillstreak.remove(uuid);

		}

		StatsLoading.PlayerKillstreak.put(uuid, save + value);

	}

	public static void removeKillstreak(UUID uuid, int value) {

		int save = StatsLoading.PlayerKillstreak.get(uuid);

		if (StatsLoading.PlayerKillstreak.containsKey(uuid)) {

			StatsLoading.PlayerKillstreak.remove(uuid);

		}

		StatsLoading.PlayerKillstreak.put(uuid, save - value);

	}

	public static void setKillstreak(UUID uuid, int value) {

		if (StatsLoading.PlayerKillstreak.containsKey(uuid)) {

			StatsLoading.PlayerKillstreak.remove(uuid);

		}

		StatsLoading.PlayerKillstreak.put(uuid, value);
	}

	public static int getKillstreak(UUID uuid) {

		if (StatsLoading.PlayerKillstreak.containsKey(uuid)) {

			return StatsLoading.PlayerKillstreak.get(uuid);

		}

		return 0;
	}


	public static void addLevel(UUID uuid, int value) {

		int save = StatsLoading.PlayerLevel.get(uuid);

		if (StatsLoading.PlayerLevel.containsKey(uuid)) {

			StatsLoading.PlayerLevel.remove(uuid);

		}

		StatsLoading.PlayerLevel.put(uuid, save + value);

	}

	public static void removeLevel(UUID uuid, int value) {

		int save = StatsLoading.PlayerLevel.get(uuid);

		if (StatsLoading.PlayerLevel.containsKey(uuid)) {

			StatsLoading.PlayerLevel.remove(uuid);

		}

		StatsLoading.PlayerLevel.put(uuid, save - value);

	}

	public static void setLevel(UUID uuid, int value) {

		if (StatsLoading.PlayerLevel.containsKey(uuid)) {

			StatsLoading.PlayerLevel.remove(uuid);

		}

		StatsLoading.PlayerLevel.put(uuid, value);

	}

	public static int getLevel(UUID uuid) {

		if (StatsLoading.PlayerLevel.containsKey(uuid)) {

			return StatsLoading.PlayerLevel.get(uuid);

		}

		return 0;

	}

	public static void setKit(UUID uuid, String kit) {

		if (StatsLoading.PlayerKit.containsKey(uuid)) {

			StatsLoading.PlayerKit.remove(uuid);

		}

		StatsLoading.PlayerKit.put(uuid, kit);

	}

	public static String getKit(UUID uuid) {

		if (StatsLoading.PlayerKit.containsKey(uuid)) {

			return StatsLoading.PlayerKit.get(uuid);

		} else {

			return "swordsman";
		}

	}

	public static void setRank(UUID uuid, String rank) {

		if (StatsLoading.PlayerRank.containsKey(uuid)) {

			StatsLoading.PlayerRank.remove(uuid);

		}

		StatsLoading.PlayerRank.put(uuid, rank);

	}

	public static String getRank(UUID uuid) {

		if (StatsLoading.PlayerRank.containsKey(uuid)) {

			return StatsLoading.PlayerRank.get(uuid);

		} else {

			return "None";
		}

	}

	public static void setStaffRank(UUID uuid, String rank) {

		if (StatsLoading.PlayerStaffRank.containsKey(uuid)) {

			StatsLoading.PlayerStaffRank.remove(uuid);

		}

		StatsLoading.PlayerStaffRank.put(uuid, rank);

	}

	public static String getStaffRank(UUID uuid) {

		if (StatsLoading.PlayerStaffRank.containsKey(uuid)) {

			return StatsLoading.PlayerStaffRank.get(uuid);

		} else {

			return "None";
		}

	}

}
