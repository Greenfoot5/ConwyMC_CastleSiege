package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 */
public class Barbarian extends CoinKit implements Listener {

    private static final int health = 260;
    private static final double regen = 10.5;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 4;

    public Barbarian() {
        super("Barbarian", health, regen, Material.NETHERITE_AXE);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                Component.text("Battle Axe", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        Component.text("Battle Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),,
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(209, 112, 0));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(209, 112, 0));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.RIB);
        bootsMeta.setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);
        es.votedFeet.setItemMeta(bootsMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0, true, false));

    }


    /**
     * Calculate how much damage should be increased by
     * @param e The entity hit
     */
    @EventHandler
    public void onIncreasedDamage(EntityDamageByEntityEvent e) {
        // Both are players
        if (e.getEntity() instanceof Attributable && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();


            double potentialDamage = health / attacker.getHealth();

            // Barbarian increased damage
            if (Objects.equals(Kit.equippedKits.get(attacker.getUniqueId()).name, name)) {
                if (potentialDamage < 3) {
                    e.setDamage(meleeDamage * potentialDamage);
                } else {
                    e.setDamage(meleeDamage * 3);
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
        kitLore.add(Component.text("Fast axe wielding warrior that deals", NamedTextColor.GRAY));
        kitLore.add(Component.text("more damage when they are low on health", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Melee damage is increased depending", NamedTextColor.GRAY));
        kitLore.add(Component.text("on how much health you have left.", NamedTextColor.GRAY));
        kitLore.add(Component.text("Maximum damage dealt is ", NamedTextColor.GRAY)
                .append(Component.text(meleeDamage * 3, NamedTextColor.GREEN))
                .append(Component.text(" DMG", NamedTextColor.GRAY)));
        return kitLore;
    }
}
