package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
		Component c = Component.text("Team Counts: ");

		for (Team t : teams) {
			c = c.append(Component.newline())
					.append(Component.text("Team ", NamedTextColor.GREEN))
					.append(t.getDisplayName())
					.append(Component.text(": ", NamedTextColor.GREEN))
					.append(Component.text(t.getTeamSize(), NamedTextColor.WHITE));
		}

		Messenger.sendInfo(c, sender);
		return true;
	}
}
