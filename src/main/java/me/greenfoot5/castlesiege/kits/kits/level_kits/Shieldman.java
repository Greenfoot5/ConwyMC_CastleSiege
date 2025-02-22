package me.greenfoot5.castlesiege.kits.kits.level_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.LevelKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The shieldman kit
 */
public class Shieldman extends LevelKit implements Listener {

    private static final int health = 400;
    private static final double regen = 14;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 4;
    private static final int blockAmount = 12;
    private final ItemStack shield;

    private int blocksRemaining = 12;

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
                Component.text("Longsword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN)), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Longsword", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
                                Component.text("⁎ Voted: +2 damage", NamedTextColor.GREEN)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Shield
        shield = CSItemCreator.weapon(new ItemStack(Material.SHIELD, 1),
                Component.text("Shield", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("- 10 DMG", NamedTextColor.DARK_GREEN),
                        Component.text("- Knockback I", NamedTextColor.DARK_GREEN),
                        Component.text("<< Right Click To Block >>", NamedTextColor.DARK_GRAY),
                        Component.text("Can block up to 12 times before", NamedTextColor.GRAY),
                        Component.text("the cooldown activates.", NamedTextColor.GRAY)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)) , 10);
        es.offhand = shield;

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Diamond Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Diamond Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 0));
    }

    /**
     * This is to stop the shieldman from blocking even when the cooldown is active.
     */
    public void tempRemoveShield() {
        equippedPlayer.getInventory().setItemInOffHand(null);
        new BukkitRunnable() {
            @Override
            public void run() {
                equippedPlayer.getInventory().setItemInOffHand(shield);
            }
        }.runTaskLater(Main.plugin, 10);
    }

    /**
     * Allows a certain number of blocks before cooldown
     */
    public void shieldMechanism() {
            if (equippedPlayer.isBlocking() && blocksRemaining != 0) {
                blocksRemaining--;
            } else if (equippedPlayer.isBlocking() && blocksRemaining <= 1) {
                equippedPlayer.setCooldown(Material.SHIELD, 300);
                tempRemoveShield();
                blocksRemaining = blockAmount;
            }
    }

    /**
     *
     * @param e when a shieldman gets hit whilst blocking.
     */
    @EventHandler
    public void combatShielding(EntityDamageByEntityEvent e) {
        if (e.getEntity() == equippedPlayer) {
            shieldMechanism();
        }
    }
    /**
     *
     * @param e when a shieldman gets hit by projectiles whilst blocking.
     */
    @EventHandler
    public void combatShielding2(ProjectileHitEvent e) {
        if (e.getEntity() == equippedPlayer) {
            shieldMechanism();
        }
    }
    /**
     *
     * @param e shieldman tries to block whilst the cooldown is active.
     */
    @EventHandler
    public void shielding(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        if (equippedPlayer.getInventory().getItemInOffHand().getType() == Material.SHIELD)
            return;

        if (equippedPlayer.getCooldown(Material.SHIELD) != 0) {
            e.setCancelled(true);
        }

        blocksRemaining = blockAmount;
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
