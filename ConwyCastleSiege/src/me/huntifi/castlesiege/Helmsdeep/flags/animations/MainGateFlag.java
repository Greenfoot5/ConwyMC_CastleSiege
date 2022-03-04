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

public class MainGateFlag implements Listener {

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
							FlagRadius.onWalkInMaingate(p);
							
							if (FlagRadius.ThisTeamIsCapturing(2, "MainGate")) {

								if (FlagCounter.isFlagCounter("MainGate", 0) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 7) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 6) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 5) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 4) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 3) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 2) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 1) && FlagRadius.MainGate2.contains(p)) {
									MainGate_Rohan.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("MainGate")) {
								
								if (FlagRadius.isPlayerInRadius(p, "MainGate")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}

						}
					}

				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("MainGate", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					if (currentMaps.currentMapIs("Helmsdeep")) {

						for (Player p : PlayerTeam.Urukhai) {

							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInMaingate(p);

							if (FlagRadius.ThisTeamIsCapturing(1, "MainGate")) {

								if (FlagCounter.isFlagCounter("MainGate", 0) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 1) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 2) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 3) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 4) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 5) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 6) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("MainGate", 7) && FlagRadius.MainGate1.contains(p)) {
									MainGate_Urukhai.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("MainGate")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
							}
						}
					}
				}

			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("MainGate", 1));
		}


	}
}
