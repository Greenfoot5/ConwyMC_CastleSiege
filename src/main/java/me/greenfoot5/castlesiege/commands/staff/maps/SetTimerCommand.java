package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TimerState;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Skips to the next map
 */
public class SetTimerCommand implements CommandExecutor {

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
		if (MapController.timer.state == TimerState.ENDED) {
			Messenger.sendError("You can't change the timer once it's ended.", sender);
			return true;
		}

		if (args.length == 0) {
			Messenger.sendError("Please at least specify minutes!", sender);
		} else if (args.length == 1) {
			MapController.timer.minutes = Integer.valueOf(args[0]);
		} else if (args.length == 2) {
			MapController.timer.minutes = Integer.valueOf(args[0]);
			MapController.timer.seconds = Integer.valueOf(args[1]);
		} else {
			Messenger.sendError("That's too many arguments!", sender);
		}

		return true;
	}
}
