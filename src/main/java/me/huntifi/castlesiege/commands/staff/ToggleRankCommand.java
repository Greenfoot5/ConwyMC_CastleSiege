package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Configures a player's shown rank
 */
public class ToggleRankCommand implements CommandExecutor {
	
	public static final ArrayList<Player> showDonator = new ArrayList<>();

	/**
	 * Switch between staff and donator rank
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			Messenger.sendError("Console cannot toggle their rank!", sender);
			return true;
		}

		Player p = (Player) sender;
		if (showDonator.contains(p)) {
			showDonator.remove(p);
			Messenger.sendWarning("Staff rank toggled on", p);
		} else {
			showDonator.add(p);
			Messenger.sendWarning("Staff rank toggled off", p);
		}

		NameTag.give(p);
		return true;

	}
}
