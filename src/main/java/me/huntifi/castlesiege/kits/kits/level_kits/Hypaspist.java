package me.huntifi.castlesiege.kits.kits.level_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.LevelKit;
import me.huntifi.conwymc.data_types.Tuple;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Hypaspist extends LevelKit implements Listener {

    public static final HashMap<Player, Entity> tridents = new HashMap<>();
    private static final int health = 460;
    private static final double regen = 10.5;
    private static final double meleeDamage = 30;
    private static final double throwDamage = 72;
    private static final int ladderCount = 4;
    private static final int level = 20;
    private static int blockAmount = 10;
    private final ItemStack shield;

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
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
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

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = " impaled ";
    }

    /**
     * @param hypaspist the hypaspist to remove and give back the shield to.
     * This is to stop the hypaspist from blocking even when the cooldown is active.
     * Don't bother trying to change this or find another way, there is no other way provided by spigot.
     */
    public void tempRemoveShield(Player hypaspist) {
        hypaspist.getInventory().setItemInOffHand(null);
        hypaspist.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
        new BukkitRunnable() {
            @Override
            public void run() {
                hypaspist.getInventory().setItemInOffHand(shield);
            }
        }.runTaskLater(Main.plugin, 10);
    }

    /**
     * This is basically a shield cool-down mechanism/method.
     * @param shielder the hypaspist holding the shield.
     */
    public void shieldMechanism(Player shielder) {
        if (Objects.equals(Kit.equippedKits.get(shielder.getUniqueId()).name, name)) {
            if (shielder.isBlocking() && blockAmount != 0) {
                blockAmount--;
            } else if (shielder.isBlocking() && blockAmount <= 1) {
                shielder.setCooldown(Material.SHIELD, 300);
                tempRemoveShield(shielder);
                blockAmount = 10;
            }
        }
    }

    /**
     *
     * @param e when a hypaspist gets hit whilst blocking.
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
     * @param e when a hypaspist gets hit by projectiles whilst blocking.
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
     * @param e hypaspist tries to block whilst the cooldown is active.
     */
    @EventHandler
    public void shielding(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) && e.getPlayer().getInventory().getItemInMainHand().getType() != Material.TRIDENT) {
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
     * Set the arrow-damage of a ranger's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onTridentHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Trident &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Trident) e.getEntity()).setDamage(throwDamage);
            tridents.put((Player) e.getEntity().getShooter(), e.getEntity());
            if (e.getHitEntity() instanceof Player) {
                Player p = (Player) e.getHitEntity();
                p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 80, 4)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 2)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 60, 2)));
            }
        }
    }

    /**
     * Hypaspist's trident on the ground if there is one. Is removed on enderchest click
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        if (tridents.containsKey(event.getPlayer())) {
            tridents.get(event.getPlayer()).remove();
            tridents.remove(event.getPlayer());
        }
    }

    /**
     *
     * @param e if a player dies remove their trident and remove them from the list if they are contained in the list.
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (tridents.containsKey(e.getPlayer())) {
            tridents.get(e.getPlayer()).remove();
            tridents.remove(e.getPlayer());
        }
    }

    /**
     *
     * @param e if a player quits remove their trident and remove them from the list if they are contained in the list.
     */
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        if (tridents.containsKey(e.getPlayer())) {
            tridents.get(e.getPlayer()).remove();
            tridents.remove(e.getPlayer());
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
