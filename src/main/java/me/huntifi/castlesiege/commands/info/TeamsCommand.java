package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player the teams of the current map and their player count
 */
public class TeamsCommand implements CommandExecutor {

	/**
	 * Print the name and size of each team on the current map
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Team[] teams = MapController.getCurrentMap().teams;

		for (Team t : teams) {
			sender.sendMessage(String.format("<green>Team %s%s: %s%d",
					ChatColor.GREEN, t.primaryChatColor, t.name, ChatColor.WHITE, t.getTeamSize()));
		}

		return true;
	}
}
