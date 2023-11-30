package me.huntifi.castlesiege.kits.kits.team_kits.conwy;


import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.*;
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

public class ConwyArbalester extends TeamKit implements Listener {

    public ConwyArbalester() {
        super("Arbalester", 260, 3, "Conwy", "The English",
                5000, Material.CROSSBOW, new Location(Bukkit.getWorld("Conwy"), 106, 88, 619));


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Crossbow
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "Crossbow", null, null
                , 3);
        es.votedWeapon = new Tuple<>(ItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "Crossbow",
                Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                Collections.singletonList(new Tuple<>(Enchantment.ARROW_KNOCKBACK, 2)), 5), 0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(174, 26, 26));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(174, 23, 51));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(174, 26, 26));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 24);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 3));

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

            p.setCooldown(Material.CROSSBOW, 160);
            Arrow a = (Arrow) e.getProjectile();
            ((Arrow) e.getProjectile()).setPierceLevel(1);
            a.setKnockbackStrength(2);
            a.setVelocity(p.getLocation().getDirection().normalize().multiply(45));
        }
    }

}
