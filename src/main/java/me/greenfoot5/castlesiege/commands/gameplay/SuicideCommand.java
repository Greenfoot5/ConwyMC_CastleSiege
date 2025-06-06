package me.greenfoot5.castlesiege.commands.gameplay;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.commands.donator.DuelCommand;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			if (canSuicide(sender) && !DuelCommand.isDueling(((Player) sender).getUniqueId())) {
				Player p = (Player) sender;
				if (p.getHealth() != 0)
					Bukkit.getScheduler().runTask(Main.plugin, () -> p.setHealth(0));

				if (MapController.isOngoing() && !InCombat.isPlayerInLobby(p.getUniqueId())) {
					Messenger.sendInfo("You have committed suicide <dark_aqua>(+2 deaths)", p);
					UpdateStats.addDeaths(p.getUniqueId(), 2);
				} else
					Messenger.sendInfo("You have committed suicide.", p);
			}
		});

		return true;
	}

	/**
	 * Check if the command's source can use this command.
	 * @param sender Source of the command
	 * @return Whether the sender can commit suicide
	 */
	private boolean canSuicide(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Messenger.sendError("Console cannot die!", sender);
			return false;
		}

		if (TeamController.isSpectating((Player) sender)) {
			Messenger.sendError("Spectators cannot die!", sender);
			return false;
		}

		return true;
	}
}
