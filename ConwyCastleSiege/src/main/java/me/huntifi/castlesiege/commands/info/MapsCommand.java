package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Shows the player the maps in rotation
 */
public class MapsCommand implements CommandExecutor {

	/**
	 * Print the maps in rotation to the player
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		StringBuilder sb = new StringBuilder();

		// Add all maps to the string builder and color the active map green
		for (Map map : MapController.maps) {
			if (Objects.equals(MapController.getCurrentMap(), map)) {
				sb.append(ChatColor.DARK_GREEN);
			} else {
				sb.append(ChatColor.GRAY);
			}
			sb.append(map.name).append(ChatColor.WHITE).append(" > ");
		}
		sb.append(ChatColor.GRAY).append("Restart");

		// Print the message
		sender.sendMessage(sb.toString());
		return true;
	}
}
