package me.huntifi.castlesiege.playerCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class teamCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("teams")) {

			if (MapController.currentMapIs("HelmsDeep")) {

				p.sendMessage(ChatColor.GREEN + "Team " + ChatColor.DARK_GREEN + "Rohan: " + ChatColor.WHITE + PlayerTeam.rohanTeamSize());
				p.sendMessage(ChatColor.GREEN + "Team " + ChatColor.DARK_GRAY + "The Urukhai: " + ChatColor.WHITE + PlayerTeam.urukhaiTeamSize()); 
				return true;

			} else if(MapController.currentMapIs("Thunderstone")) {

				p.sendMessage(ChatColor.GREEN + "Team " + ChatColor.DARK_AQUA + "Cloudcrawlers: " + ChatColor.WHITE + PlayerTeam.cloudcrawlersTeamSize());
				p.sendMessage(ChatColor.GREEN + "Team " + ChatColor.GOLD + "Thunderstone Guards: " + ChatColor.WHITE + PlayerTeam.thunderstoneGuardsTeamSize()); 

				return true;

			}

		}
		return true; 

	}

}
