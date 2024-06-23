package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.SignKit;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
		} else if (!MapController.getPlayers().contains(p.getUniqueId())) {
			Messenger.sendError("You must be on a team to swap!", sender);
			return;
		}

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
		if (MapController.disableSwitching) {
			Messenger.sendError("Switching teams is currently disabled!", sender);
			return;
		} else if (!(sender instanceof Player)) {
			Messenger.sendError("Console cannot join a team!", sender);
			return;
		} else if (!MapController.getPlayers().contains(((Player) sender).getUniqueId())) {
			Messenger.sendError("Must be on a team to switch!", sender);
			return;
		}

		Player p = (Player) sender;

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

		// Swap the player to the smallest team
		oldTeam.removePlayer(p.getUniqueId());
		smallestTeam.addPlayer(p.getUniqueId());

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

		// Remove any team specific kits
		Kit kit = Kit.equippedKits.get(p.getUniqueId());
		if (kit instanceof SignKit) {
			Kit.equippedKits.put(p.getUniqueId(), new Swordsman());
			CSActiveData.getData(p.getUniqueId()).setKit("swordsman");
		}

		// Respawn the player
		Bukkit.getScheduler().runTask(Main.plugin, () -> p.setHealth(0));
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
		teams[index].removePlayer(p.getUniqueId());
		teams[(index + 1) % teams.length].addPlayer(p.getUniqueId());
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
			TeamController.getTeam(p.getUniqueId()).removePlayer(p.getUniqueId());
			team.addPlayer(p.getUniqueId());
			return false;
		}

		// The team is invalid
		Messenger.sendError("<aqua>" + String.join(" ", args) + "<red> isn't a valid team name!", p);
		return true;
	}
}