package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Skips to the next map
 */
public class NextMapCommand implements CommandExecutor {

	/**
	 * Skip to the next map
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if (!MapController.getCurrentMap().hasMapEnded()) {
				Messenger.broadcastWarning(CSNameTag.mmUsername(p) + " has skipped to the next map!");
				MapController.endMap();
			} else {
				Messenger.sendError("Map has already ended!", p);
			}

		} else if (sender instanceof ConsoleCommandSender) {
			if (!MapController.getCurrentMap().hasMapEnded()) {
				Messenger.broadcastWarning("<dark_aqua>Console</dark_aqua>" + " has skipped to the next map!");
				MapController.endMap();
			} else {
				Messenger.sendError("Map has already ended!", sender);
			}
		}

		return true;
	}
}
