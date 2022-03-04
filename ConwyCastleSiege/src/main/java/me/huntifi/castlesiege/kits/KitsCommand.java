package me.huntifi.castlesiege.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLstats;
import me.huntifi.castlesiege.Database.StatsStrings;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class KitsCommand implements CommandExecutor {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("Kit")) {

			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

				if (LobbyPlayer.containsPlayer(p)) {

					if (args[0].equalsIgnoreCase("Swordsman")) {

						SQLstats.setKit(p.getUniqueId(), "Swordsman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 

					if (args[0].equalsIgnoreCase("Archer")) {

						SQLstats.setKit(p.getUniqueId(), "Archer");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 


					if (args[0].equalsIgnoreCase("Spearman")) {

						SQLstats.setKit(p.getUniqueId(), "Spearman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 


					if (args[0].equalsIgnoreCase("Skirmisher")) {

						SQLstats.setKit(p.getUniqueId(), "Skirmisher");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						 
					} 


					if (args[0].equalsIgnoreCase("Shieldman")) {

						SQLstats.setKit(p.getUniqueId(), "Shieldman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 


					if (args[0].equalsIgnoreCase("FireArcher")) {


						SQLstats.setKit(p.getUniqueId(), "FireArcher");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						 
					} 

				} else {

					p.sendMessage(ChatColor.RED + "You can't use this command on the battlefield!");
				}
			});

			return true;

		}
		return true; 

	}

}
