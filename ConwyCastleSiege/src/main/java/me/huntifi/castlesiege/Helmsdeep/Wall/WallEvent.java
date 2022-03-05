package me.huntifi.castlesiege.Helmsdeep.Wall;


import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.structures.MakeStructure;
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class WallEvent implements Listener, Runnable {


	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static int tnt_counter = 0;

	public static HashMap<Player, Boolean> isCarryingTnt = new HashMap<Player, Boolean>();

	@EventHandler
	public void onTntTake(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if(MapController.currentMapIs("HelmsDeep")) {

			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

				Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125); //soundlocation

				double LeverDistance = p.getLocation().distance(loc);

				if (e.getClickedBlock().getType() == Material.TNT && LeverDistance <= 5) {

					if (PlayerTeam.playerIsInTeam(p, 1)) {

						e.getClickedBlock().setType(Material.AIR);

						ItemStack tnt = new ItemStack(Material.TNT);
						ItemMeta tntMeta = tnt.getItemMeta();
						tntMeta.setDisplayName(ChatColor.GREEN + "Tnt-head");
						p.getInventory().setHelmet(tnt);

						p.getInventory().setHelmet(tnt);

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You picked the explosives up!"));
						isCarryingTnt.put(p, true);



					}

				} 
			}
		}
	}


	@EventHandler
	public void onTorchTake(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if(MapController.currentMapIs("HelmsDeep")) {

			if(p.getWorld() == (plugin.getServer().getWorld("HelmsDeep"))) {

				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

					Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125); //soundlocation

					double LeverDistance = p.getLocation().distance(loc);

					if (e.getClickedBlock().getType() == Material.TORCH && LeverDistance <= 5) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {

							e.getClickedBlock().setType(Material.AIR);

							ItemStack tnt = new ItemStack(Material.GLOWSTONE);
							ItemMeta tntMeta = tnt.getItemMeta();
							tntMeta.setDisplayName(ChatColor.GREEN + "Glowstone-head");
							p.getInventory().setHelmet(tnt);

							p.getInventory().setHelmet(tnt);

							isCarryingTnt.put(p, true);

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You picked up the torch!"));
							Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " has picked up the torch!");


						}

					} 
				}

			}
		}
	}


	@EventHandler
	public void onTntPlace(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if(MapController.currentMapIs("HelmsDeep")) {

			final Location tloc1 = new Location(plugin.getServer().getWorld("HelmsDeep"), 1026, 34, 1124); //where the tnts are placed
			final Location tloc2 = new Location(plugin.getServer().getWorld("HelmsDeep"), 1026, 34, 1123);
			final Location tloc3 = new Location(plugin.getServer().getWorld("HelmsDeep"), 1027, 34, 1123);

			final Location loc2 = new Location(plugin.getServer().getWorld("HelmsDeep"), 1026, 34, 1124); //radius Loc where tnt can be placed

			final Location blockLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125); //where the tnt spawns

			if (isCarryingTnt.containsKey(p)) {

				if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {

					Block theBlock = e.getClickedBlock();

					double LeverDistance = theBlock.getLocation().distance(loc2);

					if (LeverDistance <= 4) {

						ItemStack Wool2 = new ItemStack(Material.BLACK_WOOL);
						ItemMeta WoolMeta2 = Wool2.getItemMeta();
						WoolMeta2.setDisplayName(ChatColor.GREEN + "Woolhead");
						p.getInventory().setHelmet(Wool2);

						if (isCarryingTnt.containsKey(p)) {
							isCarryingTnt.remove(p);
						}

						if (tnt_counter == 0 && LeverDistance <= 5) {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "You placed the explosive down. (1/3)"));
							Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { MVPstats.setSupports(p.getUniqueId(), MVPstats.getSupports(p.getUniqueId()) + 12.0 );  });
							blockLoc.getBlock().setType(Material.TNT);
							tloc1.getBlock().setType(Material.TNT);
							tnt_counter = 1;
						} else if (tnt_counter == 1 && LeverDistance <= 5) {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "You placed the explosive down. (2/3)"));
							Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { MVPstats.setSupports(p.getUniqueId(), MVPstats.getSupports(p.getUniqueId()) + 12.0 );  });
							blockLoc.getBlock().setType(Material.TNT);
							tloc2.getBlock().setType(Material.TNT);
							tnt_counter = 2;
						} else if (tnt_counter == 2 && LeverDistance <= 5) {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "You placed the explosive down. (3/3)"));
							Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { MVPstats.setSupports(p.getUniqueId(), MVPstats.getSupports(p.getUniqueId()) + 12.0 );  });
							p.sendMessage(ChatColor.DARK_AQUA + "Now get the torch!");
							blockLoc.getBlock().setType(Material.TORCH);
							tloc3.getBlock().setType(Material.TNT);
							tnt_counter = 3;

						} else if (tnt_counter == 3 && LeverDistance <= 5) {

							final Location wallLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1040, 34, 1140);

							try {
								MakeStructure.createSchematicStructure(wallLoc, "HelmsdeepWallBroken", "HelmsDeep");
							} catch (WorldEditException e1) {
								e1.printStackTrace();
							}

							for (Player close : Bukkit.getOnlinePlayers()) {
								final Location death = new Location(plugin.getServer().getWorld("HelmsDeep"), 1026, 40, 1125);
								double closeDistance = close.getLocation().distance(death);

								if (closeDistance < 15) { close.setHealth(0); }

							}

							plugin.getServer().getWorld("HelmsDeep").createExplosion(tloc1, 15, false, false, p);

							Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { MVPstats.setSupports(p.getUniqueId(), MVPstats.getSupports(p.getUniqueId()) + 30.0 );  });

							Bukkit.getWorld("HelmsDeep").playSound(loc2, Sound.ENTITY_GENERIC_EXPLODE , 10000, 2 );
							Bukkit.getWorld("HelmsDeep").playSound(loc2, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR , 10000, 2 );
							Bukkit.getWorld("HelmsDeep").playSound(loc2, Sound.ENTITY_FIREWORK_ROCKET_BLAST , 10000, 2 );
							Bukkit.getWorld("HelmsDeep").playSound(loc2, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST , 10000, 2 );
							Bukkit.getWorld("HelmsDeep").playSound(loc2, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 10000, 2 );
							Bukkit.getWorld("HelmsDeep").playSound(loc2, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE , 10000, 2 );
							Bukkit.broadcastMessage(ChatColor.RED + "The Deeping Wall has been blown up!");

							tnt_counter = 4;

						} 
					}
				}
			}
		}


	}



	@EventHandler
	public void onTntDeath(PlayerDeathEvent e) {

		if(MapController.currentMapIs("HelmsDeep")) {


			if (e.getEntity() instanceof Player) {

				Player p = (Player) e.getEntity();

				if(p.getWorld() == (plugin.getServer().getWorld("HelmsDeep"))) {

					Location blockLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125); //soundlocation

					if (isCarryingTnt.containsKey(p)) {

						ItemStack Wool2 = new ItemStack(Material.BLACK_WOOL);
						ItemMeta WoolMeta2 = Wool2.getItemMeta();
						WoolMeta2.setDisplayName(ChatColor.GREEN + "Woolhead");
						p.getInventory().setHelmet(Wool2);

						p.getInventory().setHelmet(Wool2);

						isCarryingTnt.remove(p);


						if (tnt_counter == 0) {

							blockLoc.getBlock().setType(Material.TNT);

						} else if (tnt_counter == 1) {

							blockLoc.getBlock().setType(Material.TNT);

						} else if (tnt_counter == 2) {

							blockLoc.getBlock().setType(Material.TNT);

						} else if (tnt_counter == 3) {

							blockLoc.getBlock().setType(Material.TORCH);

						}

					}


				}
			}
		}
	}



	@EventHandler
	public void onTntQuit(PlayerQuitEvent e) {

		if(MapController.currentMapIs("HelmsDeep")) {

			Player p = e.getPlayer();

			if(p.getWorld() == (plugin.getServer().getWorld("HelmsDeep"))) {

				Location blockLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125); //soundlocation

				if (isCarryingTnt.containsKey(p)) {

					if (isCarryingTnt.containsKey(p)) {
						isCarryingTnt.remove(p);
					}

					if (tnt_counter == 0) {

						blockLoc.getBlock().setType(Material.TNT);

					} else if (tnt_counter == 1) {

						blockLoc.getBlock().setType(Material.TNT);

					} else if (tnt_counter == 2) {

						blockLoc.getBlock().setType(Material.TNT);

					} else if (tnt_counter == 3) {

						blockLoc.getBlock().setType(Material.TORCH);

					}

				}


			}
		}
	}


	@EventHandler
	public void onTntLeave(PlayerChangedWorldEvent e) {

		Player p = e.getPlayer();

		if(MapController.currentMapIs("HelmsDeep")) {

			if(p.getWorld() == (plugin.getServer().getWorld("HelmsDeep"))) {


				Location blockLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125); //soundlocation

				if (isCarryingTnt.containsKey(p)) {

					if (isCarryingTnt.containsKey(p)) {
						isCarryingTnt.remove(p);
					}

					if (tnt_counter == 0) {

						blockLoc.getBlock().setType(Material.TNT);

					} else if (tnt_counter == 1) {

						blockLoc.getBlock().setType(Material.TNT);

					} else if (tnt_counter == 2) {

						blockLoc.getBlock().setType(Material.TNT);

					} else if (tnt_counter == 3) {

						blockLoc.getBlock().setType(Material.TORCH);

					}

				}


			}
		}
	}


	@Override
	public void run() {

		for (Player online : Bukkit.getOnlinePlayers()) {

			if(MapController.currentMapIs("HelmsDeep")) {

				if(online.getWorld() == (plugin.getServer().getWorld("HelmsDeep"))) {

					if (!(tnt_counter == 4)) {

						if (online.getInventory().getHelmet().getType() == Material.TNT) {

							online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "" + ChatColor.BOLD + "You are holding an explosive!"));


						} else if (online.getInventory().getHelmet().getType() == Material.GLOWSTONE) {

							online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "" + ChatColor.BOLD + "You are holding the torch!"));

						}
					}

				}
			}
		}
	}
}
