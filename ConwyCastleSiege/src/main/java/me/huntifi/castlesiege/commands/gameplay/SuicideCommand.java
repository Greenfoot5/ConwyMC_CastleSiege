package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Performs the suicide command, killing the player that uses it
 */
public class SuicideCommand implements CommandExecutor {

	/**
	 * Kills the player and adds 2 deaths
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label,
							 @NotNull String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot die!");
			return true;
		}else if (sender instanceof Player && MapController.isSpectator(((Player) sender).getUniqueId())) {
			Messenger.sendError("Spectators cannot die!", sender);
			return true;
		}

		assert sender instanceof Player;
		Player p = (Player) sender;
		if (p.getHealth() != 0) {
			p.setHealth(0);
		}
		if (MapController.isOngoing()) {
			Messenger.sendInfo("You have committed suicide " + ChatColor.DARK_AQUA + "(+2 deaths)", p);
			UpdateStats.addDeaths(p.getUniqueId(), 1); // Note: 1 death added on player respawn
		} else {
			Messenger.sendInfo("You have committed suicide.", p);
		}
		return true;
	}
}
