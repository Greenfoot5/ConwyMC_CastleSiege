package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * The halberdier kit
 */
public class Halberdier extends CoinKit implements Listener {

    private static final int health = 630;
    private static final double regen = 23;
    private static final double meleeDamage = 90;

    /**
     * Set the equipment and attributes of this kit
     */
    public Halberdier() {
        super("Halberdier", 630, 23, Material.NETHERITE_CHESTPLATE);
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.DIAMOND_AXE),
                Component.text("Halberd", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.DIAMOND_AXE),
                        Component.text("Halberd", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                Component.text("Indestructible Chestplate", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_ENVIRONMENTAL, 2)));

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
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


    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Our classic tank capable of", NamedTextColor.GRAY));
        kitLore.add(Component.text("taking a large beating", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 0));
        kitLore.add(Component.text(" ", NamedTextColor.GRAY));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slowness III", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Mining Fatigue II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Resistance I", NamedTextColor.GRAY));
        kitLore.add(Component.text(" ", NamedTextColor.GRAY));
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Cannot run", NamedTextColor.RED));
        kitLore.add(Component.text("- Has a slower attack speed", NamedTextColor.RED));
        kitLore.add(Component.text("- Halberdier takes 50% more DMG from arrows", NamedTextColor.RED));
        return kitLore;
    }
}
