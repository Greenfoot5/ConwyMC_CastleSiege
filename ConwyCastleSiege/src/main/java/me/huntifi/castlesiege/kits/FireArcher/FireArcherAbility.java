package me.huntifi.castlesiege.kits.FireArcher;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map.Entry;

public class FireArcherAbility implements Listener {

	public static HashMap<Player, Block> cauldrons = new HashMap<>();


	@EventHandler (priority = EventPriority.MONITOR)
	public void onLadderPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("FireArcher")) {

			if (!(p.getGameMode() == GameMode.CREATIVE)) {

				if (!LobbyPlayer.containsPlayer(p)) {

					if (e.getBlockPlaced().getType() == Material.CAULDRON) {

						e.setCancelled(false);

						Block cauldron = e.getBlockPlaced();

						if(cauldrons.containsKey(p)) {

							cauldrons.get(p).setType(Material.AIR);

							cauldrons.remove(p);

						}

						if(!cauldrons.containsKey(p)) { cauldrons.put(p, cauldron);}

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You placed down your Firepit!"));

					} 
				} 
			} 
		}

	}

	public void onLeaveFirearcher(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		if (cauldrons.containsKey(p)) {  

			cauldrons.remove(p);
			cauldrons.get(p).setType(Material.AIR);

		}

	}

	public void onLeaveFirearcher(PlayerChangedWorldEvent e) {

		Player p = e.getPlayer();

		if (cauldrons.containsKey(p)) {  

			cauldrons.remove(p);
			cauldrons.get(p).setType(Material.AIR);

		}

	}

	public void onDieFirearcher(PlayerDeathEvent e) {

		e.getEntity();

		Player p = (Player) e.getEntity();

		if (cauldrons.containsKey(p)) {

			cauldrons.remove(p);
			cauldrons.get(p).setType(Material.AIR);

		}

	}

	public static Player returnFirearcherCauldronPlacer(Block b) {

		Player placer = null;

		for (Entry<Player, Block> entry : cauldrons.entrySet()) {

			if (cauldrons.get(entry.getKey()).equals(b)) {

				placer = entry.getKey();

				return placer;

			}

		}

		return placer;
	}


	//In This event a firearcher destroys his cauldron and takes it back.

	@EventHandler
	public void onInteract(PlayerInteractEvent event){

		Player p = event.getPlayer();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("FireArcher")) {

			if (!LobbyPlayer.containsPlayer(p)) {

				if(event.getAction() == Action.LEFT_CLICK_BLOCK){

					if(event.getClickedBlock().getType().equals(Material.CAULDRON)) {

						Block block = event.getClickedBlock();

						FirearcherKit giveItems22 = new FirearcherKit();

						ItemStack bb = giveItems22.getFirearcherFirepit();

						if (cauldrons.containsKey(p)) {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You took back your Firepit!"));

							p.getInventory().setItem(3, bb);

							block.setType(Material.AIR);

							cauldrons.remove(p);

						}

					}
				}
			}

		}

	}

	//In this event a enemy player kicks over the enemy firearcher's cauldron.

	@EventHandler
	public void onInteract2(PlayerInteractEvent event){

		Player p = event.getPlayer(); //the player that kicks it over

		if (!LobbyPlayer.containsPlayer(p)) {

			if(event.getAction() == Action.LEFT_CLICK_BLOCK){

				if(event.getClickedBlock().getType().equals(Material.CAULDRON)) {

					Block block = event.getClickedBlock();

					if (cauldrons.containsValue(block)) {

						Player placer = returnFirearcherCauldronPlacer(block);

						if (PlayerTeam.getPlayerTeam(placer) != PlayerTeam.getPlayerTeam(p)) {

							block.setType(Material.AIR);

							p.playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_INFECT , 5, 1);

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You kicked over " + placer.getName() + "'s Firepit!"));

							cauldrons.remove(p);

						}
					}

				}
			}
		}

	}

	public static void playSound(Player player) {

		Location location = player.getLocation();

		Sound effect = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		player.playSound(location, effect, volume, pitch);
	}


	public static int getAmount(Player arg0, ItemStack arg1) {
		if (arg1 == null)
			return 0;
		int amount = 0;
		for (int i = 0; i < 36; i++) {
			ItemStack slot = arg0.getInventory().getItem(i);
			if (slot == null || !slot.isSimilar(arg1))
				continue;
			amount += slot.getAmount();
		}
		return amount;
	}



	//Here when you click the cauldron you get the fire arrows.

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFirearcherAttempt(PlayerInteractEvent event){

		Player p = event.getPlayer();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("FireArcher")) {

			if (!LobbyPlayer.containsPlayer(p)) {

				if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

					if(event.getClickedBlock().getType().equals(Material.CAULDRON)) {

						if (p.getInventory().getItemInHand().getType() == Material.ARROW) {

							Block block = event.getClickedBlock();

							if (cauldrons.containsValue(block)) {

								int cooldown = p.getCooldown(Material.ARROW);

								ItemStack specialArrow = new ItemStack(Material.TIPPED_ARROW);
								ItemMeta specialArrowMeta;
								specialArrowMeta = specialArrow.getItemMeta();
								specialArrowMeta.setDisplayName(ChatColor.GOLD + "Fire arrow");
								PotionMeta meta = (PotionMeta) specialArrow.getItemMeta();
								meta.setColor(Color.ORANGE);
								specialArrowMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
								specialArrow.setItemMeta(meta);

								if (getAmount(p, specialArrow) < 7 && cooldown == 0) {

									p.setCooldown(Material.ARROW, 30);

									new BukkitRunnable() {

										@Override
										public void run() {

											p.getInventory().addItem(specialArrow);

											playSound(p);

											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You lit an arrow."));

											p.getInventory().removeItem(new ItemStack(Material.ARROW));
										}

									}.runTaskLater(plugin, 30);

								} else if (getAmount(p, specialArrow) == 7) {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You can't make more than 7 fire arrows."));
								}

							}

						}
					}
				}
			}

		}

	}

	@EventHandler
	public void FlameBow(EntityShootBowEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("FireArcher")) {

				if (p.getInventory().contains(Material.TIPPED_ARROW)) {

					if (e.getProjectile() instanceof Arrow) {

						if (p.getInventory().getItemInOffHand().getType() != Material.ARROW) {

							final Arrow arrow = (Arrow) e.getProjectile();

							arrow.setFireTicks(180);

						}
					}

				}

			}
		}
	}



	@EventHandler
	public void onFireDamage(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (!LobbyPlayer.containsPlayer(p)) {

				if (e.getCause() == DamageCause.FIRE_TICK) {

					e.setDamage(6);

				}

			}
		}

	}



	@EventHandler
	public void onHitTnt(BlockIgniteEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {

		event.setCancelled(true);

	}


}
