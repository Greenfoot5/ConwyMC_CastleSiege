package me.huntifi.castlesiege.Helmsdeep;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class HelmsdeepJoin implements Listener {

	public Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		if (MapController.currentMapIs("HelmsDeep")) {

			if (PlayerTeam.rohanTeamSize() < PlayerTeam.urukhaiTeamSize()) {

					PlayerTeam.setPlayerTeam(p, 2);
					LobbyPlayer.addPlayer(p);
					Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1);
					p.teleport(loc);
					p.getInventory().clear();
					p.sendMessage("You joined" + ChatColor.DARK_GREEN + " Rohan");
					p.sendMessage(ChatColor.YELLOW + "");
					p.sendMessage(ChatColor.YELLOW + "*** JOINING THE BATTLEFIELD ***");
					p.sendMessage(ChatColor.YELLOW + "");
					p.sendMessage(ChatColor.DARK_GREEN + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					p.sendMessage(ChatColor.DARK_GREEN + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
					p.sendMessage(ChatColor.DARK_GREEN + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

					ItemStack Wool1 = new ItemStack(Material.GREEN_WOOL);
					ItemMeta Wool1Meta = Wool1.getItemMeta();
					Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
					p.getInventory().setHelmet(Wool1);
					
					HelmsdeepEndMVP.Rohan.put(p, MVPstats.getScore(p.getUniqueId()));
					
					NametagsEvent.GiveNametag(p);

			} else 

			if (PlayerTeam.rohanTeamSize() >= PlayerTeam.urukhaiTeamSize()) {


				PlayerTeam.setPlayerTeam(p, 1);

				Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17);
				LobbyPlayer.addPlayer(p);
				p.teleport(loc);
				p.getInventory().clear();
				p.sendMessage("You joined" + ChatColor.DARK_GRAY + " The Uruk-hai");
				p.sendMessage(ChatColor.YELLOW + "");
				p.sendMessage(ChatColor.YELLOW + "*** JOINING THE BATTLEFIELD ***");
				p.sendMessage(ChatColor.YELLOW + "");
				p.sendMessage(ChatColor.DARK_GRAY + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				p.sendMessage(ChatColor.DARK_GRAY + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
				p.sendMessage(ChatColor.DARK_GRAY + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

				ItemStack Wool1 = new ItemStack(Material.BLACK_WOOL);
				ItemMeta Wool1Meta = Wool1.getItemMeta();
				Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
				p.getInventory().setHelmet(Wool1);
				
				HelmsdeepEndMVP.Urukhai.put(p, MVPstats.getScore(p.getUniqueId()));
				
				NametagsEvent.GiveNametag(p);
				
			}
		}


	}

}
