package me.huntifi.castlesiege.kits.Cavalry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class CavalryAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");



	@EventHandler
	public void onRide(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Cavalry")) {
			if (!LobbyPlayer.containsPlayer(p)) {
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
					if (e.getItem().getType() == Material.WHEAT) {
						
						int cooldown = p.getCooldown(Material.WHEAT);

						if (cooldown == 0) {

							p.setCooldown(Material.WHEAT, 600);

							if (p.isInsideVehicle()) {
								
								p.getVehicle().remove();
								
							}
							
							onHorse(p);

						}


					}


				} 
			}
		}
	}

	public static void onHorse(Player p) {
		Horse horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
		
		horse.setTamed(true); // Sets horse to tamed
		horse.setOwner(p);
		horse.setAdult();
		AttributeInstance hAttribute = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		hAttribute.setBaseValue(150.0);
		horse.setHealth(150.0);
		AttributeInstance kbAttribute = horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
		kbAttribute.setBaseValue(1);
		AttributeInstance speedAttribute = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		speedAttribute.setBaseValue(0.2425);
		AttributeInstance jumpAttribute = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH);
		jumpAttribute.setBaseValue(0.8);

		CavalryKit CavalrygiveItems = new CavalryKit(); 
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE)); // Gives horse saddle
		ItemStack Horsearmor = CavalrygiveItems.getCavalryArmor();
		horse.getInventory().setArmor(Horsearmor); // Gives horse armor
		horse.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 99999999, 0)));
		horse.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 99999999, 1)));
		horse.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 99999999, 0)));
		Location loc = p.getLocation();
		horse.teleport(loc);
		
		horse.addPassenger(p);

	}



	@EventHandler
	public void ondismount (EntityDismountEvent e) {

		Player player = (Player) e.getEntity();

		if (StatsChanging.getKit(player.getUniqueId()).equalsIgnoreCase("Cavalry")) {

			if (e.getDismounted() instanceof Horse) {

				Entity vehicle = e.getDismounted();

				vehicle.remove();


			}
		}
	}




	@EventHandler
	public void onDeath (PlayerDeathEvent e ) {
		Player p = (Player) e.getEntity();
		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Cavalry")) {
			
			if (p.getVehicle() instanceof Horse) {
				
				p.getVehicle().remove();
				
			}
			
			p.setCooldown(Material.WHEAT, 1);

		}
	}



	@EventHandler
	public void onLeave (PlayerQuitEvent e ) {

		Player p = (Player) e.getPlayer();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Cavalry")) {
			
			if (p.getVehicle() != null) {

				Entity vehicle = p.getVehicle();
				vehicle.remove();

			}
		}
	}
}
