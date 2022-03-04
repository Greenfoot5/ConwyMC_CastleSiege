package me.huntifi.castlesiege.stats.levels;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;

public class LevelingEvent {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void doLeveling() {

				for (Player p : Bukkit.getOnlinePlayers()) {

						UUID uuid = p.getUniqueId();
						
						if (StatsChanging.getScore(uuid) <= 0) {
							
							StatsChanging.setLevel(uuid, 0);
						
						}

						else if (StatsChanging.getScore(uuid) >= 8 && StatsChanging.getScore(uuid) < 24 && StatsChanging.getLevel(uuid) != 1) {

							p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "1");
							StatsChanging.setLevel(uuid, 1);
							NametagsEvent.GiveNametag(p);
						
						}

						else if (StatsChanging.getScore(uuid) >= 24 && StatsChanging.getScore(uuid) < 56 && StatsChanging.getLevel(uuid) != 2) {

								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "2");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
								StatsChanging.setLevel(uuid, 2);
								NametagsEvent.GiveNametag(p);
					
						}

						else if (StatsChanging.getScore(uuid) >= 56 && StatsChanging.getScore(uuid) < 104 && StatsChanging.getLevel(uuid) != 3) {

								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "3");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
								StatsChanging.setLevel(uuid, 3);
								NametagsEvent.GiveNametag(p);
						}

						else if (StatsChanging.getScore(uuid) >= 104 && StatsChanging.getScore(uuid) < 168 && StatsChanging.getLevel(uuid) != 4) {

								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "4");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
	
								StatsChanging.setLevel(uuid, 4);
								NametagsEvent.GiveNametag(p);
					
						}

						else if (StatsChanging.getScore(uuid) >= 168 && StatsChanging.getScore(uuid) < 296 && StatsChanging.getLevel(uuid) != 5) {

		
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "5");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
			
								StatsChanging.setLevel(uuid, 5);
								NametagsEvent.GiveNametag(p);
					
						}

						else if (StatsChanging.getScore(uuid) >= 296 && StatsChanging.getScore(uuid) < 424 && StatsChanging.getLevel(uuid) != 6) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "6");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
			
								StatsChanging.setLevel(uuid, 6);
								NametagsEvent.GiveNametag(p);
					
						}

						else if (StatsChanging.getScore(uuid) >= 424 && StatsChanging.getScore(uuid) < 604 && StatsChanging.getLevel(uuid) != 7) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "7");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
				
								StatsChanging.setLevel(uuid, 7);
								NametagsEvent.GiveNametag(p);
					
						}

						else if (StatsChanging.getScore(uuid) >= 604 && StatsChanging.getScore(uuid) < 784 && StatsChanging.getLevel(uuid) != 8) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "8");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 8);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 784 && StatsChanging.getScore(uuid) < 1084 && StatsChanging.getLevel(uuid) != 9) {

								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "9");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 9);
								NametagsEvent.GiveNametag(p);
					
						}

						else if (StatsChanging.getScore(uuid) >= 1084 && StatsChanging.getScore(uuid) < 1340 && StatsChanging.getLevel(uuid) != 10) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "10");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
								Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has reached level 10!");
					
								StatsChanging.setLevel(uuid, 10);
								NametagsEvent.GiveNametag(p);
				
						}
 
						else if (StatsChanging.getScore(uuid) >= 1340 && StatsChanging.getScore(uuid) < 1852 && StatsChanging.getLevel(uuid) != 11) {

								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "11");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 11);
								NametagsEvent.GiveNametag(p);
				
						}

						else if (StatsChanging.getScore(uuid) >= 1852 && StatsChanging.getScore(uuid) < 2364 && StatsChanging.getLevel(uuid) != 12) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "12");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 12);
								NametagsEvent.GiveNametag(p);
				
						}
						
						else if (StatsChanging.getScore(uuid) >= 2364 && StatsChanging.getScore(uuid) < 3089 && StatsChanging.getLevel(uuid) != 13) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "13");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 13);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 3089 && StatsChanging.getScore(uuid) < 3814 && StatsChanging.getLevel(uuid) != 14) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "14");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 14);
								NametagsEvent.GiveNametag(p);
				
						}
						
						else if (StatsChanging.getScore(uuid) >= 3814 && StatsChanging.getScore(uuid) < 4838 && StatsChanging.getLevel(uuid) != 15) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "15");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 15);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 4838 && StatsChanging.getScore(uuid) < 5862 && StatsChanging.getLevel(uuid) != 16) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "16");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 16);
								NametagsEvent.GiveNametag(p);
				
						}
						
						else if (StatsChanging.getScore(uuid) >= 5862 && StatsChanging.getScore(uuid) < 7062 && StatsChanging.getLevel(uuid) != 17) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "17");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 17);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 7062 && StatsChanging.getScore(uuid) < 8262 && StatsChanging.getLevel(uuid) != 18) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "18");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 18);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 8262 && StatsChanging.getScore(uuid) < 9862 && StatsChanging.getLevel(uuid) != 19) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "19");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 19);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 9862 && StatsChanging.getScore(uuid) < 11262 && StatsChanging.getLevel(uuid) != 20) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "20");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
								Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has reached level 20!");
						
								StatsChanging.setLevel(uuid, 20);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 11262 && StatsChanging.getScore(uuid) < 12662 && StatsChanging.getLevel(uuid) != 21) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "21");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 21);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 12662 && StatsChanging.getScore(uuid) < 14312 && StatsChanging.getLevel(uuid) != 22) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "22");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 22);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 14312 && StatsChanging.getScore(uuid) < 16162 && StatsChanging.getLevel(uuid) != 23) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "23");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 23);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 16162 && StatsChanging.getScore(uuid) < 18212 && StatsChanging.getLevel(uuid) != 24) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "24");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 24);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 18212 && StatsChanging.getScore(uuid) < 20662 && StatsChanging.getLevel(uuid) != 25) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "25");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 25);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 20662 && StatsChanging.getScore(uuid) < 23312 && StatsChanging.getLevel(uuid) != 26) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "26");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 26);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 23312 && StatsChanging.getScore(uuid) < 26212 && StatsChanging.getLevel(uuid) != 27) {

					
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "27");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 27);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 26212 && StatsChanging.getScore(uuid) < 29162 && StatsChanging.getLevel(uuid) != 28) {

					
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "28");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 28);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 29162 && StatsChanging.getScore(uuid) < 32662 && StatsChanging.getLevel(uuid) != 29) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "29");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 29);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 32662 && StatsChanging.getScore(uuid) < 35812 && StatsChanging.getLevel(uuid) != 30) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "30");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
								Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has reached level 30!");
						
								StatsChanging.setLevel(uuid, 30);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 35812 && StatsChanging.getScore(uuid) < 39042 && StatsChanging.getLevel(uuid) != 31) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "31");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 31);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 39042 && StatsChanging.getScore(uuid) < 42322 && StatsChanging.getLevel(uuid) != 32) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "32");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 32);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 42322 && StatsChanging.getScore(uuid) < 45742 && StatsChanging.getLevel(uuid) != 33) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "33");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 33);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 45742 && StatsChanging.getScore(uuid) < 49382 && StatsChanging.getLevel(uuid) != 34) {

								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "34");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 34);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 49382 && StatsChanging.getScore(uuid) < 53102 && StatsChanging.getLevel(uuid) != 35) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "35");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
							
								StatsChanging.setLevel(uuid, 35);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 53102 && StatsChanging.getScore(uuid) < 56962 && StatsChanging.getLevel(uuid) != 36) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "36");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 36);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 56962 && StatsChanging.getScore(uuid) < 60937 && StatsChanging.getLevel(uuid) != 37) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "37");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
					
								StatsChanging.setLevel(uuid, 37);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 60937 && StatsChanging.getScore(uuid) < 64952 && StatsChanging.getLevel(uuid) != 38) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "38");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
						
								StatsChanging.setLevel(uuid, 38);
								NametagsEvent.GiveNametag(p);
					
						}
						
						else if (StatsChanging.getScore(uuid) >= 64952 && StatsChanging.getScore(uuid) < 69202 && StatsChanging.getLevel(uuid) != 39) {

							
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "39");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
							
								StatsChanging.setLevel(uuid, 39);
								NametagsEvent.GiveNametag(p);
						
						}
						
						else if (StatsChanging.getScore(uuid) >= 69202 && StatsChanging.getScore(uuid) < 73272 && StatsChanging.getLevel(uuid) != 40) {

						
								p.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "40");
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
								Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has reached level 40!");
						
								StatsChanging.setLevel(uuid, 40);
								NametagsEvent.GiveNametag(p);
					
						}
						
					
				}
	}

}


