package me.huntifi.castlesiege.commands;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.join.stats.StatsChanging;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Allows the player to swap teams
 */
public class SwitchCommand implements CommandExecutor {

	private static final List<String> SINGLE_DEATH_RANKS = Arrays.asList("baron", "duke", "king", "high king");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		Map map = MapController.getCurrentMap();

		// If the player is a donator
		if (!Objects.equals(StatsChanging.getRank(p.getUniqueId()), "None")) {
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

			// Kill the player
			p.setHealth(0);
			Team team = map.getTeam(p.getUniqueId());
			if (SINGLE_DEATH_RANKS.contains(StatsChanging.getRank(p.getUniqueId()))) {
				p.sendMessage("You switched to " + team.primaryChatColor + team.name + ChatColor.DARK_AQUA + " (+1 death)");
				StatsChanging.addDeaths(p.getUniqueId(), 1);
			} else {
				p.sendMessage("You switched to " + team.primaryChatColor + team.name + ChatColor.DARK_AQUA + " (+2 deaths)");
				StatsChanging.addDeaths(p.getUniqueId(), 2);
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
			// The player should die when switching
			p.setHealth(0);
			p.sendMessage("You switched to " + smallestTeam.primaryChatColor + smallestTeam.name + " (+2 deaths)");
			//StatsChanging.addDeaths(p.getUniqueId(), 2);
		} else {
			// Heal the player
			p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
			p.sendMessage("You switched to " + smallestTeam.primaryChatColor + smallestTeam.name + "");
			// Teleport the player
			p.teleport(smallestTeam.lobby.spawnPoint);
		}

	return true;
	}
}