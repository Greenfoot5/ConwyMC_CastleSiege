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

public class StairhallFlag implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onCapture(PlayerJoinEvent e) {

		/*if (MapController.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "stairhall")) {

								if (TS_FlagCounter.isFlagCounter("stairhall", 0) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 15) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 14) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 13) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 12) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 11) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 10) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 9) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 8) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 7) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 6) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 5) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 4) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 3) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 2) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 1) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("stairhall")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "stairhall")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}

						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("stairhall", 2));






			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "stairhall")) {


								if (TS_FlagCounter.isFlagCounter("stairhall", 0) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 1) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 2) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 3) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 4) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 5) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 6) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 7) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 8) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 9) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 10) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 11) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 12) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 13) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 14) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 15) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("stairhall")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "stairhall")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("stairhall", 1));

		}*/
	}




	@EventHandler
	public void onCapture2(PlayerChangedWorldEvent e) {

		/*if (MapController.currentMapIs("Thunderstone")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.ThunderstoneGuards) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(2, "stairhall")) {

								if (TS_FlagCounter.isFlagCounter("stairhall", 0) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 15) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 14) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 13) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 12) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 11) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 10) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 9) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 8) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 7) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 6) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 5) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 4) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 3) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 2) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 1) && TS_FlagRadius.stairhall2.contains(p)) {
									Stairhall_ThunderstoneGuards.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("stairhall")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "stairhall")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}

						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("stairhall", 2));






			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Thunderstone")) {

						for (Player p : PlayerTeam.Cloudcrawlers) {

							TS_FlagRadius.onWalkAway(p);
							TS_FlagRadius.onWalkIn(p);

							if (TS_FlagRadius.ThisTeamIsCapturing(1, "stairhall")) {


								if (TS_FlagCounter.isFlagCounter("stairhall", 0) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA16(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 1) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA15(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 2) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA14(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 3) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA13(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 4) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA12(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 5) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA11(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 6) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA10(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 7) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA9(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 8) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA8(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 9) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA7(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 10) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA6(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 11) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA5(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 12) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA4(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 13) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA3(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 14) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA2(p);
								}
								if (TS_FlagCounter.isFlagCounter("stairhall", 15) && TS_FlagRadius.stairhall1.contains(p)) {
									Stairhall_Cloudcrawlers.CapturingProcessA1(p);
								}

							} else if (TS_FlagRadius.isFlagContested("stairhall")) {

								if (TS_FlagRadius.isPlayerInRadius(p, "stairhall")) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, TS_FlagCaptureSpeed.flagCaptureSpeed("stairhall", 1));

		}*/
	}
}

