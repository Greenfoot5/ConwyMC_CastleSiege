package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
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
import java.util.List;

/**
 * A kit that deals more damage as it gains more health
 */
public class Barbarian extends CoinKit implements Listener {

    private static final int health = 260;
    private static final double regen = 10.5;
    private static final double minDamage = 36;
    private static final double maxDamage = 70;
    private static final int ladderCount = 4;

    /**
     * Creates a new barbarian
     */
    public Barbarian() {
        super("Barbarian", health, regen, Material.NETHERITE_AXE);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                Component.text("Battle Axe", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(minDamage + " - " + maxDamage + " Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("<< Passive Ability >>", NamedTextColor.DARK_GRAY),
                        Component.text("Damage done increases with lower health.", NamedTextColor.GRAY)
                ), null, minDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        Component.text("Battle Axe", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text((minDamage + 2) + " - " + (maxDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
                                Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GREEN),
                                Component.text("<< Passive Ability >>", NamedTextColor.DARK_GRAY),
                                Component.text("Damage done increases with lower health.", NamedTextColor.GRAY)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), minDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN),
                List.of(Component.text("» Copper Tide Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(209, 112, 0));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.text("» Copper Tide Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(209, 112, 0));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.text("» Netherite Rib Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.text("» Netherite Rib Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
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
        if (e.getDamager() instanceof Player attacker) {
            // Barbarian increased damage
            if (attacker == equippedPlayer && equippedPlayer.getInventory().getItemInMainHand().getType() == Material.NETHERITE_AXE) {
                double damageMult = 1 - attacker.getHealth() / health;
                double dmg = ((maxDamage - e.getDamage()) * damageMult) + e.getDamage();
                e.setDamage(dmg);
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
        kitLore.addAll(getBaseStats(health, regen, minDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Deal up to ", NamedTextColor.GRAY)
                .append(Component.text(maxDamage - minDamage, NamedTextColor.GREEN))
                .append(Component.text(" additional DMG")));
        kitLore.add(Component.text("based on health lost"));
        return kitLore;
    }
}
