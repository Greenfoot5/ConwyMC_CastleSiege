package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Allows the player to swap teams
 */
public class SwitchCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
		if (cmd.getName().equals("ForceSwitch")) {
			return forceSwitch(args);
		}
		if (cmd.getName().equals("ToggleSwitching")) {
			if (args.length == 1) {
				MapController.disableSwitching = Boolean.getBoolean(args[0]);
			} else {
				MapController.disableSwitching = !MapController.disableSwitching;
			}
			Messenger.broadcastInfo("Switching is now " + (MapController.disableSwitching ? "disabled." : "enabled."));
			return true;
		}

		if (MapController.disableSwitching) {
			Messenger.sendError("Switching teams is currently disabled!", sender);
			return true;
		}

		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot join a team!");
			return true;
		} else if (sender instanceof Player && MapController.isSpectator(((Player) sender).getUniqueId())) {
			Messenger.sendError("Spectators don't have a team!", sender);
			return true;
		}

		assert sender instanceof Player;
		Player p = (Player) sender;
		Map map = MapController.getCurrentMap();

		// If the player is a donator
		if (p.hasPermission("castlesiege.esquire")) {
			// If the player hasn't specified a team, swap to the next one
			if (args.length == 0) {
				switchToNextTeam(map, p);
			} else {
				if (switchToSpecificTeam(map, p, args))
					return true;
			}

			// Spawn the player in their new lobby
			if (InCombat.isPlayerInLobby(p.getUniqueId())) {
				spawnPlayer(p, 0);
			} else if (p.hasPermission("castlesiege.baron")) {
				spawnPlayer(p, 1);
			} else {
				spawnPlayer(p, 2);
			}
			return true;
		}


		// Get the next team in the loop with fewer players
		Team oldTeam = MapController.getCurrentMap().getTeam(p.getUniqueId());
		Team smallestTeam = MapController.getCurrentMap().smallestTeam();
		if (oldTeam.getTeamSize() <= smallestTeam.getTeamSize()) {
			Messenger.sendError("Can't switch right now teams would be imbalanced. Donators avoid this restriction!", p);
			return true;
		}

		// Swap the player to the smallest team
		oldTeam.removePlayer(p.getUniqueId());
		smallestTeam.addPlayer(p.getUniqueId());

		if (!InCombat.isPlayerInLobby(p.getUniqueId())) {
			spawnPlayer(p, 2);
		} else {
			spawnPlayer(p, 0);
		}

	return true;
	}

	/**
	 * Spawn a player in their new lobby and award deaths
	 * @param p The player to spawn in their new lobby
	 * @param deaths The amount of deaths the player should receive
	 */
	private void spawnPlayer(Player p, int deaths) {
		Team team = MapController.getCurrentMap().getTeam(p.getUniqueId());

		//Check what type of switch it is (on battlefield or not?)
		if (deaths > 0 && MapController.isOngoing()) {
			//Set player health to 0 and teleport them to their new lobby
			p.setHealth(0);
			Messenger.sendInfo("You switched to " + team.primaryChatColor + team.name +
					ChatColor.DARK_AQUA + " (+" + deaths + " deaths)", p);
			UpdateStats.addDeaths(p.getUniqueId(), deaths - 1);

		} else if (deaths == 0 || (deaths > 0 || !MapController.isOngoing())){
			//Teleport to new lobby with full health
			p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
			Messenger.sendInfo("You switched to " + team.primaryChatColor + team.name, p);

		} else {
			//Teleport to new lobby with full health
			p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
			Messenger.sendInfo("You were forcefully switched to " + team.primaryChatColor + team.name, p);
		}

		// Remove any team specific kits
		Kit kit = Kit.equippedKits.get(p.getUniqueId());
		if (kit instanceof TeamKit && !Objects.equals(((TeamKit) kit).getTeamName(), team.name)) {
			Kit.equippedKits.remove(p.getUniqueId());
			Kit.equippedKits.put(p.getUniqueId(), new Swordsman());
			ActiveData.getData(p.getUniqueId()).setKit("swordsman");
		} else {
			Kit.equippedKits.get(p.getUniqueId()).setItems(p.getUniqueId());
		}

		p.teleport(team.lobby.spawnPoint);
		InCombat.playerDied(p.getUniqueId());
	}

	private void switchToNextTeam(Map map, Player p) {
		// Switch to the next team
		int index = 0;
		for (int i = 0; i < map.teams.length; i++) {
			if (map.teams[i].hasPlayer(p.getUniqueId())) {
				index = i;
			}
		}

		// If we're looping
		index++;
		if (index == map.teams.length) {
			map.teams[index - 1].removePlayer(p.getUniqueId());
			map.teams[0].addPlayer(p.getUniqueId());
		} else {
			map.teams[index - 1].removePlayer(p.getUniqueId());
			map.teams[index].addPlayer(p.getUniqueId());
		}
	}

	private boolean forceSwitch(@NotNull String[] args) {
		Player p = Bukkit.getPlayer(args[0]);

		if (p != null && MapController.isSpectator((p).getUniqueId())) {
			Messenger.sendError("Spectators don't have a team! Remove them from a spectator first!", p);
			return true;
		}

		Map map = MapController.getCurrentMap();
		// If the player hasn't specified a team, swap to the next one
		if (args.length == 1) {
			switchToNextTeam(map, p);
		} else {
			if (switchToSpecificTeam(map, p, Arrays.copyOfRange(args, 1, args.length)))
				return true;
		}

		// Spawn the player in their new lobby
		assert p != null;
		spawnPlayer(p, -1);
		return true;
	}

	private boolean switchToSpecificTeam(Map map, Player p, String[] args) {
		// The player has specified a team, and we should swap to that if it's a valid team
		Team team = map.getTeam(String.join(" ", args));
		if (team != null) {
			map.getTeam(p.getUniqueId()).removePlayer(p.getUniqueId());
			team.addPlayer(p.getUniqueId());
		} else {
			Messenger.sendError(ChatColor.DARK_AQUA + Arrays.toString(args) + ChatColor.RED + " isn't a valid team name!", p);
			return true;
		}
		return false;
	}
}