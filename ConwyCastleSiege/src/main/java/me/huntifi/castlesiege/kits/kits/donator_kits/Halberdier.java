package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;

/**
 * The halberdier kit
 */
public class Halberdier extends DonatorKit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */
    public Halberdier() {
        super("Halberdier", 484, 23, 8500);
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.DIAMOND_AXE),
                ChatColor.GREEN + "Halberd", null, null, 58);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.DIAMOND_AXE),
                        ChatColor.GREEN + "Halberd",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 60),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                ChatColor.GREEN + "Indestructible Chestplate", null,
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_ENVIRONMENTAL, 2)));

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 2));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were sliced in half by ";
        super.killMessage[0] = " sliced ";
        super.killMessage[1] = " in half";
    }

    /**
     * Activate the halberdier ability of dealing +50% damage to cavalry
     * @param e The event called when dealing damage to another player
     */
    @EventHandler (priority = EventPriority.HIGH)
    public void antiCav(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player cav = (Player) e.getEntity();
            Player halb = (Player) e.getDamager();

            if (Objects.equals(Kit.equippedKits.get(halb.getUniqueId()).name, name) &&
                    cav.isInsideVehicle() && cav.getVehicle() instanceof Horse) {
                e.setDamage(e.getDamage() * 1.5);
            }
        }
    }

    /**
     * The cooldown event for halberdier.
     * @param e The event called when dealing damage to another player
     */
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void halbCooldown(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name)) {
            Player halb = (Player) e.getDamager();

            if (halb.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                if (halb.getCooldown(Material.DIAMOND_AXE) == 0) {
                    halb.setCooldown(Material.DIAMOND_AXE, e.getEntity() instanceof Player ? 20 : 10);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * Activate the halberdier ability of taking +50% damage from arrows
     * @param e The event called when taking damage from an arrow
     */
    @EventHandler (priority = EventPriority.HIGH)
    public void weakToArrows(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow &&
                Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
            e.setDamage(e.getDamage() * 2);
        }
    }
}
