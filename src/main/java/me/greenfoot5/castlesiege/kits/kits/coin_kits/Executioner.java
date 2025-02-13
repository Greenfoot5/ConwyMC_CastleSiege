package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
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

/**
 * The executioner kit
 */
public class Executioner extends CoinKit implements Listener {

	private static final int health = 248;
	private static final double regen = 12.5;
	private static final double meleeDamage = 42.5;
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
								Component.text("50 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
						Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
				0);
                
		// Chestplate
		es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
				Component.text("Leather Chestplate", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
				Color.fromRGB(32, 32, 32));
                
		// Leggings
		es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
				Component.text("Leather Leggings", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
				Color.fromRGB(32, 32, 32));
                
		// Boots
		es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				Component.text("Iron Boots", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
		// Voted Boots
		es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				Component.text("Iron Boots", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
						Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
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
		if (e.getEntity() instanceof Attributable attri && e.getDamager() == equippedPlayer) {
            Damageable hit = (Damageable) e.getEntity();

            // Executioner hits with axe
			if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {

				AttributeInstance healthAttribute = attri.getAttribute(Attribute.MAX_HEALTH);
				assert healthAttribute != null;

				// Execute
				if (hit.getHealth() < healthAttribute.getValue() * 0.3) {
					e.setCancelled(true);

					Location loc = hit.getLocation();
					// Do extra stuff if they were a player
					if (hit instanceof Player p) {
                        loc = p.getEyeLocation();

						Material wool = TeamController.getTeam(hit.getUniqueId()).primaryWool;
						hit.getWorld().dropItem(loc, new ItemStack(wool)).setPickupDelay(32767);
					}
					hit.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH, 1, 1);
					hit.damage(healthAttribute.getValue(), equippedPlayer);
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
