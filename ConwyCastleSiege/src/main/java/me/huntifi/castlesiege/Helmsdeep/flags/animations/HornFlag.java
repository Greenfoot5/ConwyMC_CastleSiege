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

public class HornFlag implements Listener {

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
							FlagRadius.onWalkInHorn(p);

							if (FlagRadius.ThisTeamIsCapturing(2, "Horn")) {

								if (FlagCounter.isFlagCounter("Horn", 0) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 15) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 14) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 13) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 12) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 11) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 10) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 9) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 8) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 7) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 6) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 5) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 4) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 3) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 2) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 1) && FlagRadius.Horn2.contains(p)) {
									Horn_Rohan.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("Horn")) {
								
								if (FlagRadius.isPlayerInRadius(p, "Horn")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}

						}
					}
				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("Horn", 2));


			new BukkitRunnable() {

				@Override
				public void run() {

					for (Player p : PlayerTeam.Urukhai) {

						if (MapController.currentMapIs("Helmsdeep")) {


							FlagRadius.onWalkAway(p);
							FlagRadius.onWalkInHorn(p);

							if (FlagRadius.ThisTeamIsCapturing(1, "Horn")) {

								if (FlagCounter.isFlagCounter("Horn", 0) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA16(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 1) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA15(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 2) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA14(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 3) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA13(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 4) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA12(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 5) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA11(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 6) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA10(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 7) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA9(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 8) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA8(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 9) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA7(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 10) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA6(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 11) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA5(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 12) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA4(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 13) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA3(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 14) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA2(p);
								}
								if (FlagCounter.isFlagCounter("Horn", 15) && FlagRadius.Horn1.contains(p)) {
									Horn_Urukhai.CapturingProcessA1(p);
								}

							} else if (FlagRadius.isFlagContested("Horn")) {
								
								if (FlagRadius.isPlayerInRadius(p, "Horn")) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "The flag is being contested!"));
								
								}
							}
						}
					}
				}
			}.runTaskTimer(plugin, 0, FlagCaptureSpeed.flagCaptureSpeed("Horn", 1));

		}
	}
}

