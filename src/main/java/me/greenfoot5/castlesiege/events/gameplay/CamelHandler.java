package me.greenfoot5.castlesiege.events.gameplay;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Material;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Handles players using/interacting with camels
 */
public class CamelHandler implements Listener {

    // Kit specific camel information
    private static final HashMap<String, CamelSpawner> camels = new HashMap<>();
    private static final HashMap<String, Integer> cooldowns = new HashMap<>();

    /**
     * Add a kit and its spawn-able camel
     * @param kitName The name of the kit that can spawn the camel
     * @param cooldown The time in ticks before a new camel can be spawned
     * @param health The camel's max health
     * @param knockback The camel's knockback resistance
     * @param speed The camel's movement speed
     * @param jump The camel's jump strength
     * @param effects The camel's potion effects
     */
    public static void add(String kitName, int cooldown, double health, double knockback, double speed,
                           double jump, @Nullable Collection<PotionEffect> effects) {
        CamelSpawner camel = new CamelSpawner(health, knockback, speed, jump, effects);
        camels.put(kitName, camel);
        cooldowns.put(kitName, cooldown);
    }

    /**
     * Spawn and embark a camel
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

        // Check if the player attempts to spawn a camel
        CamelSpawner camel = camels.get(Kit.equippedKits.get(uuid).name);
        if (camel != null && e.getItem() != null
                && e.getItem().getType() == Material.WHEAT
                && p.getCooldown(Material.WHEAT) == 0) {

            if (p.isInsideVehicle()) {
                Messenger.sendActionError("Leave your current vehicle to spawn a camel!", p);
            } else {
                camel.spawn(p);
            }
        }
    }

    /**
     * Remove the camel when its rider dismounts and apply cooldown
     * @param e The event called when dismounting a camel
     */
    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player) {
            removeCamel(e.getDismounted(), (Player) e.getEntity());
            setCooldown(e.getEntity(), e.getDismounted());
        }
    }

    /**
     * players that aren't on the same team of the rider, cannot join on the camel's back
     * @param e The event called when a player right-clicks an enemy camel
     */
    @EventHandler
    public void onClickCamel(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Camel camel) {
            UUID uuid = Objects.requireNonNull(camel.getOwner()).getUniqueId();
            if (!(TeamController.getTeam(e.getPlayer().getUniqueId()).equals(TeamController.getTeam(uuid)))) {
                e.setCancelled(true);
            }
        }
    }

    /**
     * Remove the camel when its rider dies
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        removeCamel(e.getEntity().getVehicle(), e.getEntity());
    }

    /**
     * Remove the camel when its rider leaves the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        removeCamel(e.getPlayer().getVehicle(), e.getPlayer());
    }

    /**
     * Apply cooldown when the camel dies
     * @param e The event called when a camel dies
     */
    @EventHandler
    public void onCamelDeath(EntityDeathEvent e) {
        Entity camel = e.getEntity();
        if (!camel.getPassengers().isEmpty())
            setCooldown(camel.getPassengers().getFirst(), camel);
        if (camel instanceof Camel)
            e.getDrops().clear();
    }

    /**
     * Remove the camel
     * @param camel The camel that is to be removed
     */
    private void removeCamel(Entity camel, Player rider) {
        if (camel instanceof Camel c) {
            if (rider.equals(c.getOwner())) {
                c.remove();
            }
        }
    }

    /**
     * Set a player's camel-spawning cooldown
     * @param player The player
     */
    private void setCooldown(Entity player, Entity camel) {
        if (player instanceof Player p && camel instanceof Camel) {
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
