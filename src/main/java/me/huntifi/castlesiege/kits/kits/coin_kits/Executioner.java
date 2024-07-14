package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The executioner kit
 */
public class Executioner extends CoinKit implements Listener {

	private static final int health = 210;
	private static final double regen = 15;
	private static final double meleeDamage = 48;
	private static final int ladderCount = 4;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Executioner() {
		super("Executioner", health, regen, Material.IRON_AXE);
		super.canSeeHealth = true;

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
                
		// Weapon
		es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_AXE),
				Component.text("Execution Axe", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text("48 Melee Damage", NamedTextColor.DARK_GREEN)), null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				CSItemCreator.weapon(new ItemStack(Material.IRON_AXE),
						Component.text("Execution Axe", NamedTextColor.GREEN),
						List.of(Component.empty(),
								Component.text("50 Melee Damage", NamedTextColor.DARK_GREEN)
										.append(Component.text(" (+2 from voting)", NamedTextColor.DARK_AQUA))),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
				0);
                
		// Chestplate
		es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
				Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
				Color.fromRGB(32, 32, 32));
                
		// Leggings
		es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
				Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
				Color.fromRGB(32, 32, 32));
                
		// Boots
		es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
		// Voted Boots
		es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				Component.text("Iron Boots", NamedTextColor.GREEN),
				Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
                
		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);
                
		super.equipment = es;

		// Perm Potion Effect
		super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

		// Death Messages
		super.deathMessage[0] = "You were decapitated by ";
		super.killMessage[0] = " decapitated ";
	}

	/**
	 * Decapitate an enemy if their hp is below 37%
	 * @param e The event called when hitting another player
	 */
	@EventHandler(ignoreCancelled = true)
	public void onExecute(EntityDamageByEntityEvent e) {
		// Both are players
		if (e.getEntity() instanceof Attributable && e.getDamager() instanceof Player) {
			Attributable defAttri = (Attributable) e.getEntity();
			Damageable defender = (Damageable) e.getEntity();
			Player attacker = (Player) e.getDamager();

			// Executioner hits with axe
			if (Objects.equals(Kit.equippedKits.get(attacker.getUniqueId()).name, name) &&
					attacker.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {

				AttributeInstance healthAttribute = defAttri.getAttribute(Attribute.GENERIC_MAX_HEALTH);
				assert healthAttribute != null;

				// Execute
				if (defender.getHealth() < healthAttribute.getValue() * 0.3) {
					e.setCancelled(true);

					Location loc = defender.getLocation();
					// Do extra stuff if they were a player
					if (defender instanceof Player) {
						Player p = (Player) defender;
						loc = p.getEyeLocation();

						Material wool = TeamController.getTeam(defender.getUniqueId()).primaryWool;
						defender.getWorld().dropItem(loc, new ItemStack(wool)).setPickupDelay(32767);

						DeathEvent.setKiller(p, attacker);
					}

					defender.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH, 1, 1);
					defender.setHealth(0);
				}
			}
		}
	}

	/**
	 * @return The lore to add to the kit gui item
	 */
	@Override
    public ArrayList<Component> getGuiDescription() {
		ArrayList<Component> kitLore = new ArrayList<>();
		kitLore.add(Component.text("An axe-wielder capable of", NamedTextColor.GRAY));
		kitLore.add(Component.text("instantly killing weak enemies", NamedTextColor.GRAY));
		kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
		kitLore.add(Component.empty());
		kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
		kitLore.add(Component.text("- Speed I", NamedTextColor.DARK_GREEN));
		kitLore.add(Component.text(" ", NamedTextColor.GRAY));
		kitLore.add(Component.text("Passive: ", NamedTextColor.DARK_GREEN));
		kitLore.add(Component.text("- Executes enemies that are below", NamedTextColor.GRAY));
		kitLore.add(Component.text("§a30% §7of their max health", NamedTextColor.GRAY));
		return kitLore;
	}
}
