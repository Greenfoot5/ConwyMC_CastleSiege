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

public class EndMapAction {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void HelmsdeepEndMap() {	

		if (currentMaps.currentMapIs("Helmsdeep")) {

			for (Player all : Bukkit.getOnlinePlayers()) {

				if (PlayerTeam.playerIsInTeam(all, 2)) {

					Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1);
					LobbyPlayer.lobby.clear();
					if (!LobbyPlayer.containsPlayer(all)) { LobbyPlayer.addPlayer(all); }
					all.teleport(loc);

					for (PotionEffect effect : all.getActivePotionEffects())
						all.removePotionEffect(effect.getType());
					all.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)));

				}

				if (PlayerTeam.playerIsInTeam(all, 1)) {

					Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17);
					LobbyPlayer.lobby.clear();
					if (!LobbyPlayer.containsPlayer(all)) { LobbyPlayer.addPlayer(all); }
					all.teleport(loc);

					for (PotionEffect effect : all.getActivePotionEffects())
						all.removePotionEffect(effect.getType());
					all.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)));

				}
			}

			new BukkitRunnable() {

				@Override
				public void run() {

					PlayerTeam.Urukhai.clear();
					PlayerTeam.Rohan.clear();

					for (Player all : Bukkit.getOnlinePlayers()) {

						all.sendMessage(ChatColor.YELLOW + "*** PREPAIRING THE BATTLEFIELD ***");
						all.sendMessage(ChatColor.GRAY + "Expect some lag for a few seconds...");

					}

					NextMap.nextMap();

					new BukkitRunnable() {

						@Override
						public void run() {

							for (Player all : Bukkit.getOnlinePlayers()) {
								
								all.sendMessage(ChatColor.YELLOW + "Starting new game...");

							}
							joinNewTeam.ThunderstoneJoinNewTeam();

						}

					}.runTaskLater(plugin, 60);

				}

			}.runTaskLater(plugin, 150);

		} 


	}




	public static void ThunderstoneEndMap() {	

		if (currentMaps.currentMapIs("Thunderstone")) {

			for (Player all : Bukkit.getOnlinePlayers()) {

				if (PlayerTeam.playerIsInTeam(all, 2)) {

					Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -187, 203, 106, -90, 2);
					LobbyPlayer.lobby.clear();
					if (!LobbyPlayer.containsPlayer(all)) { LobbyPlayer.addPlayer(all); }
					all.teleport(loc);

					for (PotionEffect effect : all.getActivePotionEffects())
						all.removePotionEffect(effect.getType());
					all.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)));

				}

				if (PlayerTeam.playerIsInTeam(all, 1)) {

					Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -191, 203, 159, -90, 1);
					LobbyPlayer.lobby.clear();
					if (!LobbyPlayer.containsPlayer(all)) { LobbyPlayer.addPlayer(all); }
					all.teleport(loc);

					for (PotionEffect effect : all.getActivePotionEffects())
						all.removePotionEffect(effect.getType());
					all.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)));

				}
			}

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

											NextMap.nextMap();


									}

								}.runTaskLater(plugin, 100);
								
								

						}

					}.runTaskLater(plugin, 100);

				}

			}.runTaskLater(plugin, 150);



		} 


	}

}
