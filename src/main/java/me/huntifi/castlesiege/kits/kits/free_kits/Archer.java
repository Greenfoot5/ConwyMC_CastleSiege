package me.huntifi.castlesiege.kits.kits.free_kits;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The archer kit
 */
public class Archer extends FreeKit implements Listener {
	private static final int health = 210;
	private static final double regen = 10.5;
	private static final double meleeDamage = 34;
	private static final int ladderCount = 4;
	private static final int arrowCount = 35;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Archer() {
		super("Archer", health, regen, Material.BOW);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();

		// Weapon
		es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
				Component.text("Dagger", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text("34 Melee Damage", NamedTextColor.DARK_GREEN)),
				null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
				Component.text("Dagger", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text("36 Melee Damage", NamedTextColor.DARK_GREEN),
						Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
				Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
				0);

		// Chestplate
		es.chest = CSItemCreator.item(new ItemStack(Material.LEATHER_CHESTPLATE),
				Component.text("Leather Tunic", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

		// Leggings
		es.legs = CSItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
				Component.text("Leather Leggings", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

		// Boots
		es.feet = CSItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
				Component.text("Leather Boots", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
		// Voted Boots
		es.votedFeet = CSItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
				Component.text("Leather Boots", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
						Component.empty(),
						Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

		// Arrows
		es.hotbar[7] = new ItemStack(Material.ARROW, arrowCount);

		// Bow
		es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.BOW),
				Component.text("Bow", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text("20.5 Ranged Damage", NamedTextColor.DARK_GREEN)),
				null);

		super.equipment = es;
	}

	/**
	 * Set the arrow-damage of a ranger's arrows
	 * @param e The event called when a player is hit by an arrow
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onArrowHit(ProjectileHitEvent e) {
		if (!(e.getEntity() instanceof AbstractArrow) ||
				!(e.getEntity().getShooter() instanceof Player))
			return;

		if (!Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name))
			return;

		((AbstractArrow) e.getEntity()).setDamage(20.5);
	}

	/**
	 * @return The lore to add to the kit gui item
	 */
	@Override
    public ArrayList<Component> getGuiDescription() {
		ArrayList<Component> kitLore = new ArrayList<>();
		kitLore.add(Component.text("Standard ranged kit", NamedTextColor.GRAY));
		kitLore.addAll(getBaseStats(health, regen, meleeDamage, 20.5, ladderCount, arrowCount));
		return kitLore;
	}
}
