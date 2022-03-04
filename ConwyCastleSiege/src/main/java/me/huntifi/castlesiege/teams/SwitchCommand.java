package me.huntifi.castlesiege.teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.huntifi.castlesiege.Helmsdeep.HelmsdeepEndMVP;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneEndMVP;
import me.huntifi.castlesiege.kits.EnderchestRefill;
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class SwitchCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("Switch")) {

			if(currentMaps.currentMapIs("HelmsDeep")) {

				if (LobbyPlayer.containsPlayer(p)) {

					if (PlayerTeam.playerIsInTeam(p, 2)) {

						if (PlayerTeam.rohanTeamSize() >= PlayerTeam.urukhaiTeamSize()) {

							PlayerTeam.switchPlayerTeam(p, 2);
							p.sendMessage("You switched to" + ChatColor.DARK_GRAY + " The Uruk-hai");

							Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17);
							p.teleport(loc);
							p.getInventory().clear();
							EnderchestRefill.refill(p);
							NametagsEvent.GiveNametag(p);
							
							ItemStack Wool = new ItemStack(Material.BLACK_WOOL);
							ItemMeta WoolMeta = Wool.getItemMeta();
							WoolMeta.setDisplayName(ChatColor.GREEN + "Woolhead");
							p.getInventory().setHelmet(Wool);

							HelmsdeepEndMVP.Urukhai.put(p, MVPstats.getScore(p.getUniqueId()));
							
						} else {

							p.sendMessage(ChatColor.RED + "Can't switch right now teams would be imbalanced.");

						}

					} else if (PlayerTeam.playerIsInTeam(p, 1)) {

						if (PlayerTeam.rohanTeamSize() < PlayerTeam.urukhaiTeamSize()) {
							
							PlayerTeam.switchPlayerTeam(p, 1);
							p.sendMessage("You switched to" + ChatColor.DARK_GREEN + " Rohan");

							Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1);
							p.teleport(loc);
							p.getInventory().clear();
							EnderchestRefill.refill(p);
							NametagsEvent.GiveNametag(p);
						
							ItemStack Wool = new ItemStack(Material.GREEN_WOOL);
							ItemMeta WoolMeta = Wool.getItemMeta();
							WoolMeta.setDisplayName(ChatColor.GREEN + "Woolhead");
							p.getInventory().setHelmet(Wool);
							
							HelmsdeepEndMVP.Rohan.put(p, MVPstats.getScore(p.getUniqueId()));

						} else {

							p.sendMessage(ChatColor.RED + "Can't switch right now teams would be imbalanced.");

						}
					}


				} else {
					
					p.sendMessage(ChatColor.RED + "You can't switch teams outside of your spawn (Woolmap-lobby).");
					
				}
				
				
			} else if(currentMaps.currentMapIs("Thunderstone")) {

				if (LobbyPlayer.containsPlayer(p)) {

					if (PlayerTeam.playerIsInTeam(p, 2)) {

						if (PlayerTeam.thunderstoneGuardsTeamSize() >= PlayerTeam.urukhaiTeamSize()) {

							PlayerTeam.switchPlayerTeam(p, 2);
							p.sendMessage("You switched to" + ChatColor.DARK_AQUA + " Cloudcrawlers");

							Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -191, 202, 159, -90, 1);
							p.teleport(loc);
							p.getInventory().clear();
							EnderchestRefill.refill(p);
							
							ItemStack Wool1 = new ItemStack(Material.CYAN_WOOL);
							ItemMeta Wool1Meta = Wool1.getItemMeta();
							Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
							p.getInventory().setHelmet(Wool1);
							
							NametagsEvent.GiveNametag(p);
							
							ThunderstoneEndMVP.Cloudcrawlers.put(p, MVPstats.getScore(p.getUniqueId()));


						} else {

							p.sendMessage(ChatColor.RED + "Can't switch right now teams would be imbalanced.");

						}

					} else if (PlayerTeam.playerIsInTeam(p, 1)) {

						if (PlayerTeam.rohanTeamSize() < PlayerTeam.urukhaiTeamSize()) {
							
							PlayerTeam.switchPlayerTeam(p, 1);
							p.sendMessage("You switched to" + ChatColor.GOLD + " Thunderstone Guards");

							Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -187, 202, 106, -90, 2);
							p.teleport(loc);
							p.getInventory().clear();
							EnderchestRefill.refill(p);
							NametagsEvent.GiveNametag(p);
						
							ItemStack Wool = new ItemStack(Material.ORANGE_WOOL);
							ItemMeta WoolMeta = Wool.getItemMeta();
							WoolMeta.setDisplayName(ChatColor.GREEN + "Woolhead");
							p.getInventory().setHelmet(Wool);
							
							ThunderstoneEndMVP.ThunderstoneGuards.put(p, MVPstats.getScore(p.getUniqueId()));

						} else {

							p.sendMessage(ChatColor.RED + "Can't switch right now teams would be imbalanced.");

						}
					}


				} else {
					
					p.sendMessage(ChatColor.RED + "You can't switch teams outside of your spawn (Woolmap-lobby).");
					
				}
			}
		} 

		return true;

	}
}