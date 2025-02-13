package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.events.EnderchestEvent;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.conwymc.data_types.Tuple;
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
import java.util.List;

/**
 * A kit that can fire arrows that don't fall
 */
public class Crossbowman extends CoinKit implements Listener {

    private static final int health = 270;
    private static final double regen = 9;
    private static final int ladderCount = 4;
    private static final int arrowCount = 48;

    /**
     * Set the equipment and attributes of this kit
     */

    private boolean sniping = false;

    private final ItemStack mobilitySwitch;
    private final ItemStack sniperSwitch;

    /**
     * Creates a new crossbowman
     */
    public Crossbowman() {
        super("Crossbowman", health, regen, Material.CROSSBOW);
        super.kbResistance = 1;
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Crossbow
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                Component.text("Crossbow", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("1 Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("18 Ranged Damage", NamedTextColor.DARK_GREEN)),
                null, 1);
        es.votedWeapon = new Tuple<>(CSItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                Component.text("Crossbow", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("3 Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA),
                        Component.text("18 Ranged Damage", NamedTextColor.DARK_GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 3), 0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(255, 255, 51));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(255, 255, 51));
        // Voted boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(255, 255, 51));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // mode switch button
        mobilitySwitch = CSItemCreator.weapon(new ItemStack(Material.LIME_DYE),
                Component.text("Mobility Mode", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right-click to enable Mobility Mode", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Mobility mode:", NamedTextColor.AQUA),
                        Component.text("In this mode crossbowman is faster, ", NamedTextColor.AQUA),
                        Component.text("has no cooldown and shoots like a normal", NamedTextColor.AQUA),
                        Component.text("crossbow.", NamedTextColor.AQUA)),
                null, 0);

        sniperSwitch = CSItemCreator.weapon(new ItemStack(Material.YELLOW_DYE),
                Component.text("Sniper Mode", NamedTextColor.YELLOW),
                Arrays.asList(Component.empty(),
                        Component.text("Right-click to enable Sniper Mode", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Sniper mode:", NamedTextColor.AQUA),
                        Component.text("In this mode crossbowman is slower, ", NamedTextColor.AQUA),
                        Component.text("has a cooldown and shoots like a sniper", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 0);

        es.hotbar[6] = sniperSwitch;

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
        if (e.isCancelled() || e.getEntity() != equippedPlayer || !sniping) {
            return;
        }

        if (!(e.getProjectile() instanceof Arrow a)) {
            return;
        }

        equippedPlayer.setCooldown(Material.CROSSBOW, 70);
        //((Arrow) e.getProjectile()).setPierceLevel(1);
        a.setKnockbackStrength(1);
        a.setVelocity(equippedPlayer.getLocation().getDirection().normalize().multiply(40));
    }

    /**
     * Set the arrow-damage of a crossbow's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow
                && e.getEntity().getShooter() == equippedPlayer) {
            if (!sniping) {
                ((Arrow) e.getEntity()).setDamage(18);
            }
        }
    }

    /**
     * Crossbow is put into mobility mode when they click an enderchest.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        if (event.getPlayer() == equippedPlayer)
            mobilityMode();
    }


    /**
     * Handles clicking the green dye
     * @param p The player clicking the dye
     */
    private void onClickGreenDye(Player p) {
        // mode switch button
        int cooldown = p.getCooldown(Material.LIME_DYE);
        if (cooldown == 0 && sniping) {
            mobilityMode();
        }
    }

    /**
     * Handles clicking the yellow dye
     * @param p The player clicking the dye
     */
    private void onClickYellowDye(Player p) {
        int cooldown = p.getCooldown(Material.YELLOW_DYE);
        if (cooldown == 0 && !sniping) {
            snipingMode();
        }
    }

    /**
     * Sets the player to mobility mode
     */
    private void mobilityMode() {
        sniping = false;

        equippedPlayer.setCooldown(Material.LIME_DYE, 200);
        equippedPlayer.setCooldown(Material.YELLOW_DYE, 200);
        equippedPlayer.getInventory().setItem(6, sniperSwitch);

        // Potion effects when Mobility Mode
        for (PotionEffect effect : equippedPlayer.getActivePotionEffects()) {
            if ((effect.getType().equals(PotionEffectType.SLOWNESS) && effect.getAmplifier() == 2)) {
                equippedPlayer.removePotionEffect(effect.getType());
                this.potionEffects.remove(effect.getType());
            }
        }
        equippedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
    }

    /**
     * Sets the player to sniping mode
     */
    private void snipingMode() {
        sniping = true;

        equippedPlayer.setCooldown(Material.LIME_DYE, 200);
        equippedPlayer.setCooldown(Material.YELLOW_DYE, 200);
        equippedPlayer.getInventory().setItem(6, mobilitySwitch);

        // Potion effects when Sniper Mode
        for (PotionEffect effect : equippedPlayer.getActivePotionEffects()) {
            if ((effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() == 0)) {
                equippedPlayer.removePotionEffect(effect.getType());
                this.potionEffects.remove(effect.getType());
            }
        }
        equippedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 2));
        sniping = true;
    }


    /**
     *
     * @param e event triggered by right-clicking mode switch button.
     */
    @EventHandler
    public void onModeSwitch(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (equippedPlayer.getInventory().getItemInMainHand().getType().equals(Material.LIME_DYE)) {
                mobilityMode();
            }
            else if (equippedPlayer.getInventory().getItemInMainHand().getType().equals(Material.YELLOW_DYE)) {
                snipingMode();
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
        kitLore.addAll(getBaseStats(health, regen, 1, 18, ladderCount, arrowCount));
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
