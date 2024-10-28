package me.huntifi.castlesiege.events.gameplay;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Handles horse events
 */
public class HorseHandler implements Listener {

    // Kit specific horse information
    private static final HashMap<String, HorseSpawner> horses = new HashMap<>();
    private static final HashMap<String, Integer> cooldowns = new HashMap<>();
    private static String horseType = "regular";

    /**
     * Add a kit and its spawn-able horse
     * @param kitName The name of the kit that can spawn the horse
     * @param cooldown The time in ticks before a new horse can be spawned
     * @param health The horse's max health
     * @param knockback The horse's knockback resistance
     * @param speed The horse's movement speed
     * @param jump The horse's jump strength
     * @param armor The horse's horse armor
     * @param effects The horse's potion effects
     */
    public static void add(String kitName, int cooldown, double health, double knockback, double speed,
                           double jump, Material armor, @Nullable Collection<PotionEffect> effects, String type) {
        HorseSpawner horse = new HorseSpawner(health, knockback, speed, jump, armor, effects);
        horses.put(kitName, horse);
        cooldowns.put(kitName, cooldown);
        horseType = type;
    }

    /**
     * Spawn and embark a horse
     * @param e The event called when clicking with wheat in hand
     */
    @EventHandler
    public void onRide(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        // Check if the player attempts to spawn a horse
        HorseSpawner horse = horses.get(Kit.equippedKits.get(uuid).name);
        if (horse != null && e.getItem() != null
                && e.getItem().getType() == Material.WHEAT
                && p.getCooldown(Material.WHEAT) == 0) {

            if (p.isInsideVehicle()) {
                Messenger.sendActionError("Leave your current vehicle to spawn a horse!", p);
            } else {
                horse.spawn(p, horseType);
            }
        }
    }

    /**
     * Remove the horse when its rider dismounts and apply cooldown
     * @param e The event called when dismounting a horse
     */
    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        removeHorse(e.getDismounted());
        setCooldown(e.getEntity(), e.getDismounted());
    }

    /**
     * Remove the horse when its rider dies
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        removeHorse(e.getEntity().getVehicle());
    }

    /**
     * Remove the horse when its rider leaves the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        removeHorse(e.getPlayer().getVehicle());
    }

    /**
     * Apply cooldown when the horse dies
     * @param e The event called when a horse dies
     */
    @EventHandler
    public void onHorseDeath(EntityDeathEvent e) {
        Entity horse = e.getEntity();
        if (!horse.getPassengers().isEmpty())
            setCooldown(horse.getPassengers().get(0), horse);
        if (horse instanceof Horse)
            e.getDrops().clear();
    }

    /**
     * Remove the horse
     * @param horse The horse that is to be removed
     */
    private void removeHorse(Entity horse) {
        if (horse instanceof Horse || horse instanceof SkeletonHorse || horse instanceof ZombieHorse) {
            horse.remove();
        }
    }

    /**
     * Set a player's horse-spawning cooldown
     * @param player The player
     */
    private void setCooldown(Entity player, Entity horse) {
        if (player instanceof Player && (horse instanceof Horse || horse instanceof SkeletonHorse || horse instanceof ZombieHorse)) {
            Player p = (Player) player;
            if (Kit.equippedKits.get(p.getUniqueId()) == null) {
                return;
            }
            Integer cooldown = cooldowns.get(Kit.equippedKits.get(p.getUniqueId()).name);
            if (cooldown != null) {
                p.setCooldown(Material.WHEAT, cooldown);
            }
        }
    }
}