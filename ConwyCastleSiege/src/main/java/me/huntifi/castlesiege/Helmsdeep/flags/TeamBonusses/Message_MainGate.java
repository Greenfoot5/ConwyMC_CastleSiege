package me.huntifi.castlesiege.Helmsdeep.flags.TeamBonusses;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagRadius;
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Message_MainGate {
 
	public static void CapturesMessages(Player p, String flag) {

		if (currentMaps.currentMapIs("HelmsDeep")) {

			if (FlagRadius.isPlayerInRadius(p, flag)) {

				if (flag.equalsIgnoreCase("MainGate")) {

					if (FlagRadius.MainGate1.contains(p) || FlagRadius.MainGate2.contains(p)) {

						if (FlagRadius.MainGate1.size() == 1 || FlagRadius.MainGate2.size() == 1) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+1 flagcapping point(s)" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);

								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+1 flagcapping point(s)" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);

								}
							}

						}

						if (FlagRadius.MainGate1.size() == 2 || FlagRadius.MainGate2.size() == 2) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+2 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);

								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+2 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);

								}
							}

						}

						if (FlagRadius.MainGate1.size() == 3 || FlagRadius.MainGate2.size() == 3) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+3 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);

								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+3 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);

								}
							}

						}

						if (FlagRadius.MainGate1.size() >= 4 || FlagRadius.MainGate2.size() >= 4) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+4 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 4);

								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+4 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 4);

								}
							}

						}
					}

				}
			}
		}
	}

	public static void CapturesMessagesFinal(Player p, String flag) {

		if (currentMaps.currentMapIs("HelmsDeep")) {

			if (FlagRadius.isPlayerInRadius(p, flag)) {

				if (flag.equalsIgnoreCase("MainGate")) {

					if (FlagRadius.MainGate1.contains(p) || FlagRadius.MainGate2.contains(p)) {

						if (FlagRadius.MainGate1.size() == 1 || FlagRadius.MainGate2.size() == 1) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);
									return;
								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);
									return;
								}
							}

						}

						if (FlagRadius.MainGate1.size() == 2 || FlagRadius.MainGate2.size() == 2) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);
									return;
								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);
									return;
								}
							}

						}

						if (FlagRadius.MainGate1.size() == 3 || FlagRadius.MainGate2.size() == 3) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);
									return;
								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);
									return;
								}
							}

						}

						if (FlagRadius.MainGate1.size() >= 4 || FlagRadius.MainGate2.size() >= 4) {

							if (FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : FlagRadius.MainGate1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 4);
									return;
								}
							}

							if (FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : FlagRadius.MainGate2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The Main Gate"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 4);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void playSound(Player player) {
		
		Location location = player.getLocation();
		
		Sound effect = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		player.playSound(location, effect, volume, pitch);
	}
}
