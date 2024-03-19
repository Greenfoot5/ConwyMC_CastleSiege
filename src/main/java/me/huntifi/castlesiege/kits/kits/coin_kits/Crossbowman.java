package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The crossbowman kit
 */
public class Crossbowman extends CoinKit implements Listener {

    private static final int health = 270;
    private static final double regen = 9;
    private static final int ladderCount = 4;
    private static final int arrowCount = 48;

    /**
     * Set the equipment and attributes of this kit
     */

    public static final HashMap<UUID, Boolean> snipers = new HashMap<>();

    private final ItemStack mobilitySwitchInactive;
    private final ItemStack sniperSwitchInactive;

    private final ItemStack mobilitySwitchActive;
    private final ItemStack sniperSwitchActive;

    public Crossbowman() {
        super("Crossbowman", health, regen, Material.CROSSBOW);
        super.kbResistance = 1;
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Crossbow
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                Component.text("Crossbow", NamedTextColor.GREEN), null, null, 1);
        es.votedWeapon = new Tuple<>(ItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                Component.text("Crossbow", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 3), 0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 51));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 51));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(255, 255, 51));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // mode switch button
        mobilitySwitchInactive = ItemCreator.weapon(new ItemStack(Material.LIME_DYE),
                Component.text("Mobility Mode", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right-click to enable Mobility Mode", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Mobility mode:", NamedTextColor.AQUA),
                        Component.text("In this mode crossbowman is faster, ", NamedTextColor.AQUA),
                        Component.text("has no cooldown and shoots like a normal", NamedTextColor.AQUA),
                        Component.text("crossbow.", NamedTextColor.AQUA)),
                null, 0);

        sniperSwitchInactive = ItemCreator.weapon(new ItemStack(Material.YELLOW_DYE),
                Component.text("Sniper Mode", NamedTextColor.YELLOW),
                Arrays.asList(Component.empty(),
                        Component.text("Right-click to enable Sniper Mode", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Sniper mode:", NamedTextColor.AQUA),
                        Component.text("In this mode crossbowman is slower, ", NamedTextColor.AQUA),
                        Component.text("has a cooldown and shoots like a sniper", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 0);

        // mode switch button
        mobilitySwitchActive = ItemCreator.weapon(new ItemStack(Material.LIME_DYE),
                Component.text("Mobility Mode", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right-click to enable Mobility Mode", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Mobility mode:", NamedTextColor.AQUA),
                        Component.text("In this mode crossbowman is faster, ", NamedTextColor.AQUA),
                        Component.text("has no cooldown and shoots like a normal", NamedTextColor.AQUA),
                        Component.text("crossbow.", NamedTextColor.AQUA)),
                null, 0);

        sniperSwitchActive = ItemCreator.weapon(new ItemStack(Material.YELLOW_DYE),
                Component.text("Sniper Mode", NamedTextColor.YELLOW),
                Arrays.asList(Component.empty(),
                        Component.text("Right-click to enable Sniper Mode", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Sniper mode:", NamedTextColor.AQUA),
                        Component.text("In this mode crossbowman is slower, ", NamedTextColor.AQUA),
                        Component.text("has a cooldown and shoots like a sniper", NamedTextColor.AQUA)),
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
                if ((effect.getType().equals(PotionEffectType.SLOW) && effect.getAmplifier() == 2)) {
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
                if ((effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() == 0)) {
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

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A ranged kit that can function", NamedTextColor.GRAY));
        kitLore.add(Component.text("like a sniper", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, 1, 36, ladderCount, arrowCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slowness III ", NamedTextColor.GRAY)
                .append(Component.text("(sniper mode)", NamedTextColor.GREEN)));
        kitLore.add(Component.text("- Speed I ", NamedTextColor.GRAY)
                .append(Component.text("(mobility move)", NamedTextColor.YELLOW)));
        kitLore.add(Component.text(" ", NamedTextColor.GRAY));
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Has two movement modes", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive: ", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Arrows are not affected by", NamedTextColor.GRAY));
        kitLore.add(Component.text("gravity ", NamedTextColor.GRAY)
                .append(Component.text("(sniper mode)", NamedTextColor.GREEN)));
        return kitLore;
    }
}
