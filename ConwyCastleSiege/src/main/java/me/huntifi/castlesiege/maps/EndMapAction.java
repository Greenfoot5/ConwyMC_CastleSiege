package me.huntifi.castlesiege.maps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.joinevents.stats.MainStats;
import me.huntifi.castlesiege.stats.levels.LevelingEvent;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

/**
 * Handles ending a map
 */
public class EndMapAction {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	/**
	 * End a map
	 * @param mapName The MapsList map that's ended
	 */
	public static void endMap(MapsList mapName)
	{
		switch (mapName)
		{
			case HelmsDeep:
				// Moves all players to the next location
				for (Player player : Bukkit.getOnlinePlayers()) {
					Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1);
					if (PlayerTeam.playerIsInTeam(player, 1)) {
						loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17);
					}
					teleportPlayer(player, loc);
				}
				LobbyPlayer.lobby.clear();

				new BukkitRunnable() {

					@Override
					public void run() {

						PlayerTeam.Urukhai.clear();
						PlayerTeam.Rohan.clear();

						for (Player all : Bukkit.getOnlinePlayers()) {

							all.sendMessage(ChatColor.YELLOW + "*** PREPAIRING THE BATTLEFIELD ***");
							all.sendMessage(ChatColor.GRAY + "Expect some lag for a few seconds...");

						}

						MapController.nextMap();

						new BukkitRunnable() {

							@Override
							public void run() {

								for (Player all : Bukkit.getOnlinePlayers()) {

									all.sendMessage(ChatColor.YELLOW + "Starting new game...");

								}
								JoinNewTeam.joinATeam(MapsList.Thunderstone);

							}

						}.runTaskLater(plugin, 60);

					}

				}.runTaskLater(plugin, 150);

				break;

			case Thunderstone:
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (PlayerTeam.playerIsInTeam(player, 2)) {
						Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -187, 203, 106, -90, 2);
						teleportPlayer(player, loc);

					}
					else if (PlayerTeam.playerIsInTeam(player, 1)) {
						Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -191, 203, 159, -90, 1);
						teleportPlayer(player, loc);
					}
				}
				LobbyPlayer.lobby.clear();

				new BukkitRunnable() {

					@Override
					public void run() {

						PlayerTeam.Cloudcrawlers.clear();
						PlayerTeam.ThunderstoneGuards.clear();

						for (Player all : Bukkit.getOnlinePlayers()) {

							if (all != null) {

								LevelingEvent.doLeveling();
								MainStats.updateStats(all.getUniqueId(), all);

							}

							all.sendMessage(ChatColor.YELLOW + "Restarting the server in 10 seconds...");
							all.sendMessage(ChatColor.YELLOW + "Kicking all players in 5 seconds... ");

						}

						new BukkitRunnable() {

							@Override
							public void run() {

								for (Player all : Bukkit.getOnlinePlayers()) {

									all.kickPlayer(ChatColor.YELLOW + "This Castle Siege server is now restarting.");

								}

								new BukkitRunnable() {

									@Override
									public void run() {

										MapController.nextMap();

									}

								}.runTaskLater(plugin, 100);

							}

						}.runTaskLater(plugin, 100);

					}

				}.runTaskLater(plugin, 150);

				break;
		}
	}

	/**
	 * Teleports a player to the next lobby
	 * @param player The player to teleport
	 * @param location The new location of the player
	 */
	private static void teleportPlayer(Player player, Location location)
	{
		if (!LobbyPlayer.containsPlayer(player)) { LobbyPlayer.addPlayer(player); }
		player.teleport(location);

		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		player.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)));
	}
}
