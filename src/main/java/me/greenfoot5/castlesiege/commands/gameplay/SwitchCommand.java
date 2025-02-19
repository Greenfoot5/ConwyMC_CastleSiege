package me.greenfoot5.castlesiege.commands.gameplay;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.maps.Map;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.maps.objects.Flag;
import me.greenfoot5.castlesiege.maps.objects.Gate;
import me.greenfoot5.castlesiege.maps.objects.Ram;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static me.greenfoot5.castlesiege.advancements.levels.LevelAdvancements.SWITCH_LEVEL;

/**
 * Allows the player to swap teams
 */
public class SwitchCommand implements CommandExecutor {

	/**
	 * Switch a player's team
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player player && CSActiveData.getData(player.getUniqueId()) != null && CSActiveData.getData(player.getUniqueId()).getLevel() < SWITCH_LEVEL) {
			Messenger.sendError("You must be at least <green>level " + SWITCH_LEVEL + "</green> to use this command!", sender);
			return true;
		}

		// Force switch command was used
		if (cmd.getName().equals("ForceSwitch"))
			forceSwitch(sender, args);

		// Regular switch command was used
		if (cmd.getName().equals("Switch"))
			switchTeam(sender, args);

		return true;
	}

	/**
	 * Forcefully switch a player
	 * @param sender Source of the command
	 * @param args Passed command arguments
	 */
	private void forceSwitch(CommandSender sender, String[] args) {
		if (args.length == 0) {
			Messenger.sendError("Use: /forceswitch <player> [team name]", sender);
			return;
		}

		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
			Messenger.sendError("Could not find player: <red>" + args[0], sender);
			return;
		} else if (!TeamController.isPlaying(p)) {
			Messenger.sendError("You must be on a team to swap!", sender);
			return;
		}

		stopCapping(p);
		stopRamming(p);

		// If the player hasn't specified a team, swap to the next one
		if (args.length == 1) {
			switchToNextTeam(p);
		} else {
			if (switchToSpecificTeam(p, Arrays.copyOfRange(args, 1, args.length)))
				return;
		}

		// Spawn the player in their new lobby
		spawnPlayer(p, -1);
	}
	
	private void switchTeam(CommandSender sender, String[] args) {
		if (TeamController.disableSwitching) {
			Messenger.sendError("Switching teams is currently disabled!", sender);
			return;
		} else if (!(sender instanceof Player)) {
			Messenger.sendError("Console cannot join a team!", sender);
			return;
		} else if (!TeamController.isPlaying((Player) sender)) {
			Messenger.sendError("Must be on a team to switch!", sender);
			return;
		}

		Player p = (Player) sender;
		stopCapping(p);
		stopRamming(p);

		// If the player is a donator
		if (p.hasPermission("conwymc.esquire")) {
			// If the player hasn't specified a team, swap to the next one
			if (args.length == 0) {
				switchToNextTeam(p);
			} else {
				if (switchToSpecificTeam(p, args))
					return;
			}

			// Spawn the player in their new lobby
			if (InCombat.isPlayerInLobby(p.getUniqueId())) {
				spawnPlayer(p, 0);
		    } else if (p.hasPermission("conwymc.baron")) {
				spawnPlayer(p, 1);
			} else {
				spawnPlayer(p, 2);
			}
			return;
		}

		// Get the next team in the loop with fewer players
		Team oldTeam = TeamController.getTeam(p.getUniqueId());
		Team smallestTeam = MapController.getCurrentMap().smallestTeam();
		if (oldTeam.getTeamSize() <= smallestTeam.getTeamSize()) {
			Messenger.sendError("Can't switch right now teams would be imbalanced. Donators avoid this restriction!", p);
			return;
		}

		TeamController.joinSmallestTeam(p.getUniqueId(), MapController.getCurrentMap());

		if (!InCombat.isPlayerInLobby(p.getUniqueId())) {
			spawnPlayer(p, 2);
		} else {
			spawnPlayer(p, 0);
		}
	}

	/**
	 * Spawn a player in their new lobby and award deaths
	 * @param p The player to spawn in their new lobby
	 * @param deaths The amount of deaths the player should receive
	 */
	private void spawnPlayer(Player p, int deaths) {
		Team team = TeamController.getTeam(p.getUniqueId());

		if (deaths > 0 && MapController.isOngoing()) {
			// Regular switch on the battlefield during a game
			Messenger.sendInfo(Component.text("You switched to ")
							.append(team.getDisplayName())
							.append(Component.text(" (+" + deaths + " deaths)", NamedTextColor.DARK_AQUA)),
					p);
			UpdateStats.addDeaths(p.getUniqueId(), deaths - 1);

		} else if (deaths == 0 || !MapController.isOngoing()){
			// Regular switch outside the battlefield or not during a game
			Messenger.sendInfo(Component.text("You switched to ").append(team.getDisplayName()), p);
			InCombat.playerDied(p.getUniqueId());

		} else {
			// Forced switch
			Messenger.sendInfo(Component.text("You forcefully switched to ").append(team.getDisplayName()), p);
			InCombat.playerDied(p.getUniqueId());
		}

		// Respawn the player
		Bukkit.getScheduler().runTask(Main.plugin, () -> p.setHealth(0));
		team.grantLives(1);
	}

	/**
	 * Switch the player to the next team
	 * @param p The player to switch
	 */
	private void switchToNextTeam(Player p) {
		Team[] teams = MapController.getCurrentMap().teams;

		// Get the index of the current team
		int index = 0;
		for (int i = 0; i < teams.length; i++) {
			if (teams[i].hasPlayer(p.getUniqueId())) {
				index = i;
			}
		}

		// Switch to the next team
		TeamController.joinTeam(p.getUniqueId(), teams[(index + 1) % teams.length]);
	}

	/**
	 * Switch the player to a specific team
	 * @param p The player to switch
	 * @param args The team to switch to
	 * @return Whether the switch was unsuccessful
	 */
	private boolean switchToSpecificTeam(Player p, String[] args) {
		Map map = MapController.getCurrentMap();

		// The player has specified a team, and we should swap to that if it's a valid team
		Team team = map.getTeam(String.join(" ", args));
		if (team != null) {
			TeamController.joinTeam(p.getUniqueId(), team);
			return false;
		}

		// The team is invalid
		Messenger.sendError("<aqua>" + String.join(" ", args) + "<red> isn't a valid team name!", p);
		return true;
	}

	/**
	 * Remove the player from all capping zones
	 * @param p The player
	 */
	private void stopCapping(Player p) {
		for (Flag flag : MapController.getCurrentMap().flags) {
			flag.playerExit(p);
		}
	}

	/**
	 * Remove the player from all rams
	 * @param p The player
	 */
	private void stopRamming(Player p) {
		for (Gate gate : MapController.getCurrentMap().gates) {
			Ram ram = gate.getRam();
			if (ram != null)
				ram.playerExit(p);
		}
	}
}