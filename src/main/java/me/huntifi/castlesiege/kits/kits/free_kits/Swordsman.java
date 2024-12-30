package me.huntifi.castlesiege.kits.kits.free_kits;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The swordsman kit
 */
public class Swordsman extends FreeKit {
	private static final int health = 430;
	private static final double regen = 10.5;
	private static final double meleeDamage = 40.5;
	private static final int ladderCount = 4;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Swordsman() {
		super("Swordsman", health, regen, Material.IRON_SWORD);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();

		// Weapon
		es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
				Component.text("Iron Sword", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text("43 Melee Damage", NamedTextColor.DARK_GREEN)),
				null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
						Component.text("Iron Sword", NamedTextColor.GREEN),
						List.of(Component.empty(),
								Component.text("45 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
				0);

		// Chestplate
		es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
				Component.text("Iron Chestplate", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

		// Leggings
		es.legs = CSItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
				Component.text("Iron Leggings", NamedTextColor.GREEN),
				List.of(Component.empty(),
						Component.text(health + " HP", NamedTextColor.DARK_GREEN),
						Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

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
	}

	/**
	 * @return The lore to add to the kit gui item
	 */
	@Override
    public ArrayList<Component> getGuiDescription() {
		ArrayList<Component> kitLore = new ArrayList<>();
		kitLore.add(Component.text("Standard melee kit", NamedTextColor.GRAY));
		kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
		return kitLore;
	}
}
