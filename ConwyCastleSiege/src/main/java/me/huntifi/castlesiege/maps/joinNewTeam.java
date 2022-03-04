package me.huntifi.castlesiege.maps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.huntifi.castlesiege.Helmsdeep.HelmsdeepEndMVP;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneEndMVP;
import me.huntifi.castlesiege.joinevents.login;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class joinNewTeam {

	public static void HelmsdeepJoinNewTeam() {

		if (currentMaps.currentMapIs("HelmsDeep")) {

			if (login.Playerlist.get(0) != null) {

					Player first = login.Playerlist.get(0);

					for (Player p : Bukkit.getOnlinePlayers()) { 

						if (PlayerTeam.urukhaiTeamSize() == 0) {

							if (first != null) {

								PlayerTeam.setPlayerTeam(first, 1);

								Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17);
								if (!LobbyPlayer.containsPlayer(first)) { LobbyPlayer.addPlayer(first); }
								first.teleport(loc);
								first.getInventory().clear();
								first.sendMessage("You joined" + ChatColor.DARK_GRAY + " The Uruk-hai");
								first.sendMessage(ChatColor.DARK_GRAY + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
								first.sendMessage(ChatColor.DARK_GRAY + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
								first.sendMessage(ChatColor.DARK_GRAY + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

								ItemStack Wool1 = new ItemStack(Material.BLACK_WOOL);
								ItemMeta Wool1Meta = Wool1.getItemMeta();
								Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
								first.getInventory().setHelmet(Wool1);

								HelmsdeepEndMVP.Urukhai.put(first, MVPstats.getScore(first.getUniqueId()));

								NametagsEvent.GiveNametag(first);

							}

						} else if (PlayerTeam.rohanTeamSize() < PlayerTeam.urukhaiTeamSize()) {

							PlayerTeam.setPlayerTeam(p, 2);
							if (!LobbyPlayer.containsPlayer(p)) { LobbyPlayer.addPlayer(p); }
							Location loc = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1);
							p.teleport(loc);
							p.getInventory().clear();
							p.sendMessage("You joined" + ChatColor.DARK_GREEN + " Rohan");
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
								if (!LobbyPlayer.containsPlayer(p)) { LobbyPlayer.addPlayer(p); }
								p.teleport(loc);
								p.getInventory().clear();
								p.sendMessage("You joined" + ChatColor.DARK_GRAY + " The Uruk-hai");
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
	}



	public static void ThunderstoneJoinNewTeam() {

		if (currentMaps.currentMapIs("Thunderstone")) {

			if (login.Playerlist.get(0) != null) {

					Player first = login.Playerlist.get(0);

					for (Player p : Bukkit.getOnlinePlayers()) {

						if (PlayerTeam.cloudcrawlersTeamSize() == 0) {

							if (first != null) {

								PlayerTeam.setPlayerTeam(first, 1);
								Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -191, 202, 159, -90, 1);
								if (!LobbyPlayer.containsPlayer(first)) { LobbyPlayer.addPlayer(first); }
								first.teleport(loc);
								first.getInventory().clear();
								first.sendMessage("You joined" + ChatColor.DARK_AQUA + " Cloudcrawlers");
								first.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
								first.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
								first.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

								ItemStack Wool1 = new ItemStack(Material.CYAN_WOOL);
								ItemMeta Wool1Meta = Wool1.getItemMeta();
								Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
								first.getInventory().setHelmet(Wool1);

								ThunderstoneEndMVP.Cloudcrawlers.put(first, MVPstats.getScore(p.getUniqueId()));

								NametagsEvent.GiveNametag(first);

							}

						}  
						
						if (PlayerTeam.thunderstoneGuardsTeamSize() < PlayerTeam.cloudcrawlersTeamSize()) {
							
							if (PlayerTeam.getPlayerTeam(p) != 1) {

							PlayerTeam.setPlayerTeam(p, 2);
							if (!LobbyPlayer.containsPlayer(p)) { LobbyPlayer.addPlayer(p); }
							Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -187, 201, 106, -90, 2);
							p.teleport(loc);
							p.getInventory().clear();
							p.sendMessage("You joined" + ChatColor.GOLD + " Thunderstone Guard");
							p.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
							p.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
							p.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

							ItemStack Wool1 = new ItemStack(Material.ORANGE_WOOL);
							ItemMeta Wool1Meta = Wool1.getItemMeta();
							Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
							p.getInventory().setHelmet(Wool1);

							ThunderstoneEndMVP.ThunderstoneGuards.put(p, MVPstats.getScore(p.getUniqueId()));

							NametagsEvent.GiveNametag(p);
							
							}

						} else 

							if (PlayerTeam.thunderstoneGuardsTeamSize() >= PlayerTeam.cloudcrawlersTeamSize()) {
								
								if (PlayerTeam.getPlayerTeam(p) != 2) {

								PlayerTeam.setPlayerTeam(p, 1);

								Location loc = new Location(Bukkit.getServer().getWorld("Thunderstone"), -191, 202, 159, -90, 1);
								if (!LobbyPlayer.containsPlayer(p)) { LobbyPlayer.addPlayer(p); }
								p.teleport(loc);
								p.getInventory().clear();
								p.sendMessage("You joined" + ChatColor.DARK_AQUA + " Cloudcrawlers");
								p.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
								p.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
								p.sendMessage(ChatColor.DARK_AQUA + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

								ItemStack Wool1 = new ItemStack(Material.CYAN_WOOL);
								ItemMeta Wool1Meta = Wool1.getItemMeta();
								Wool1Meta.setDisplayName(ChatColor.GREEN + "Woolhead");
								p.getInventory().setHelmet(Wool1);

								ThunderstoneEndMVP.Cloudcrawlers.put(p, MVPstats.getScore(p.getUniqueId()));

								NametagsEvent.GiveNametag(p);
								
								}

							}
					}
			}
		}
	}

}
