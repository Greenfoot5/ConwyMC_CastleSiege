package me.huntifi.castlesiege.playerCommands.staffCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.Helmsdeep.HelmsdeepTimer;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneTimer;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.maps.currentMaps;

public class NextMapCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("nextmap")) {

			if(sender instanceof Player) {

				if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") || StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") || p.isOp() || StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {

					Bukkit.getServer()
					.broadcastMessage(p.getDisplayName() + ChatColor.YELLOW + " Has skipped to the next map!");

					if (currentMaps.currentMapIs("HelmsDeep")) {

						HelmsdeepTimer.Seconds = 0;
						HelmsdeepTimer.Minutes = 0;


					} else if (currentMaps.currentMapIs("Thunderstone")) {

						ThunderstoneTimer.Seconds = 0;
						ThunderstoneTimer.Minutes = 0;


					}

				}

			}
		}
		return true;

	}

}
