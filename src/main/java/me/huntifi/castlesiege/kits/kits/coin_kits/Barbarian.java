package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.ChatColor;
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
                ChatColor.GREEN + "Battle Axe", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        ChatColor.GREEN + "Battle Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(209, 112, 0));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(209, 112, 0));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
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
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7Fast axe wielding warrior that deals");
        kitLore.add("§7more damage when they are low on health");
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(" ");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Speed I");
        kitLore.add(" ");
        kitLore.add("§2Passive:");
        kitLore.add("§7- Melee damage is increased depending");
        kitLore.add("§7on how much health you have left.");
        kitLore.add("§7Maximum damage dealt is §a" + meleeDamage * 3 + " §7DMG");
        return kitLore;
    }
}
