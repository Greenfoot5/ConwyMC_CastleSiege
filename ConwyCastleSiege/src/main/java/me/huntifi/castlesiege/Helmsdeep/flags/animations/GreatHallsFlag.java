package me.huntifi.castlesiege.Helmsdeep.flags.animations;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagCaptureSpeed;
import me.huntifi.castlesiege.Helmsdeep.flags.FlagCounter;
import me.huntifi.castlesiege.Helmsdeep.flags.FlagRadius;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GreatHallsFlag implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onCapture(PlayerJoinEvent e) {

		if (MapController.currentMapIs("Helmsdeep")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Helmsdeep")) {


						for (Player p : PlayerTeam.Rohan) {

							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInGreatHall(p);

							if (FlagRadius.ThisTeamIsCapturing(2, "GreatHalls")) {

								if (FlagCounter.isFlagCounter("GreatHalls", 0) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 15) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 14) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 13) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 12) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 11) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 10) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 9) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 8) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 7) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 6) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 5) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 4) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 3) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 2) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 1) && FlagRadius.GreatHalls2.contains(p)) {
									GreatHalls_Rohan.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("GreatHalls")) {
								
								if (FlagRadius.isPlayerInRadius(p, "GreatHalls")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}

						}
					}

				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("GreatHalls", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Helmsdeep")) {

						for (Player p : PlayerTeam.Urukhai) {


							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInGreatHall(p);

							if (FlagRadius.ThisTeamIsCapturing(1, "GreatHalls")) {


								if (FlagCounter.isFlagCounter("GreatHalls", 0) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 1) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 2) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 3) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 4) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 5) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 6) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 7) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 8) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 9) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 10) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 11) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 12) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 13) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 14) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("GreatHalls", 15) && FlagRadius.GreatHalls1.contains(p)) {
									GreatHalls_Urukhai.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("GreatHalls")) {
								
								if (FlagRadius.isPlayerInRadius(p, "GreatHalls")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("GreatHalls", 1));

		}
	}

}

