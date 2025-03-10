package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Windlancer kit
 */
public class Windlancer extends SignKit implements Listener {

    /**
     * Creates a new Windlancer
     */
    public Windlancer() {
        super("Windlancer", 300, 9 , Material.STICK, 5000);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STICK, 20),
                Component.text("Small Hand-Windlass Spear", NamedTextColor.GREEN), null, null, 29);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STICK, 20),
                        Component.text("Small Hand-Windlass Spear", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 31),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(218, 211, 183));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(218, 183, 183));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were impaled by ";
        super.killMessage[0] = " impaled ";

        super.equipment = es;
    }

    /**
     * Activate the windlancer ability
     * @param e The event called when interacting
     */
    @EventHandler
    public void throwSpear(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack stick = equippedPlayer.getInventory().getItemInMainHand();
        if (!stick.getType().equals(Material.STICK))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (equippedPlayer.getCooldown(Material.STICK) == 0) {
            Messenger.sendActionInfo("You threw your spear-burst!", equippedPlayer);
            shootSpearBurst();
        } else {
            Messenger.sendActionError("You can't use spear-burst yet!", equippedPlayer);
        }
    }

    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW)
    public void changeSpearDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.getShooter() != equippedPlayer)
                return;

            arrow.setDamage(20);
        }
    }

    private void shootSpearBurst() {
        equippedPlayer.setCooldown(Material.STICK, 60);
        burstSpear(9);
        burstSpear(18);
        burstSpear(27);
        burstSpear(36);
    }

    /**
     * Shoot a single arrow from the spear burst ability
     * @param d The delay with which to shoot the arrow
     */
    private void burstSpear(int d) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Shoot if the player has an arrow
                if (removeSpear()) {
                    equippedPlayer.launchProjectile(Arrow.class).setVelocity(equippedPlayer.getLocation().getDirection().multiply(2.6));
                }
            }
        }.runTaskLater(Main.plugin, d);
    }


    /**
     * Remove an arrow from the player's inventory
     * @return true if the player has an arrow to remove, false otherwise
     */
    private boolean removeSpear() {
        PlayerInventory inv = equippedPlayer.getInventory();

        // Try offhand first
        if (inv.getItemInOffHand().getType() == Material.STICK) {
            ItemStack o = inv.getItemInOffHand();
            o.add(-1);
            return true;
            // Try inventory
        } else if (inv.getItemInMainHand().getType() == Material.STICK) {
            ItemStack stick = inv.getItemInMainHand();
            stick.add(-1);
            return true;
        }

        // No arrow found
        return false;
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
