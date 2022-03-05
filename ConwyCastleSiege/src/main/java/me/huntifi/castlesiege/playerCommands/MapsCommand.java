package me.huntifi.castlesiege.playerCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.MapController;

public class MapsCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("maps")) {
				
				if (MapController.currentMapIs("HelmsDeep")) {

					p.sendMessage(ChatColor.DARK_GREEN + "Helm's Deep" + ChatColor.WHITE + " > " + ChatColor.GRAY + "Thunderstone" + ChatColor.WHITE + " > " + ChatColor.GRAY + "Restart");
			

					return true;

				} else if(MapController.currentMapIs("Thunderstone")) {

					p.sendMessage(ChatColor.GRAY + "Helm's Deep" + ChatColor.WHITE + " > " + ChatColor.DARK_GREEN + "Thunderstone" + ChatColor.WHITE + " > " + ChatColor.GRAY + "Restart");
				

					return true;

				}
			}
		}
		
		return true;
	}

}
