package me.huntifi.castlesiege.Thunderstone.Flags.animations;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagCaptureSpeed;
import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagCounter;
import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagRadius;
import me.huntifi.castlesiege.maps.MapController;
//import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TwinbridgeFlag implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onCapture(PlayerJoinEvent e) {

		if (MapController.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {
					
					if (MapController.currentMapIs("Thunderstone")) {

					/*for (Player p : PlayerTeam.ThunderstoneGuards) {

						TS_FlagRadius.onWalkAway(p);
						TS_FlagRadius.onWalkIn(p);

						if (TS_FlagRadius.ThisTeamIsCapturing(2, "twinbridge")) {

							if (TS_FlagCounter.isFlagCounter("twinbridge", 0) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA16(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 15) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA15(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 14) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA14(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 13) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA13(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 12) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA12(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 11) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA11(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 10) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA10(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 9) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA9(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 8) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA8(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 7) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA7(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 6) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA6(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 5) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA5(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 4) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA4(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 3) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA3(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 2) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA2(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 1) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA1(p);
							}

						} else if (TS_FlagRadius.isFlagContested("twinbridge")) {

							if (TS_FlagRadius.isPlayerInRadius(p, "twinbridge")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
							}
						}
					}*/

					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("twinbridge", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						/*for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "twinbridge")) {


								if (TS_FlagCounter.isFlagCounter("twinbridge", 0) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 1) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 2) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 3) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 4) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 5) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 6) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 7) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 8) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 9) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 10) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 11) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 12) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 13) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 14) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 15) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("twinbridge")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "twinbridge")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}*/
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("twinbridge", 1));

		}

	}
	
	
	
	
	
	@EventHandler
	public void onCapture2(PlayerChangedWorldEvent e) {

		if (MapController.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {
					
					if (MapController.currentMapIs("Thunderstone")) {

					/*for (Player p : PlayerTeam.ThunderstoneGuards) {

						TS_FlagRadius.onWalkAway(p);
						TS_FlagRadius.onWalkIn(p);

						if (TS_FlagRadius.ThisTeamIsCapturing(2, "twinbridge")) {

							if (TS_FlagCounter.isFlagCounter("twinbridge", 0) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA16(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 15) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA15(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 14) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA14(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 13) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA13(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 12) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA12(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 11) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA11(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 10) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA10(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 9) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA9(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 8) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA8(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 7) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA7(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 6) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA6(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 5) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA5(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 4) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA4(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 3) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA3(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 2) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA2(p);
							}
							if (TS_FlagCounter.isFlagCounter("twinbridge", 1) && TS_FlagRadius.twinbridge2.contains(p)) {
								Twinbridge_ThunderstoneGuards.CapturingProcessA1(p);
							}

						} else if (TS_FlagRadius.isFlagContested("twinbridge")) {

							if (TS_FlagRadius.isPlayerInRadius(p, "twinbridge")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
							}
						}
					}*/

					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("twinbridge", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						/*for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "twinbridge")) {


								if (TS_FlagCounter.isFlagCounter("twinbridge", 0) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 1) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 2) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 3) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 4) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 5) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 6) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 7) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 8) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 9) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 10) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 11) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 12) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 13) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 14) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("twinbridge", 15) && TS_FlagRadius.twinbridge1.contains(p)) {
									Twinbridge_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("twinbridge")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "twinbridge")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}*/
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("twinbridge", 1));

		}

	}
}
