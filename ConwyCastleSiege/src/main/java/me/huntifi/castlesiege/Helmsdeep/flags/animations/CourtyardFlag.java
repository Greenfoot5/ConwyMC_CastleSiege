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
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CourtyardFlag implements Listener {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onCapture(PlayerJoinEvent e) {

		if (currentMaps.currentMapIs("Helmsdeep")) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Helmsdeep")) {

						for (Player p : PlayerTeam.Rohan) {

							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInCourtyard(p);

							if (FlagRadius.ThisTeamIsCapturing(2, "Courtyard")) {

								if (FlagCounter.isFlagCounter("Courtyard", 0) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 15) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 14) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 13) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 12) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 11) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 10) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 9) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 8) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 7) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 6) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 5) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 4) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 3) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 2) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 1) && FlagRadius.Courtyard2.contains(p)) {
									Courtyard_Rohan.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("Courtyard")) {
								
								if (FlagRadius.isPlayerInRadius(p, "Courtyard")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}

						}

					}
				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("Courtyard", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Helmsdeep")) {

						for (Player p : PlayerTeam.Urukhai) {

							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInCourtyard(p);

							if (FlagRadius.ThisTeamIsCapturing(1, "Courtyard")) {


								if (FlagCounter.isFlagCounter("Courtyard", 0) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 1) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 2) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 3) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 4) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 5) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 6) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 7) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 8) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 9) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 10) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 11) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 12) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 13) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 14) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("Courtyard", 15) && FlagRadius.Courtyard1.contains(p)) {
									Courtyard_Urukhai.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("Courtyard")) {
								
								if (FlagRadius.isPlayerInRadius(p, "Courtyard")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("Courtyard", 1));

		}

	}
}

