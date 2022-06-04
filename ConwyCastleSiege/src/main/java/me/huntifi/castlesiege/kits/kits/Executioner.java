package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Objects;

/**
 * The executioner kit
 */
public class Executioner extends Kit implements Listener {

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Executioner() {
		super("Executioner", 160, 5);
		super.canSeeHealth = true;

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;
                
		// Weapon
		es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
				ChatColor.GREEN + "Iron Axe", null, null, 34.5);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
						ChatColor.GREEN + "Iron Axe",
						Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 36.5),
				0);
                
		// Chestplate
		es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
				ChatColor.GREEN + "Leather Chestplate", null, null,
				Color.fromRGB(32, 32, 32));
                
		// Leggings
		es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
				ChatColor.GREEN + "Leather Leggings", null, null,
				Color.fromRGB(32, 32, 32));
                
		// Boots
		es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				ChatColor.GREEN + "Iron Boots", null, null);
		// Voted Boots
		es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				ChatColor.GREEN + "Iron Boots",
				Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
                
		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);
                
		super.equipment = es;


		// Death Messages
		super.deathMessage[0] = "You were decapitated by ";
		super.killMessage[0] = "You decapitated ";
	}

	/**
	 * Decapitate an enemy if their hp is below 37%
	 * @param e The event called when hitting another player
	 */
	@EventHandler
	public void onExecute(EntityDamageByEntityEvent e) {
		if (e.isCancelled()) {
			return;
		}

		// Both are players
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player whoWasHit = (Player) e.getEntity();
			Player whoHit = (Player) e.getDamager();

			// Executioner hits with axe
			if (Objects.equals(Kit.equippedKits.get(whoHit.getUniqueId()).name, name) &&
					whoHit.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {

				AttributeInstance healthAttribute = whoWasHit.getAttribute(Attribute.GENERIC_MAX_HEALTH);
				assert healthAttribute != null;

				// Execute
				if (whoWasHit.getHealth() < healthAttribute.getValue() * 0.40) {
					e.setCancelled(true);

					Location loc = whoWasHit.getEyeLocation();
					whoWasHit.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH, 1, 1);
					Material wool = MapController.getCurrentMap().getTeam(whoWasHit.getUniqueId()).primaryWool;
					whoWasHit.getWorld().dropItem(loc, new ItemStack(wool)).setPickupDelay(32767);

					DeathEvent.setKiller(whoWasHit, whoHit);
					whoWasHit.setHealth(0);
				}
			}
		}
	}
}
