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

public class CavesFlag implements Listener {

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
							FlagRadius.onWalkInCaves(p);

							if (FlagRadius.ThisTeamIsCapturing(2, "Caves")) {

								if (FlagCounter.isFlagCounter("Caves", 0) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 15) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 14) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 13) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 12) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 11) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 10) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 9) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 8) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 7) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 6) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 5) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 4) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 3) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 2) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 1) && FlagRadius.Caves2.contains(p)) {
									Caves_Rohan.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("Caves")) {
								
								if (FlagRadius.isPlayerInRadius(p, "Caves")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}


						}

					}
				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("Caves", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					if (MapController.currentMapIs("Helmsdeep")) {
						
						for (Player p : PlayerTeam.Urukhai) {

							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInCaves(p);

							if (FlagRadius.ThisTeamIsCapturing(1, "Caves")) {


								if (FlagCounter.isFlagCounter("Caves", 0) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 1) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 2) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 3) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 4) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 5) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 6) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 7) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 8) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 9) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 10) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 11) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 12) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 13) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 14) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("Caves", 15) && FlagRadius.Caves1.contains(p)) {
									Caves_Urukhai.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("Caves")) {
								
								if (FlagRadius.isPlayerInRadius(p, "Caves")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}
						}
					}

				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("Caves", 1));

		}
	}

}
