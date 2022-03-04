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

public class SupplyCampFlag implements Listener {

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
							FlagRadius.onWalkInCamp(p);

							if (FlagRadius.ThisTeamIsCapturing(2, "SupplyCamp")) {

								if (FlagCounter.isFlagCounter("SupplyCamp", 0) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 15) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 14) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 13) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 12) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 11) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 10) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 9) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 8) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 7) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 6) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 5) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 4) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 3) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 2) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 1) && FlagRadius.SupplyCamp2.contains(p)) {
									SupplyCamp_Rohan.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("SupplyCamp")) {
								
								if (FlagRadius.isPlayerInRadius(p, "SupplyCamp")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}

						}
					}
				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("SupplyCamp", 2));

			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Helmsdeep")) {

						for (Player p : PlayerTeam.Urukhai) {


							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInCamp(p);

							if (FlagRadius.ThisTeamIsCapturing(1, "SupplyCamp")) {


								if (FlagCounter.isFlagCounter("SupplyCamp", 0) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 1) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 2) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 3) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 4) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 5) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 6) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 7) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 8) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 9) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 10) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 11) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 12) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 13) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 14) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("SupplyCamp", 15) && FlagRadius.SupplyCamp1.contains(p)) {
									SupplyCamp_Urukhai.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("SupplyCamp")) {
								
								if (FlagRadius.isPlayerInRadius(p, "SupplyCamp")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}
						}
					}

				}

			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("SupplyCamp", 1));
		}
	}
}