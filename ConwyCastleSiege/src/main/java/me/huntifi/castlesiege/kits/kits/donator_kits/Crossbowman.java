package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.combat.InCombat;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * The crossbowman kit
 */
public class Crossbowman extends DonatorKit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */

    public static HashMap<UUID, Boolean> snipers = new HashMap<>();

    private final ItemStack mobilitySwitchInactive;
    private final ItemStack sniperSwitchInactive;

    private final ItemStack mobilitySwitchActive;
    private final ItemStack sniperSwitchActive;

    public Crossbowman() {
        super("Crossbowman", 250, 8.5, 10000, 12, Material.CROSSBOW);
        super.kbResistance = 1;
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

        // mode swich button
        mobilitySwitchInactive = ItemCreator.weapon(new ItemStack(Material.LIME_DYE),
                ChatColor.GREEN + "Mobility Mode", Arrays.asList("",
                        ChatColor.AQUA + "Mobility mode:",
                        ChatColor.AQUA + "In this mode crossbowman is faster, ",
                        ChatColor.AQUA + "has no cooldown and shoots like a normal",
                        ChatColor.AQUA + "crossbow.", "",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to enable Mobility Mode."),
                null, 0);

        sniperSwitchInactive = ItemCreator.weapon(new ItemStack(Material.YELLOW_DYE),
                ChatColor.YELLOW + "Sniper Mode", Arrays.asList("",
                        ChatColor.AQUA + "Sniper mode:",
                        ChatColor.AQUA + "In this mode crossbowman is slower, ",
                        ChatColor.AQUA + "has a cooldown and shoots like a sniper",
                        ChatColor.AQUA + "",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to enable Sniper Mode."),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 0);

        // mode swich button
        mobilitySwitchActive = ItemCreator.weapon(new ItemStack(Material.LIME_DYE),
                ChatColor.GREEN + "Mobility Mode", Arrays.asList("",
                        ChatColor.AQUA + "Mobility mode:",
                        ChatColor.AQUA + "In this mode crossbowman is faster, ",
                        ChatColor.AQUA + "has no cooldown and shoots like a normal",
                        ChatColor.AQUA + "crossbow.", "",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to enable Mobility Mode."),
                null, 0);

        sniperSwitchActive = ItemCreator.weapon(new ItemStack(Material.YELLOW_DYE),
                ChatColor.YELLOW + "Sniper Mode", Arrays.asList("",
                        ChatColor.AQUA + "Sniper mode:",
                        ChatColor.AQUA + "In this mode crossbowman is slower, ",
                        ChatColor.AQUA + "has a cooldown and shoots like a sniper",
                        ChatColor.AQUA + "",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to enable Sniper Mode."),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 0);

        es.hotbar[6] = mobilitySwitchActive;
        es.hotbar[7] = sniperSwitchInactive;

        // Arrows
        es.hotbar[5] = new ItemStack(Material.ARROW, 48);

        super.equipment = es;

        // Potion effects when Mobility Mode
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

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
        //Multiple checks including if the crossbow is in sniper mode.
        if (e.isCancelled() || !(e.getEntity() instanceof Player) || !isInSnipingMode(e.getEntity().getUniqueId())) {
            return;
        }

        Player p = (Player) e.getEntity();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {

            if (!(e.getProjectile() instanceof Arrow)) {
                return;
            }

            p.setCooldown(Material.CROSSBOW, 70);
            Arrow a = (Arrow) e.getProjectile();
            //((Arrow) e.getProjectile()).setPierceLevel(1);
            a.setKnockbackStrength(1);
            a.setVelocity(p.getLocation().getDirection().normalize().multiply(40));
        }
    }

    /**
     * Crossbow is put into mobility mode when they click an enderchest.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
            snipers.put(event.getPlayer().getUniqueId(), false);
    }

    /**
     *
     * @param uuid the uuid of the player to check
     * @return true if the player is in snipe mode, false if they aren't.
     */
    public boolean isInSnipingMode(UUID uuid) {
        if (snipers.get(uuid) == null) {
            return false;
        } else return snipers.get(uuid);
    }

    /**
     * Set the arrow-damage of a ranger's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler (priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            if (!isInSnipingMode(((Player) e.getEntity().getShooter()).getUniqueId())) {
                ((Arrow) e.getEntity()).setDamage(18);
            }
        }
    }


    public void onClickGreenDye(Player p) {
        // mode switch button
        int cooldown = p.getCooldown(Material.LIME_DYE);
        if (cooldown == 0 && isInSnipingMode(p.getUniqueId())) {
            p.setCooldown(Material.LIME_DYE, 200);
            p.setCooldown(Material.YELLOW_DYE, 200);
            p.getInventory().setItem(6, mobilitySwitchActive);
            p.getInventory().setItem(7, sniperSwitchInactive);
            // Potion effects when Mobility Mode
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if ((effect.getType().getName().equals(PotionEffectType.SLOW.getName()) && effect.getAmplifier() == 2)) {
                    p.removePotionEffect(effect.getType());
                    this.potionEffects.remove(effect.getType());
                }
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
            snipers.put(p.getUniqueId(), false);
        }
    }

    public void onClickYellowDye(Player p) {
        int cooldown = p.getCooldown(Material.YELLOW_DYE);
        if (cooldown == 0 && !isInSnipingMode(p.getUniqueId())) {
            p.setCooldown(Material.LIME_DYE, 200);
            p.setCooldown(Material.YELLOW_DYE, 200);
            p.getInventory().setItem(6, mobilitySwitchInactive);
            p.getInventory().setItem(7, sniperSwitchActive);
            // Potion effects when Sniper Mode
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if ((effect.getType().getName().equals(PotionEffectType.SPEED.getName()) && effect.getAmplifier() == 0)) {
                    p.removePotionEffect(effect.getType());
                    this.potionEffects.remove(effect.getType());
                }
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 2));
            snipers.put(p.getUniqueId(), true);
        }
    }


    /**
     *
     * @param e event triggered by right-clicking mode switch button.
     */
    @EventHandler
    public void onModeSwitch(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (p.getInventory().getItemInMainHand().getType().equals(Material.LIME_DYE)) {
                    onClickGreenDye(p);
                }
                if (p.getInventory().getItemInMainHand().getType().equals(Material.YELLOW_DYE)) {
                    onClickYellowDye(p);
                }
            }
        }
    }
}
