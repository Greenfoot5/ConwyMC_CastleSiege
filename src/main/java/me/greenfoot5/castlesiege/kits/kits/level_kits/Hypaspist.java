package me.greenfoot5.castlesiege.kits.kits.level_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.EnderchestEvent;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.LevelKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hypaspist Kit
 */
public class Hypaspist extends LevelKit implements Listener {

    private Entity trident;
    private static final int health = 460;
    private static final double regen = 10.5;
    private static final double meleeDamage = 30;
    private static final double throwDamage = 72;
    private static final int ladderCount = 4;
    private static final int level = 20;

    private final ItemStack shield;

    private static final int blockAmount = 10;
    private int blocksLeft = blockAmount;

    /**
     * Creates a new Hypaspist
     */
    public Hypaspist() {
        super("Hypaspist", health, regen, Material.GOLDEN_CHESTPLATE, level);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Short-sword", NamedTextColor.GREEN), List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN)), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Short-sword", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
                                Component.text("⁎ Voted: +2 damage", NamedTextColor.GREEN)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Shield
        shield = CSItemCreator.weapon(new ItemStack(Material.SHIELD, 1),
                Component.text("Concave Shield", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("- 10 DMG", NamedTextColor.DARK_GREEN),
                        Component.text("- Knockback I", NamedTextColor.DARK_GREEN),
                        Component.text("<< Right Click To Block >>", NamedTextColor.DARK_GRAY),
                        Component.text("Can block up to 10 times before", NamedTextColor.GRAY),
                        Component.text("the cooldown activates.", NamedTextColor.GRAY)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)) , 10);
        es.offhand = shield;

        // Weapon
        es.hotbar[1] = CSItemCreator.weapon(new ItemStack(Material.TRIDENT),
                Component.text("Sarissa", NamedTextColor.GREEN), null, null, meleeDamage);

        // Chestplate + trim
        es.chest = CSItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                Component.text("Copper Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(183, 12, 12));

        // Boots + trim
        es.feet = CSItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Copper Boots", NamedTextColor.GREEN), null, null);
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE);
        ((ArmorMeta) boots).setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);

        // Voted Boots + trim
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Copper Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta votedBoots = es.votedFeet.getItemMeta();
        ArmorMeta votedBootsMeta = (ArmorMeta) votedBoots;
        assert votedBoots != null;
        ArmorTrim votedBootsTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE);
        ((ArmorMeta) votedBoots).setTrim(votedBootsTrim);
        es.votedFeet.setItemMeta(votedBootsMeta);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = " impaled ";
    }

    /**
     * Stops player from blocking when the cooldown starts
     */
    public void tempRemoveShield() {
        equippedPlayer.getInventory().setItemInOffHand(null);
        equippedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
        new BukkitRunnable() {
            @Override
            public void run() {
                equippedPlayer.getInventory().setItemInOffHand(shield);
            }
        }.runTaskLater(Main.plugin, 10);
    }

    /**
     * Shield cool-down mechanism/method.
     */
    public void shieldMechanism() {
        if (equippedPlayer.isBlocking() && blocksLeft != 0) {
            blocksLeft--;
        } else if (equippedPlayer.isBlocking() && blocksLeft <= 1) {
            equippedPlayer.setCooldown(Material.SHIELD, 300);
            tempRemoveShield();
            blocksLeft = blockAmount;
        }
    }

    /**
     * Shield ability when player is hit by entity
     * @param e when a hypaspist gets hit whilst blocking.
     */
    @EventHandler
    public void combatShieldingEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() != equippedPlayer)
            return;

        shieldMechanism();
    }
    /**
     *Shield ability when player is hit by projectile
     * @param e when a hypaspist gets hit by projectiles whilst blocking.
     */
    @EventHandler
    public void combatShieldingProjectile(ProjectileHitEvent e) {
        if (e.getEntity() != equippedPlayer)
            return;

        shieldMechanism();
    }
    /**
     * Hypaspist activates items
     * @param e Hypaspist activates items
     */
    @EventHandler
    public void shielding(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (equippedPlayer.getCooldown(Material.SHIELD) != 0
                && equippedPlayer.getInventory().getItemInOffHand().getType() == Material.SHIELD
                && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            return;
        }


        if (equippedPlayer.getCooldown(Material.SHIELD) != 0
                && equippedPlayer.getInventory().getItemInMainHand().getType() == Material.SHIELD
                && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            return;
        }

        if (InCombat.isPlayerInCombat(e.getPlayer().getUniqueId()))
            return;

        blocksLeft = blockAmount;
    }

    /**
     * Set the arrow-damage of a ranger's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onTridentHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() != equippedPlayer)
            return;

        if (!(e.getEntity() instanceof Trident))
            return;

        ((Trident) e.getEntity()).setDamage(throwDamage);
        trident = e.getEntity();
        if (e.getHitEntity() instanceof Player p) {
            p.addPotionEffect((new PotionEffect(PotionEffectType.NAUSEA, 80, 4)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 60, 2)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 60, 2)));
        }
    }

    /**
     * Hypaspist's trident on the ground if there is one. Is removed on enderchest click
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        if (trident != null) {
            trident.remove();
        }
    }

    /**
     *
     * @param e if a player dies remove their trident and remove them from the list if they are contained in the list.
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (trident != null) {
            trident.remove();
        }
    }

    /**
     *
     * @param e if a player quits remove their trident and remove them from the list if they are contained in the list.
     */
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        if (trident != null) {
            trident.remove();
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A tank with a trident & shield, ", NamedTextColor.GRAY));
        kitLore.add(Component.text("which weakens opponents", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, throwDamage, ladderCount, -1));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slowness I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Mining fatigue I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Trident-throw inflicts confusion IV,", NamedTextColor.GRAY));
        kitLore.add(Component.text("Mining Fatigue III and Slowness II", NamedTextColor.GRAY));
        kitLore.add(Component.text("on hit opponents", NamedTextColor.GRAY));
        kitLore.add(Component.text("On Shield Cooldown:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Speed II (0:08)", NamedTextColor.GRAY));
        return kitLore;
    }
}
