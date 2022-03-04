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
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SkyviewTowerFlag implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onCapture(PlayerJoinEvent e) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "skyviewtower")) {

								if (TS_FlagCounter.isFlagCounter("skyviewtower", 0) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 7) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 6) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 5) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 4) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 3) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 2) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 1) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("skyviewtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "skyviewtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("skyviewtower", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "skyviewtower")) {

								if (TS_FlagCounter.isFlagCounter("skyviewtower", 0) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 1) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 2) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 3) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 4) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 5) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 6) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 7) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("skyviewtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "skyviewtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("skyviewtower", 1));


		}
	}





	@EventHandler
	public void onCapture2(PlayerChangedWorldEvent e) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "skyviewtower")) {

								if (TS_FlagCounter.isFlagCounter("skyviewtower", 0) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 7) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 6) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 5) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 4) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 3) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 2) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 1) && TS_FlagRadius.skyviewtower2.contains(p)) {
									Skyview_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("skyviewtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "skyviewtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("skyviewtower", 2));


			new BukkitRunnable() {

				@Override
				public void run() {
					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "skyviewtower")) {

								if (TS_FlagCounter.isFlagCounter("skyviewtower", 0) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 1) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 2) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 3) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 4) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 5) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 6) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("skyviewtower", 7) && TS_FlagRadius.skyviewtower1.contains(p)) {
									Skyview_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("skyviewtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "skyviewtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("skyviewtower", 1));


		}
	}
}
