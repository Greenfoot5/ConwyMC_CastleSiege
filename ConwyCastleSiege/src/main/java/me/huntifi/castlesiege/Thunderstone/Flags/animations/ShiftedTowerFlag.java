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
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ShiftedTowerFlag implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onCapture(PlayerJoinEvent e) {

		if (MapController.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "shiftedtower")) {

								if (TS_FlagCounter.isFlagCounter("shiftedtower", 0) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 15) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 14) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 13) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 12) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 11) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 10) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 9) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 8) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 7) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 6) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 5) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 4) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 3) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 2) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 1) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("shiftedtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "shiftedtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}

					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("shiftedtower", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "shiftedtower")) {


								if (TS_FlagCounter.isFlagCounter("shiftedtower", 0) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 1) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 2) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 3) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 4) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 5) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 6) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 7) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 8) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 9) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 10) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 11) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 12) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 13) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 14) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 15) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("shiftedtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "shiftedtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("shiftedtower", 1));

		}

	}






	@EventHandler
	public void onCapture2(PlayerChangedWorldEvent e) {

		if (MapController.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "shiftedtower")) {

								if (TS_FlagCounter.isFlagCounter("shiftedtower", 0) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 15) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 14) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 13) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 12) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 11) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 10) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 9) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 8) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 7) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 6) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 5) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 4) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 3) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 2) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 1) && TS_FlagRadius.shiftedtower2.contains(p)) {
									ShiftedTower_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("shiftedtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "shiftedtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}

					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("shiftedtower", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "shiftedtower")) {


								if (TS_FlagCounter.isFlagCounter("shiftedtower", 0) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 1) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 2) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 3) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 4) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 5) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 6) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 7) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 8) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 9) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 10) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 11) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 12) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 13) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 14) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("shiftedtower", 15) && TS_FlagRadius.shiftedtower1.contains(p)) {
									ShiftedTower_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("shiftedtower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "shiftedtower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}

						}

					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("shiftedtower", 1));

		}

	}
}
