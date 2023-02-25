package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
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
			color = ChatColor.GREEN;
		}

		//Allow to tag players in chat
		for (Player tagged : Bukkit.getOnlinePlayers()) {
			if (message.contains("@" + tagged.getName())) {
				playTagSound(tagged);
			}
		}

		e.setMessage(color + message);
		e.setFormat("%s: %s");
	}

	private void playTagSound(Player player) {
		Location location = player.getLocation();

		Sound effect = Sound.BLOCK_NOTE_BLOCK_BELL;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		player.playSound(location, effect, volume, pitch);
	}
}


