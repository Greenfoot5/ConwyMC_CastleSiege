package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Sends a message to all teammates
 */
public class TeamChat implements CommandExecutor {

	/**
	 * Send the provided arguments as a chat message to all team members
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Prevent using from console
		if (!(sender instanceof Player)) {
			sender.sendMessage("Console can't say things in team-chat.");
			return true;
		}

		// The command requires an actual message
		if (args.length == 0) {
			return false;
		}

		// Put the message together
		Player p = (Player) sender;
		Team t = MapController.getCurrentMap().getTeam(p.getUniqueId());
		String m = p.getDisplayName() + ChatColor.DARK_AQUA + " TEAM: " + ChatColor.GRAY + String.join(" ", args);

		// Send the message to all team members
		for (Player q : Bukkit.getOnlinePlayers()) {
			if (t.hasPlayer(q.getUniqueId())) {
				q.sendMessage(m);
			}
		}



		return true;



	}

}
