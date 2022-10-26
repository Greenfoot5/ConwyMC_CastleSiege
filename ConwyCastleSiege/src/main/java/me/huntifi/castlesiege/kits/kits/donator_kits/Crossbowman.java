package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;

/**
 * The crossbowman kit
 */
public class Crossbowman extends DonatorKit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */
    public Crossbowman() {
        super("Crossbowman", 250, 8.5, 10000, 10);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Crossbow
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "Crossbow", null, null, 1);
        es.votedWeapon = new Tuple<>(ItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "Crossbow",
                Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 3), 0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(255, 255, 51));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(255, 255, 51));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(255, 255, 51));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 48);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));

        // Death Messages
        super.projectileDeathMessage[0] = "Your skull was pierced by ";
        super.projectileDeathMessage[1] = "'s bolt";
        super.projectileKillMessage[0] = " pierced ";
        super.projectileKillMessage[1] = "'s skull";
    }

    /**
     * Activate the crossbowman ability, shooting an arrow in the direction the player is looking at high speed
     * @param e The even called when shooting an arrow from a crossbow
     */
    @EventHandler
    public void shootCrossbow(EntityShootBowEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {

            if (!(e.getProjectile() instanceof Arrow)) {
                return;
            }

            p.setCooldown(Material.CROSSBOW, 55);
            Arrow a = (Arrow) e.getProjectile();
            ((Arrow) e.getProjectile()).setPierceLevel(1);
            a.setKnockbackStrength(1);
            a.setVelocity(p.getLocation().getDirection().normalize().multiply(40));
        }
    }
}
