package me.huntifi.castlesiege.commands;

import me.huntifi.castlesiege.database.UpdateStats;
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
		}

		Player p = (Player) sender;
		p.setHealth(0);
		p.sendMessage("You have committed suicide (+2 deaths)");
		UpdateStats.addDeaths(p.getUniqueId(), 1); // Note: 1 death added on player respawn
		return true;
	}
}
