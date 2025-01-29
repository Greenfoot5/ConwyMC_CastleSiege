package me.greenfoot5.castlesiege.maps.objects;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.map.CannonEvent;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Artillerist;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.structures.SchematicSpawner;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a cannon
 */
public class Cannon implements Listener {

    // General variables used by the cannon
    private final String direction;
    private final Location schematicLocation;
    private final Location recoil;

    // Booleans used to represent the cannon state
    private STATE state = STATE.Ready;

    // Variables used for operating the cannon
    private double vertical_multiplier = 1.0;
    private final double yaw;
    private final double pitch;

    // Variables used for shooting the cannon
    private final Location barrel;
    private Player shooter;
    private Snowball projectile;

    /**
     * Create a new cannon
     * @param direction The direction in which the cannon is facing
     * @param location The base location for the cannon
     * @param yaw The yaw (left/right) to fire the cannonball from.
     * @param pitch # The pitch (vertical) to fire the cannonball
     */
    public Cannon(String direction, Location location, double yaw, double pitch) {
        this.direction = direction.toLowerCase();
        this.schematicLocation = location;
        this.yaw = yaw;
        this.pitch = pitch == 0 ? 1 : pitch;

        // Set direction dependent variables
        switch (this.direction) {
            case "north":
                // Locations
                this.barrel = location.clone().add(0, 0, -4);
                this.recoil = location.clone().add(0, 0, 1);
                break;

            case "east":
                // Locations
                this.barrel = location.clone().add(4, 0, 0);
                this.recoil = location.clone().add(-1, 0, 0);
                break;

            case "south":
                // Locations
                this.barrel = location.clone().add(0, 0, 4);
                this.recoil = location.clone().add(0, 0, -1);
                break;

            case "west":
                // Locations
                this.barrel = location.add(-4, 0, 0);
                this.recoil = location.add(1, 0, 0);
                break;

            default:
                // Ensure that a valid direction is supplied
                throw new IllegalArgumentException("The direction " + direction
                        + " cannot be applied to a cannon!");
        }
    }


    /**
     * Handles shooting the cannon
     * @param event The event when a player presses the button
     */
    @EventHandler
    public void onPressButton(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null
                || clickedBlock.getType() != Material.POLISHED_BLACKSTONE_BUTTON
                || (event.getAction() != Action.RIGHT_CLICK_BLOCK
                    && event.getAction() != Action.LEFT_CLICK_BLOCK)
                || !TeamController.isPlaying(event.getPlayer())) {
            return;
        }

        // Make sure the button belongs to this cannon
        Player player = event.getPlayer();
        if (clickedBlock.getLocation().distance(schematicLocation) == 0) {
            shooter = player;
            shoot();
        }
    }

    /**
     * Shoot the cannon
     */
    private void shoot() {
        CannonEvent shootEvent = new CannonEvent(shooter);
        Bukkit.getPluginManager().callEvent(shootEvent);
        if (shootEvent.isCancelled()) {
            return;
        }

        // Animate the shot and play the shooting sound
        SchematicSpawner.spawnSchematic(recoil, "Cannon_Shot_" + direction);
        barrel.getWorld().playSound(barrel, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);

        // Perform the shot
        launchProjectile();
        state = STATE.Shot;

        // Start the 10s timer for the catapult coming back down
        Bukkit.getScheduler().runTaskLater(Main.plugin, this::reposition, 200);
    }

    /**
     * Reposition the cannon from its recoil position, it is now ready to be refilled.
     */
    private void reposition() {
        // Animate the reposition and play the reposition sound
        SchematicSpawner.spawnSchematic(schematicLocation, "Cannon_Reloading_" + direction);
        barrel.getWorld().playSound(barrel, Sound.BLOCK_DISPENSER_DISPENSE, 5, 1);
        state = STATE.Reloading;
    }

    /**
     * Refill the cannon supposedly and allow it to be shot again
     */
    private void addBall() {
        if (state == STATE.Reloading) {
            SchematicSpawner.spawnSchematic(schematicLocation, "Cannon_Ready_" + direction);
            // Perform the logical changes
            state = STATE.Ready;
        }
    }

    /**
     * Launch the cannon's projectile
     */
    public void launchProjectile() {
        // Spawn a falling cobblestone block riding a projectile
        projectile = barrel.getWorld().spawn(barrel, Snowball.class);

        // Set the projectile's velocity
        Vector vector = new Vector();
        vector = switch (direction) {
            case "north" -> new Vector(yaw, pitch * vertical_multiplier, -30 - pitch * vertical_multiplier);
            case "east" -> new Vector(30 + pitch * vertical_multiplier, pitch, yaw);
            case "south" -> new Vector(-yaw, pitch * vertical_multiplier, 30 + pitch * vertical_multiplier);
            case "west" -> new Vector(-30 - pitch * vertical_multiplier, pitch * vertical_multiplier, -yaw);
            default -> vector;
        };
        vertical_multiplier = 0.0;
        projectile.setVelocity(vector.normalize().multiply(3.5));
    }

    /**
     * Create an explosion on impact
     * @param event The event called when the projectile hits something
     */
    @EventHandler
    public void onImpact(ProjectileHitEvent event) {
        if (Objects.equals(projectile, event.getEntity())) {
            schematicLocation.getWorld().createExplosion(projectile.getLocation(), 3F, false, true, shooter);
        }
    }

    /**
     * Allows artillerists to add balls to cannons
     * @param event The event called when a block is placed
     */
    @EventHandler
    public void artilleristRefill(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        ItemStack item = player.getInventory().getItemInMainHand();
        int ballCooldown = player.getCooldown(Material.PLAYER_HEAD);
        int powderCooldown = player.getCooldown(Material.GUNPOWDER);

        // If the player isn't clicking on the right thing, we don't care
        if (block == null || InCombat.isPlayerInLobby(uuid)
        || block.getLocation().distance(schematicLocation) != 0
        || (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        // Only Arillerist can do this
        if (!Objects.equals(Kit.equippedKits.get(uuid).name, new Artillerist().name)) {
            Messenger.sendActionError("Only Arillerists can reload cannons!", player);
        }

        // Player is adding a cannon bal
        if (player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD
                && block.getType().equals(Material.TRIPWIRE_HOOK)
                && ballCooldown == 0) {

            if (state == STATE.Ready) {
                Messenger.sendActionError("The cannon already has a projectile ready!", player);
                return;
            } else if (vertical_multiplier == 0) {
                Messenger.sendActionError("The cannon need gunpowder first!", player);
                return;
            }

            // Perform the addBall on the next tick to prevent overwriting the cobblestone slab
            addBall();
            item.setAmount(item.getAmount() - 1);
            player.setCooldown(Material.FIREWORK_STAR, 20);
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() == Material.GUNPOWDER
                && block.getType().equals(Material.TRIPWIRE_HOOK)
                && powderCooldown == 0) {
            if (vertical_multiplier == 0)
                vertical_multiplier = 1;
            else if (vertical_multiplier < 11)
                vertical_multiplier += 0.75;
            else {
                Messenger.sendActionError("Cannon is full of gunpowder!", player);
                return;
            }

            player.setCooldown(Material.GUNPOWDER, 5);
            item.setAmount(item.getAmount() - 1);
            Messenger.sendActionInfo("Contains " + new DecimalFormat("0.0").format(vertical_multiplier) + "/11.5 gunpowder", player);
        }
    }

    private enum STATE {
        Ready,
        Shot,
        Reloading
    }
}
