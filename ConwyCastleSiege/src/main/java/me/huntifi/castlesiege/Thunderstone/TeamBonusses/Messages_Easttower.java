package me.huntifi.castlesiege.Thunderstone.TeamBonusses;

import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagRadius;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Messages_Easttower {

	public static void CapturesMessages(Player p, String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (TS_FlagRadius.isPlayerInRadius(p, flag)) {

				if (flag.equalsIgnoreCase("easttower")) {

					if (TS_FlagRadius.easttower1.contains(p) || TS_FlagRadius.easttower2.contains(p)) {

						if (TS_FlagRadius.easttower1.size() <= 1 || TS_FlagRadius.easttower2.size() <= 1) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+1 flagcapping point(s)" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);
									
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+1 flagcapping point(s)" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);
									
								}
							}

						}

						if (TS_FlagRadius.easttower1.size() == 2 || TS_FlagRadius.easttower2.size() == 2) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+2 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);
									
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+2 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);
									
								}
							}

						}

						if (TS_FlagRadius.easttower1.size() == 3 || TS_FlagRadius.easttower2.size() == 3) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+3 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);
									
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+3 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);
									
								}
							}

						}

						if (TS_FlagRadius.easttower1.size() >= 4 || TS_FlagRadius.easttower2.size() >= 4) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+4 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 4);
									
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+4 flagcapping point(s) [Team Bonus]" + ChatColor.AQUA + " Flag: The East Tower"));
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

		if (MapController.currentMapIs("Thunderstone")) {

			if (TS_FlagRadius.isPlayerInRadius(p, flag)) {

				if (flag.equalsIgnoreCase("easttower")) {

					if (TS_FlagRadius.easttower1.contains(p) || TS_FlagRadius.easttower2.contains(p)) {

						if (TS_FlagRadius.easttower1.size() <= 1 || TS_FlagRadius.easttower2.size() <= 1) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);
									return;
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 1);
									return;
								}
							}

						}

						 if (TS_FlagRadius.easttower1.size() == 2 || TS_FlagRadius.easttower2.size() == 2) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);
									return;
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 2);
									return;
								}
							}

						}

						 if (TS_FlagRadius.easttower1.size() == 3 || TS_FlagRadius.easttower2.size() == 3) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);
									return;
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 3);
									return;
								}
							}

						}

						 if (TS_FlagRadius.easttower1.size() >= 4 || TS_FlagRadius.easttower2.size() >= 4) {

							if (TS_FlagRadius.ThisTeamIsCapturing(1, flag)) {

								for (Player online : TS_FlagRadius.easttower1) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
									playSound(online);
									MVPstats.setCaptures(online.getUniqueId(), MVPstats.getCaptures(online.getUniqueId()) + 4);
									return;
								}
							}

							if (TS_FlagRadius.ThisTeamIsCapturing(2, flag)) {

								for (Player online : TS_FlagRadius.easttower2) {
									online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: The East Tower"));
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
