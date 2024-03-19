package me.huntifi.castlesiege.kits.kits.team_kits.moria;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
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
import java.util.Objects;
import java.util.UUID;

public class MoriaWindlancer extends TeamKit implements Listener {
    public MoriaWindlancer() {
        super("Windlancer", 300, 9 , "Moria",
                "The Dwarves", 5000, Material.STICK,
                "moriawindlancer");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STICK, 20),
                Component.text("Small Hand-Windlass Spear", NamedTextColor.GREEN), null, null, 29);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STICK, 20),
                        Component.text("Small Hand-Windlass Spear", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 31),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(218, 211, 183));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(218, 183, 183));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
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
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwSpear(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stick = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.STICK);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (stick.getType().equals(Material.STICK)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        Messenger.sendActionInfo("You threw your spear-burst!", p);
                        shootSpearBurst(p);

                    } else {
                        Messenger.sendActionError("You can't use spear-burst yet!", p);
                    }
                }
            }
        }
    }

    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW)
    public void changeSpearDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            if(arrow.getShooter() instanceof Player){
                Player damages = (Player) arrow.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    arrow.setDamage(20);
                }
            }
        }
    }


    public void shootSpearBurst(Player p) {
        p.setCooldown(Material.STICK, 60);
        burstSpear(p,  9);
        burstSpear(p,  18);
        burstSpear(p,  27);
        burstSpear(p, 36);
    }

    /**
     * Shoot a single arrow from the spear burst ability
     * @param p The Windlancer shooting their spear burst
     * @param d The delay with which to shoot the arrow
     */
    private void burstSpear(Player p, int d) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Shoot if the player has an arrow
                if (removeSpear(p)) {
                    p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(2.6));
                }
            }
        }.runTaskLater(Main.plugin, d);
    }


    /**
     * Remove an arrow from the player's inventory
     * @param p The player from whom to remove an arrow
     * @return true if the player has an arrow to remove, false otherwise
     */
    private boolean removeSpear(Player p) {
        PlayerInventory inv = p.getInventory();

        // Try offhand first
        if (inv.getItemInOffHand().getType() == Material.STICK) {
            ItemStack o = inv.getItemInOffHand();
            o.setAmount(o.getAmount() - 1);
            return true;
            // Try inventory
        } else if (inv.getItemInMainHand().getType() == Material.STICK) {
            ItemStack stick = p.getInventory().getItemInMainHand();
            stick.setAmount(stick.getAmount() - 1);
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
