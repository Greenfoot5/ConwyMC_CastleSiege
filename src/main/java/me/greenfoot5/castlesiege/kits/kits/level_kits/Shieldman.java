package me.greenfoot5.castlesiege.kits.kits.level_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.LevelKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
    private static int blockAmount = 12;
    private final ItemStack shield;

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
     * @param shieldman the shieldman to remove and give back the shield to.
     * This is to stop the shieldman from blocking even when the cooldown is active.
     * Don't bother trying to change this or find another way, there is no other way provided by spigot.
     */
    public void tempRemoveShield(Player shieldman) {
        shieldman.getInventory().setItemInOffHand(null);
        new BukkitRunnable() {
            @Override
            public void run() {
                shieldman.getInventory().setItemInOffHand(shield);
            }
        }.runTaskLater(Main.plugin, 10);
    }

    /**
     * This is basically a shield cool-down mechanism/method.
     * @param shielder the shieldman holding the shield.
     */
    public void shieldMechanism(Player shielder) {
        if (Objects.equals(Kit.equippedKits.get(shielder.getUniqueId()).name, name)) {
            if (shielder.isBlocking() && blockAmount != 0) {
                blockAmount--;
            } else if (shielder.isBlocking() && blockAmount <= 1) {
                shielder.setCooldown(Material.SHIELD, 300);
                tempRemoveShield(shielder);
                blockAmount = 12;
            }
        }
    }

    /**
     *
     * @param e when a shieldman gets hit whilst blocking.
     */
    @EventHandler
    public void combatShielding(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
                Player p = (Player) e.getEntity();
                shieldMechanism(p);
            }
        }
    }
    /**
     *
     * @param e when a shieldman gets hit by projectiles whilst blocking.
     */
    @EventHandler
    public void combatShielding2(ProjectileHitEvent e) {
        if (e.getHitEntity() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getHitEntity().getUniqueId()).name, name)) {
                Player p = (Player) e.getHitEntity();
                shieldMechanism(p);
            }
        }
    }
    /**
     *
     * @param e shieldman tries to block whilst the cooldown is active.
     */
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
                blockAmount = 12;
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
