package me.huntifi.castlesiege.commands.staffCommands;

import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Allows staff players to fly
 */
public class FlyCommand implements CommandExecutor {

	/**
	 * Toggles flight for the player if they are staff
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player p = (Player) sender;
		String staffRank = ActiveData.getData(p.getUniqueId()).getStaffRank();

		if ("AdminModeratorDeveloperChatMod+".contains(staffRank)) {
			p.setAllowFlight(!p.getAllowFlight());
			p.setFlying(p.getAllowFlight());

			if (p.getAllowFlight()) {
				p.sendMessage(ChatColor.DARK_GREEN + " Flying has been enabled for you, enjoy your flight!");
			} else {
				p.sendMessage(ChatColor.DARK_GREEN + " Flying has been disabled for you!");
			}
		} else {
			p.sendMessage(ChatColor.DARK_RED + "You are not permitted to use this command!");
		}

		return true;
	}
}
