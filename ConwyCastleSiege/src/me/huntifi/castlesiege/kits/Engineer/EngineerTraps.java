package me.huntifi.castlesiege.kits.Engineer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class EngineerTraps implements Listener {

	public static ArrayList<Block> traps = new ArrayList<Block>();

	public static HashMap<Player, Block> hashmapTraps = new HashMap<Player, Block>();

	public static HashMap<Player, Integer> amountOfTraps = new HashMap<Player, Integer>();

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		if (!(p.getGameMode() == GameMode.CREATIVE)) {

			if (StatsChanging.getKit(uuid).equalsIgnoreCase("Engineer")) {

				if (e.getBlockPlaced().getType() == Material.STONE_PRESSURE_PLATE) {

					if (getTraps(p) <= 8) {

						e.setCancelled(false);

						Block trap = e.getBlockPlaced();

						if (!traps.contains(trap)) {  

							traps.add(trap);
						}

						if (!hashmapTraps.containsValue(trap)) {  hashmapTraps.put(p, trap);  }
						addTraps(p, 1);


					} else if (getTraps(p) > 8) {

						e.setCancelled(true);

						p.sendMessage(ChatColor.RED + "You have placed your maximum amount of traps.");
						p.sendMessage(ChatColor.RED + "Pick your traps up if you need them or destroy them and restock.");

					}
				}
			}

		}

	}
	
	public static Block returnTrap() {

		Block highest1 = null;

		for (Entry<Player, Block> entry : hashmapTraps.entrySet()) {

			highest1 = entry.getValue();


		}
		return highest1;

	}
	
	@EventHandler
	public void onWalkOverTrap(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		
		Player trapper = returnTrapPlacer();
		
		if (PlayerTeam.getPlayerTeam(p) != PlayerTeam.getPlayerTeam(trapper)) {
		
			if (p.getLocation().getBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {
					
				if (returnTrap().equals(p.getLocation().getBlock()) && returnTrap() != null) {
					
					if ((p.getHealth() -25) > 0) {
					
					p.damage(25);
					returnTrap().setType(Material.AIR);
					amountOfTraps.remove(trapper);
					hashmapTraps.remove(trapper);
					traps.remove(returnTrap());
					
					} else {
						
						p.setHealth(0);
						returnTrap().setType(Material.AIR);
						amountOfTraps.remove(trapper);
						hashmapTraps.remove(trapper);
						traps.remove(returnTrap());
						
						p.sendMessage("You stepped on " + NametagsEvent.colour(trapper) + trapper.getName() + ChatColor.RESET + "'s traps and died.");
						trapper.sendMessage(NametagsEvent.colour(p) + p.getName() + ChatColor.RESET + " stepped on your trap(s) " + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(trapper) + ")");
						
					}
				}
					
			}
		}
	}

	public static Player returnTrapPlacer() {

		Player placer = null;

		for (Entry<Player, Block> entry : hashmapTraps.entrySet()) {

			placer = entry.getKey();

		}

		return placer;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			UUID uuid = p.getUniqueId();

			if (StatsChanging.getKit(uuid).equalsIgnoreCase("Engineer")) {

				for (Block b : traps) {

					if (hashmapTraps.get(p).equals(b)) {

						b.setType(Material.AIR);
						amountOfTraps.remove(p);
						hashmapTraps.remove(p);
						traps.remove(b);

					}


				}

			}
		}

	}


	@EventHandler
	public void onDestroy(BlockBreakEvent e) {

		Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		if (!(p.getGameMode() == GameMode.CREATIVE)) {

			if (StatsChanging.getKit(uuid).equalsIgnoreCase("Engineer")) {

				if (e.getBlock().getType() == Material.STONE_PRESSURE_PLATE) {

					Block trap = e.getBlock();

					if (traps.contains(trap)) {

						e.setCancelled(false);

						if (traps.contains(trap)) {  traps.remove(trap);  }
						if (hashmapTraps.containsValue(trap)) {  hashmapTraps.remove(p, trap);  }
						removeTraps(p,1);
						trap.setType(Material.AIR);

					}
				}
			} 

		}

	}

	@EventHandler
	public void onPlayerInteractTrap(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		if (!LobbyPlayer.containsPlayer(p)) {

			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

				if (e.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {

					if (StatsChanging.getKit(uuid).equalsIgnoreCase("Engineer")) {

						if (traps.contains(e.getClickedBlock())) {

							if (hashmapTraps.get(p) == e.getClickedBlock()) {

								if (getTraps(p) != 8) {

									e.getClickedBlock().setType(Material.AIR);
									EngineerKit item = new EngineerKit();
									ItemStack theItem = item.getEngineerTraps();
									ItemMeta theMeta = theItem.getItemMeta();

									ItemStack bob = new ItemStack(Material.STONE_PRESSURE_PLATE);
									bob.setItemMeta(theMeta);

									p.getInventory().addItem(bob);

									p.sendMessage(ChatColor.GREEN + "You took this trap.");

								} else {

									p.sendMessage(ChatColor.DARK_RED + "You took this trap and put it away.");	
									e.getClickedBlock().setType(Material.AIR);
									
								}

							} else if (hashmapTraps.get(p) != e.getClickedBlock()) {

								p.sendMessage(ChatColor.DARK_RED + "You can't pick up traps that are not your own.");	

							}
						}
					}
				}	
			}

		}


	}


	public static void addTraps(Player uuid, int value) {

		final int save = amountOfTraps.get(uuid);

		if (amountOfTraps.containsKey(uuid)) {

			amountOfTraps.remove(uuid);

		}

		amountOfTraps.put(uuid, save + value);

	}

	public static void removeTraps(Player uuid, int value) {

		final int save = amountOfTraps.get(uuid);

		if (amountOfTraps.containsKey(uuid)) {

			amountOfTraps.remove(uuid);

		}

		amountOfTraps.put(uuid, save - value);

	}

	public static void setTraps(Player uuid, int value) {

		if (amountOfTraps.containsKey(uuid)) {

			amountOfTraps.remove(uuid);

		}

		amountOfTraps.put(uuid, value);
	}

	public static int getTraps(Player uuid) {

		if (amountOfTraps.containsKey(uuid)) {

			return amountOfTraps.get(uuid);

		}

		return 0;
	}



}
