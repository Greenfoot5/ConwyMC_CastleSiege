package me.huntifi.castlesiege.kits.kits.level_kits;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.LevelKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The shieldman kit
 */
public class Shieldman extends LevelKit implements Listener {

    private static final int health = 400;
    private static final double regen = 14;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 4;
    private static int blockAmount = 8;

    /**
     * Set the equipment and attributes of this kit
     */
    public Shieldman() {
        super("Shieldman", health, regen, Material.SHIELD, 2);
        super.kbResistance = 0.5;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Longsword", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Longsword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Shield
        es.offhand = CSItemCreator.item(new ItemStack(Material.SHIELD),
                Component.text("Shield", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)));

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Diamond Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Diamond Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0));
    }

    @EventHandler
    public void combatShielding(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.isBlocking() && blockAmount != 0) {
                blockAmount--;
            } else if (p.isBlocking() && blockAmount <= 0) {
                p.setCooldown(Material.SHIELD, 100);
                blockAmount = 8;
            }
        }
    }

    @EventHandler
    public void shielding(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            Player p = e.getPlayer();
            if (p.getCooldown(Material.SHIELD) != 0 &&
                    (p.getInventory().getItemInMainHand().getType() == Material.SHIELD || p.getInventory().getItemInOffHand().getType() == Material.SHIELD)) {
                e.setCancelled(true);
            }
            if (!InCombat.isPlayerInCombat(e.getPlayer().getUniqueId())) {
                blockAmount = 8;
            }
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Basic tank equipped with a", NamedTextColor.GRAY));
        kitLore.add(Component.text("sword and shield", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Mining Fatigue I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Slowness I", NamedTextColor.GRAY));
        return kitLore;
    }
}
