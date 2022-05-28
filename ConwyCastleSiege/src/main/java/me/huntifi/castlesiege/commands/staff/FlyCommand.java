package me.huntifi.castlesiege.commands.staff;

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
 * Allows staff players to fly
 */
public class FlyCommand implements CommandExecutor {

	/**
	 * Toggles flight for the player
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			Messenger.sendError("Console cannot fly!", sender);
			return true;
		} else if (sender instanceof Player && MapController.isSpectator(((Player) sender).getUniqueId())) {
			Messenger.sendError("Spectators can already fly!", sender);
		}

		assert sender instanceof Player;
		Player p = (Player) sender;
		p.setAllowFlight(!p.getAllowFlight());
		p.setFlying(p.getAllowFlight());

		if (p.getAllowFlight()) {
			p.sendMessage(ChatColor.DARK_GREEN + " Flying has been enabled for you, enjoy your flight!");
		} else {
			p.sendMessage(ChatColor.DARK_GREEN + " Flying has been disabled for you!");
		}

		return true;
	}
}
