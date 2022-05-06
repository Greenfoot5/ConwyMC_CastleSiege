package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

/**
 * Allows the player to swap teams
 */
public class SwitchCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot join a team!");
			return true;
		}

		Player p = (Player) sender;
		Map map = MapController.getCurrentMap();

		// If the player is a donator
		if (p.hasPermission("castlesiege.esquire")) {
			// If the player hasn't specified a team, swap to the next one
			if (args.length == 0) {
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
			} else {
				// The player has specified a team, and we should swap to that if it's a valid team
				Team team = map.getTeam(String.join(" ", args));
				if (team != null) {
					map.getTeam(p.getUniqueId()).removePlayer(p.getUniqueId());
					team.addPlayer(p.getUniqueId());
				} else {
					p.sendMessage(ChatColor.DARK_AQUA + Arrays.toString(args) + ChatColor.RED + " isn't a valid team name!");
					return true;
				}
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
		if (oldTeam == smallestTeam) {
			p.sendMessage(ChatColor.RED + "Can't switch right now teams would be imbalanced. Donators avoid this restriction!");
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
		if (deaths > 0) {
			p.setHealth(0);
			p.sendMessage("You switched to " + team.primaryChatColor + team.name +
					ChatColor.DARK_AQUA + " (+" + deaths + " deaths)");
			UpdateStats.addDeaths(p.getUniqueId(), deaths - 1);
		} else {
			p.teleport(team.lobby.spawnPoint);
			p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
			p.sendMessage("You switched to " + team.primaryChatColor + team.name);
			Kit.equippedKits.get(p.getUniqueId()).setItems(p.getUniqueId());
		}
	}
}