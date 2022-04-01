package me.huntifi.castlesiege.commands.staffCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.Thunderstone.ThunderstoneTimer;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.maps.MapController;

public class NextMapCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("nextmap")) {

			if(sender != null) {

				if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") || StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") || p.isOp() || StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {

					Bukkit.getServer()
					.broadcastMessage(p.getDisplayName() + ChatColor.YELLOW + " Has skipped to the next map!");

					MapController.nextMap();
				}
			}
		}
		return true;
	}
}
