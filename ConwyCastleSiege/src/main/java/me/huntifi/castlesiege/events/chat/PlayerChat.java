package me.huntifi.castlesiege.events.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.huntifi.castlesiege.database.AsyncGetters;
import me.huntifi.castlesiege.database.AsyncGetters.BooleanCallback;
import me.huntifi.castlesiege.database.SQLGetter;
import me.huntifi.castlesiege.commands.togglerankCommand;

/**
 * Makes staff's text chat colour white
 */
public class PlayerChat implements Listener {

	/**
	 * Checks if the player is a staff member, and sets chat colour accordingly
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		BooleanCallback callback = isStaff -> {
			if (!togglerankCommand.rankers.contains(p) || isStaff) {
				e.setFormat("%s:" + ChatColor.WHITE + " %s");
			} else {
				e.setFormat("%s:" + ChatColor.GRAY + " %s");
			}
		};

		// NOTE - ChatMod is included in ChatMod+
		AsyncGetters.performLookupRank(callback, "Admin, Moderator, Developer, ChatMod+".contains(SQLGetter.getStaffRank(p.getUniqueId())), p, true);
	}
}


