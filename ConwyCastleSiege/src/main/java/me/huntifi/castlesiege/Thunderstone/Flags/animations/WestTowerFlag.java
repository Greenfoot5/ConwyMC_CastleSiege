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

public class WestTowerFlag implements Listener {

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

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "westtower")) {

								if (TS_FlagCounter.isFlagCounter("westtower", 0) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 15) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 14) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 13) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 12) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 11) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 10) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 9) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 8) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 7) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 6) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 5) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 4) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 3) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 2) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 1) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("westtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "westtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}

							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("westtower", 2));

			new BukkitRunnable() {

				@Override
				public void run() {


					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "westtower")) {


								if (TS_FlagCounter.isFlagCounter("westtower", 0) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 1) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 2) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 3) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 4) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 5) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 6) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 7) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 8) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 9) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 10) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 11) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 12) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 13) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 14) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 15) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("westtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "westtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}

							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("westtower", 1));

		}
	}




	@EventHandler
	public void onCapture(PlayerChangedWorldEvent e) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "westtower")) {

								if (TS_FlagCounter.isFlagCounter("westtower", 0) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 15) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 14) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 13) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 12) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 11) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 10) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 9) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 8) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 7) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 6) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 5) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 4) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 3) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 2) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 1) && TS_FlagRadius.westtower2.contains(p)) {
									WestTower_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("westtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "westtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("westtower", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "westtower")) {


								if (TS_FlagCounter.isFlagCounter("westtower", 0) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 1) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 2) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 3) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 4) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 5) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 6) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 7) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 8) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 9) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 10) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 11) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 12) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 13) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 14) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("westtower", 15) && TS_FlagRadius.westtower1.contains(p)) {
									WestTower_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("westtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "westtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}

							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("westtower", 1));

		}
	}

}
