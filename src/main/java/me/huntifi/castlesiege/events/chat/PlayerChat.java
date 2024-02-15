package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.commands.donator.duels.AcceptDuel;
import me.huntifi.castlesiege.commands.donator.duels.DuelCmd;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener {

	private static final ArrayList<String> owners = new ArrayList<>();

	public PlayerChat() {
		owners.add("Huntifi");
		owners.add("Greenfoot5");
	}

	/**
	 * Set message color to white for staff and gray otherwise
	 * Send the message in a specific mode if applicable
	 *
	 * @param e The event called when a player sends a message
	 */
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String message = e.getMessage();

		// Check if the player is muted
		if (Mute.isMuted(p.getUniqueId())) {
			e.setCancelled(true);
			return;
		}

		// Send message in team-chat or staff-chat
		if (TeamChat.isTeamChatter(p.getUniqueId())) {
			TeamChat.sendMessage(e.getPlayer(), e.getMessage());
			e.setCancelled(true);
			return;
		} else if (StaffChat.isStaffChatter(p.getUniqueId())) {
			StaffChat.sendMessage(e.getPlayer(), e.getMessage());
			e.setCancelled(true);
			return;
		}


		// Set message colour to white or gray and send as regular message
		ChatColor color = p.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(p)
				? ChatColor.WHITE : ChatColor.GRAY;


		if (owners.contains(p.getName()) && !ToggleRankCommand.showDonator.contains(p)) {
			switch (p.getName()) {
				case "Huntifi":
					color = ChatColor.DARK_PURPLE;
					break;
				case "Greenfoot5":
					color = ChatColor.DARK_GREEN;
					break;
			}
		}

		//Allow to tag players in chat
		for (Player tagged : Bukkit.getOnlinePlayers()) {
			if (message.contains("@" + tagged.getName())) {
				playTagSound(tagged);
			}
		}

		// Set the message so that the bot still receives input.
		// Also removed all recipients as that would send the message twice to everyone
		e.setMessage(message);
		e.setFormat("%s: %s");
		sendTotalMessage(p, color, message);
		for (Player everyone : Bukkit.getOnlinePlayers()) {
			e.getRecipients().remove(everyone);
		}


	}

	private void playTagSound(Player player) {
		Location location = player.getLocation();

		Sound effect = Sound.BLOCK_NOTE_BLOCK_BELL;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		player.playSound(location, effect, volume, pitch);
	}


	/** This handles level colours for all players.
	 * 	 * @param p the player to send the message from globally.
	 * @param chatColor the colour that the player's chat should be in.
	 * @param message The message that comes after the name.
	 */
	public void sendTotalMessage(Player p, ChatColor chatColor, String message) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {

			PlayerData data = ActiveData.getData(p.getUniqueId());
			if (data == null) {
				return;
			}
			int playerLevel = data.getLevel();

			// Get the player's wanted rank
			String rank;
			if (p.hasPermission("castlesiege.builder") && !ToggleRankCommand.showDonator.contains(p)) {
				rank = NameTag.convertRank(data.getStaffRank());
			} else {
				rank = NameTag.convertRank(data.getRank());
			}

			for (Player everyone : Bukkit.getOnlinePlayers()) {
				String everyoneMessage = "";
				PlayerData everyoneData = ActiveData.getData(everyone.getUniqueId());
				if (everyoneData == null) {
					return;
				}
				int everyoneLevel = everyoneData.getLevel();

                //Check if the player is not in some other non-game world (currently just the duelsmap)
				if (!DuelCmd.challenging.containsKey(p) && !DuelCmd.challenging.containsValue(p)) {

					if (playerLevel + 10 < everyoneLevel) {
						everyoneMessage = ChatColor.DARK_GREEN + String.valueOf(playerLevel) + " " + rank + NameTag.color(p) + p.getName() + ": " + chatColor + message;
					} else if (playerLevel + 5 < everyoneLevel && playerLevel + 10 >= everyoneLevel) {
						everyoneMessage = ChatColor.GREEN + String.valueOf(playerLevel) + " " + rank + NameTag.color(p) + p.getName() + ": " + chatColor + message;
					} else if (playerLevel - 5 <= everyoneLevel && playerLevel + 5 >= everyoneLevel) {
						everyoneMessage = ChatColor.YELLOW + String.valueOf(playerLevel) + " " + rank + NameTag.color(p) + p.getName() + ": " + chatColor + message;
					} else if (playerLevel - 5 > everyoneLevel && playerLevel - 10 <= everyoneLevel) {
						everyoneMessage = ChatColor.RED + String.valueOf(playerLevel) + " " + rank + NameTag.color(p) + p.getName() + ": " + chatColor + message;
					} else if (playerLevel - 10 > everyoneLevel) {
						everyoneMessage = ChatColor.DARK_RED + String.valueOf(playerLevel) + " " + rank + NameTag.color(p) + p.getName() + ": " + chatColor + message;
						//during duels (beneath)
					}

					//If they are then their display should be this:
				} else {
					everyoneMessage = ChatColor.BLUE + "⚔ " + rank + NameTag.color(p) + p.getName() + ": " + chatColor + message;
				}

				everyone.sendMessage(everyoneMessage);
			}

		});
	}
}


