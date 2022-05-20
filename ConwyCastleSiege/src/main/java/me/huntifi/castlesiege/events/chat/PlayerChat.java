package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener {

	/**
	 * Set message color to white for staff and gray otherwise
	 * Send the message in a specific mode if applicable
	 * @param e The event called when a player sends a message
	 */
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

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
		ChatColor color = p.hasPermission("castlesiege.chatmod") ? ChatColor.WHITE : ChatColor.GRAY;
		e.setMessage(color + e.getMessage());
		e.setFormat("%s: %s");
	}
}


