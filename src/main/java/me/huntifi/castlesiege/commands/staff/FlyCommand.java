package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.commands.donator.duels.DuelCommand;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
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
		}

		assert sender instanceof Player;
		Player p = (Player) sender;

		if (MapController.isSpectator(p.getUniqueId())) {
			Messenger.sendError("Spectators can already fly!", sender);
			return true;
		} else if (DuelCommand.isDueling(p)) {
			Messenger.sendError("You are dueling, you cannot fly!", p);
			return true;
		}

		p.setAllowFlight(!p.getAllowFlight());
		p.setFlying(p.getAllowFlight());
		p.setFlySpeed(0.2f);

		if (p.getAllowFlight()) {
			Messenger.sendSuccess("Flying has been enabled for you, enjoy your flight!", sender);
		} else {
			Messenger.sendSuccess("Flying has been disabled for you!", sender);
		}

		return true;
	}
}
