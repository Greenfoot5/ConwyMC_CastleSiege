package me.huntifi.castlesiege.Thunderstone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;
//import me.huntifi.castlesiege.teams.PlayerTeam;


public class ThunderstoneJoin implements Listener {

	public Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		if (MapController.currentMapIs("Thunderstone")) {

			/*if (PlayerTeam.thunderstoneGuardsTeamSize() < PlayerTeam.cloudcrawlersTeamSize()) {

					PlayerTeam.setPlayerTeam(p, 2);
					Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -187, 202, 106, -90, 2);
					p.teleport(loc);
					p.getInventory().clear();
					p.sendMessage("You joined" + ChatColor.GOLD + " Thunderstone Guard");
					p.sendMessage(ChatColor.YELLOW + "");
					p.sendMessage(ChatColor.YELLOW + "*** JOINING THE BATTLEFIELD ***");
					p.sendMessage(ChatColor.YELLOW + "");
					p.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					p.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
					p.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

					ItemStack Wool1 = new ItemStack(Material.ORANGE_WOOL);
					ItemMeta Wool1Meta = Wool1.getItemMeta();
					Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
					p.getInventory().setHelmet(Wool1);
					
					ThunderstoneEndMVP.ThunderstoneGuards.put(p, MVPstats.getScore(p.getUniqueId()));
					
					NametagsEvent.GiveNametag(p);

			} else 

			if (PlayerTeam.thunderstoneGuardsTeamSize() >= PlayerTeam.cloudcrawlersTeamSize()) {


				PlayerTeam.setPlayerTeam(p, 1);

				Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -191, 202, 159, -90, 1);
				p.teleport(loc);
				p.getInventory().clear();
				p.sendMessage("You joined" + ChatColor.DARK_AQUA + " Cloudcrawlers");
				p.sendMessage(ChatColor.YELLOW + "");
				p.sendMessage(ChatColor.YELLOW + "*** JOINING THE BATTLEFIELD ***");
				p.sendMessage(ChatColor.YELLOW + "");
				p.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				p.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
				p.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

				ItemStack Wool1 = new ItemStack(Material.CYAN_WOOL);
				ItemMeta Wool1Meta = Wool1.getItemMeta();
				Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
				p.getInventory().setHelmet(Wool1);
				
				ThunderstoneEndMVP.Cloudcrawlers.put(p, MVPstats.getScore(p.getUniqueId()));
				
				NametagsEvent.GiveNametag(p);
				
			}*/
		}


	}
}
