package me.huntifi.castlesiege.kits.medic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Cake;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public class MedicAbilities implements Listener {

	public static HashMap<Player, Block> cakes = new HashMap<Player, Block>();

	static ArrayList<Player> hasEaten = new ArrayList<Player>();

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler (priority = EventPriority.MONITOR)
	public void onLadderPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Medic")) {

			if (!(p.getGameMode() == GameMode.CREATIVE)) {

				if (!LobbyPlayer.containsPlayer(p)) {

					if (e.getBlockPlaced().getType() == Material.CAKE) {

						e.setCancelled(false);

						Block cake = e.getBlockPlaced();

						if(cakes.containsKey(p)) {

							cakes.get(p).setType(Material.AIR);

							cakes.remove(p);

						}

						if(!cakes.containsKey(p)) { cakes.put(p, cake);}

					} 
				} 
			} 
		}

	}


	public void onLeaveMedic(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		if (cakes.containsKey(p)) {  

			cakes.get(p).setType(Material.AIR);
			cakes.remove(p);

		}

	}

	public void onLeaveMedic(PlayerChangedWorldEvent e) {

		Player p = e.getPlayer();

		if (cakes.containsKey(p)) {  

			cakes.get(p).setType(Material.AIR);
			cakes.remove(p);

		}

	}

	public void onMedicFirearcher(PlayerDeathEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (cakes.containsKey(p)) {  

				cakes.get(p).setType(Material.AIR);
				cakes.remove(p);

			}

		}

	}

	public static Player returnMedicCakePlacer(Block b) {

		Player placer = null;

		for (Entry<Player, Block> entry : cakes.entrySet()) {

			if (cakes.get(entry.getKey()).equals(b)) {

				placer = entry.getKey();

				return placer;

			}

		}

		return placer;
	}


	@EventHandler
	public void onInteract(BlockBreakEvent event){

		Player p = event.getPlayer();

		if (!LobbyPlayer.containsPlayer(p)) {

			if(event.getBlock().getType().equals(Material.CAKE)) {

				Block block = event.getBlock();

				if (cakes.containsValue(block)) {

					if (PlayerTeam.getPlayerTeam(p) != PlayerTeam.getPlayerTeam(returnMedicCakePlacer(block))) {

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You destroyed an enemy cake"));
						returnMedicCakePlacer(block).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your cake was destroyed by " + p.getName()));

						block.setType(Material.AIR);

						cakes.remove(p);

					} else if (returnMedicCakePlacer(block) == p) {

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You destroyed your own cake"));

						block.setType(Material.AIR);

						cakes.remove(p);

					}

				}

			}

		}

	}

	@EventHandler
	public void eatCake(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if (!LobbyPlayer.containsPlayer(p)) {

			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

				if(e.getClickedBlock().getType().equals(Material.CAKE)) {

					Block block = e.getClickedBlock();
					BlockState state = block.getState();
					MaterialData data = state.getData();

					Cake cake = (Cake) data;

					if (cakes.containsValue(block)) {

						if (PlayerTeam.getPlayerTeam(p) == PlayerTeam.getPlayerTeam(returnMedicCakePlacer(block))) {

							if (!hasEaten.contains(p) && p.getHealth() != p.getMaxHealth()) {

								p.setFoodLevel(17);

								if (cake.getSlicesRemaining() <= 7) {

									p.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 20, 5)));

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + returnMedicCakePlacer(block).getName() + "'s cake is healing you!"));
									returnMedicCakePlacer(block).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "Your cake is healing " + p.getName()));

									if (p != returnMedicCakePlacer(block)) {
										MVPstats.addHeals(returnMedicCakePlacer(block).getUniqueId(), 2);
									}

									hasEaten.add(p);

									new BukkitRunnable() {

										@Override
										public void run() {

											if (hasEaten.contains(p)) { hasEaten.remove(p);  }

										}
									}.runTaskLater(plugin, 25);

								} else {

									block.breakNaturally();
									cakes.remove(returnMedicCakePlacer(block), block);
									state.update();

								}
							}

						} else {

							e.setCancelled(true);
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "This is an enemy cake, destroy it!"));
							return;
						}
					}
				}

			}
		}

	}

	@EventHandler
	public void healTeam(PlayerInteractEntityEvent e) {
		
		Player p = (Player) e.getPlayer();
		
		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Medic")) {
		
		if(p.getItemInHand().getType() != Material.PAPER) return;

		if (e.getRightClicked() instanceof Player) {
			
			Player target = (Player) e.getRightClicked();
			
			if (!LobbyPlayer.containsPlayer(p) && !LobbyPlayer.containsPlayer(target)) {
				
					if (PlayerTeam.getPlayerTeam(target) == PlayerTeam.getPlayerTeam(p)) {

							if (!hasEaten.contains(target) && target.getHealth() != target.getMaxHealth()) {

								target.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 20, 4)));

								target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + p.getName() + " is healing you"));
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You are healing " + target.getName()));
								MVPstats.addHeals(p.getUniqueId(), 1);

								hasEaten.add(target);

								new BukkitRunnable() {

									@Override
									public void run() {

										if (hasEaten.contains(target)) { hasEaten.remove(target);  }

									}
								}.runTaskLater(plugin, 24);

							}

						} else {
							
						return;
						}
					}

				}

		}
	}

}
