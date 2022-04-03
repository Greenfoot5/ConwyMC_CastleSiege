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

public class EastTowerFlag implements Listener {

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

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "easttower")) {

								if (TS_FlagCounter.isFlagCounter("easttower", 0) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 15) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 14) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 13) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 12) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 11) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 10) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 9) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 8) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 7) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 6) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 5) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 4) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 3) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 2) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 1) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("easttower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "easttower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));

								}
							}
						}

					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("easttower", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "easttower")) {


								if (TS_FlagCounter.isFlagCounter("easttower", 0) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 1) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 2) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 3) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 4) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 5) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 6) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 7) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 8) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 9) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 10) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 11) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 12) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 13) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 14) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 15) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("easttower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "easttower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));

								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("easttower", 1));

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

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "easttower")) {

								if (TS_FlagCounter.isFlagCounter("easttower", 0) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 15) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 14) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 13) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 12) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 11) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 10) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 9) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 8) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 7) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 6) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 5) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 4) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 3) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 2) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 1) && TS_FlagRadius.easttower2.contains(p)) {
									EastTower_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("easttower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "easttower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));

								}
							}
						}

					}
				}

			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("easttower", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "easttower")) {


								if (TS_FlagCounter.isFlagCounter("easttower", 0) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 1) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 2) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 3) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 4) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 5) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 6) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 7) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 8) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 9) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 10) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 11) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 12) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 13) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 14) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("easttower", 15) && TS_FlagRadius.easttower1.contains(p)) {
									EastTower_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("easttower")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "easttower")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));

								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("easttower", 1));

		}
	}

}
