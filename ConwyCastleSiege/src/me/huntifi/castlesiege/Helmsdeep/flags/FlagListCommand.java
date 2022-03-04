package me.huntifi.castlesiege.Helmsdeep.flags;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class FlagListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("CheckFlagList")) {

				if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") || StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") || p.isOp() || StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {
					p.sendMessage(ChatColor.GOLD + "Shows the capture-arraylists for each flag and how many players it contains. (Helmsdeep Only)");
					p.sendMessage(" ");
					p.sendMessage("Horn Flag: " + ChatColor.DARK_GREEN + FlagRadius.Horn2.size() + " " + ChatColor.DARK_GRAY + FlagRadius.Horn1.size());
					p.sendMessage("Caves Flag: " + ChatColor.DARK_GREEN + FlagRadius.Caves2.size() + " " + ChatColor.DARK_GRAY + FlagRadius.Caves1.size());
					p.sendMessage("Courtyard Flag: " + ChatColor.DARK_GREEN + FlagRadius.Courtyard2.size() + " " + ChatColor.DARK_GRAY + FlagRadius.Courtyard1.size());
					p.sendMessage("Supply Camp Flag: " + ChatColor.DARK_GREEN + FlagRadius.SupplyCamp2.size() + " " + ChatColor.DARK_GRAY + FlagRadius.SupplyCamp1.size());
					p.sendMessage("Great Hall Flag: " + ChatColor.DARK_GREEN + FlagRadius.GreatHalls2.size() + " " + ChatColor.DARK_GRAY + FlagRadius.GreatHalls1.size());
					p.sendMessage("Main Gate Flag: " + ChatColor.DARK_GREEN + FlagRadius.MainGate2.size() + " " + ChatColor.DARK_GRAY + FlagRadius.MainGate1.size());

				}

			}
		}


		return true;
	}
}