package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.commands.chat.TeamChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.huntifi.castlesiege.database.SQLGetter;

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

		// Set message colour to white or gray
		String staffRank = SQLGetter.getStaffRank(p.getUniqueId());
		if (staffRank != null && !staffRank.equalsIgnoreCase("None") &&
				!staffRank.equalsIgnoreCase("")) {
			e.setMessage(ChatColor.WHITE + e.getMessage());
		} else {
			e.setMessage(ChatColor.GRAY + e.getMessage());
		}

		// Send message in team-chat
		if (TeamChat.isTeamChatter(p.getUniqueId())) {
			TeamChat.sendTeamMessage(e.getPlayer(), e.getMessage());
			e.setCancelled(true);
			return;
		}

		// Send regular message
		e.setFormat("%s: %s");
	}
}


