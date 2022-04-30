package me.huntifi.castlesiege.commands.staffCommands;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class NextMapCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {


		if(sender instanceof Player) {
			Player p = (Player) sender;

			if (ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Moderator") ||
					ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Admin") ||
					ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Developer") ||
					p.isOp() ) {

				Bukkit.getServer().broadcastMessage(
						p.getDisplayName() + ChatColor.YELLOW + " has skipped to the next map!");

				MapController.endMap();
			}
		} else if (sender instanceof ConsoleCommandSender) {
			Bukkit.getServer().broadcastMessage(
					ChatColor.DARK_AQUA + "Console" + ChatColor.YELLOW + " has skipped to the next map!");

			MapController.endMap();
		}
		return true;
	}
}
