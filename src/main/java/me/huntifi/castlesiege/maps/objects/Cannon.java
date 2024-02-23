package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.UUID;

public class Cannon implements Listener {

    // General variables used by the catapult
    private final String direction;
    private final World world;

    // Location and names of the schematics used for the different cannon states
    private final Location schematicLocation;
    // Location where the recoil happens
    private final Location recoil;
    private final String schematicReady;
    private final String schematicReloading;
    private final String schematicShot;

    // Booleans used to represent the catapult state
    private boolean canRefill = false;
    private boolean canShoot = true;

    // Variables used for operating the catapult
    private double vertical_multiplier = 1.0;
    private final double aimHorizontal;
    private final double aimVertical;
    private final Location button;

    // Variables used for shooting the cannon
    private final Location projectile;
    private final Location sound;
    private Player shooter;
    private Snowball snowball;

    /**
     * Create a new catapult
     * @param world The name of the world in which the catapult is location
     * @param direction The direction in which the catapult is facing
     * @param location The base location for the catapult
     */
    public Cannon(String world, String direction, Vector location, double horizontal, double power) {
        // Set direction independent variables
        this.world = Bukkit.getWorld(world);
        if (this.world == null)
            throw new IllegalArgumentException("The world " + world + " does not exist!");
        this.direction = direction.toLowerCase();
        this.schematicLocation = location.toLocation(this.world);
        this.aimHorizontal = horizontal;
        this.aimVertical = power;

        // Set direction dependent variables
        switch (this.direction) {
            case "north":
                // Locations
                this.button = new Vector(0, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(0, 1, -4).add(location).toLocation(this.world);
                this.projectile = new Vector(0, 0, -4).add(location).toLocation(this.world);
                this.recoil = new Vector(0, 0, 1).add(location).toLocation(this.world);

                // Schematics
                this.schematicReloading = "Cannon_Reloading_North";
                this.schematicShot = "Cannon_Shot_North";
                this.schematicReady = "Cannon_Ready_North";
                break;

            case "east":
                // Locations
                this.button = new Vector(0, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(4, 1, 0).add(location).toLocation(this.world);
                this.projectile = new Vector(4, 0, 0).add(location).toLocation(this.world);
                this.recoil = new Vector(-1, 0, 0).add(location).toLocation(this.world);

                // Schematics
                this.schematicReloading = "Cannon_Reloading_East";
                this.schematicShot = "Cannon_Shot_East";
                this.schematicReady = "Cannon_Ready_East";
                break;

            case "south":
                // Locations
                this.button = new Vector(0, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(0, 1, 4).add(location).toLocation(this.world);
                this.projectile = new Vector(0, 0, 4).add(location).toLocation(this.world);
                this.recoil = new Vector(0, 0, -1).add(location).toLocation(this.world);

                // Schematics
                this.schematicReloading = "Cannon_Reloading_South";
                this.schematicShot = "Cannon_Shot_South";
                this.schematicReady = "Cannon_Ready_South";
                break;

            case "west":
                // Locations
                this.button = new Vector(0, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(-4, 1, 0).add(location).toLocation(this.world);
                this.projectile = new Vector(-4, 0, 0).add(location).toLocation(this.world);
                this.recoil = new Vector(1, 0, 0).add(location).toLocation(this.world);

                // Schematics
                schematicReloading = "Cannon_Reloading_West";
                schematicShot = "Cannon_Shot_West";
                this.schematicReady = "Cannon_Ready_West";
                break;

            default:
                // Ensure that a valid direction is supplied
                throw new IllegalArgumentException("The direction " + direction
                        + " cannot be applied to a cannon!");
        }
    }


    /**
     * Handles shooting the catapult
     * @param event The event called when pulling a lever
     */
    @EventHandler
    public void onPressButton(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.POLISHED_BLACKSTONE_BUTTON
                || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Make sure the button belongs to this cannon
        Player player = event.getPlayer();
        if (Objects.equals(player.getWorld(), world) && clickedBlock.getLocation().distance(button) == 0) {

            // Check if the cannon can be shot
            if (canShoot) {
                shooter = player;
                shoot();
            } else {
                Messenger.sendActionError("This cannon is still reloading or has not been reloaded yet.", player);
                event.setCancelled(true);
            }
        }

    }

    /**
     * Shoot the catapult and start the timer for it coming back down again
     */
    private void shoot() {
        // Animate the shot and play the shooting sound
        SchematicSpawner.spawnSchematic(recoil, schematicShot);
        world.playSound(sound, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);

        // Perform the shot
        launchProjectile();
        canShoot = false;

        // Start the 10s timer for the catapult coming back down
        new BukkitRunnable() {
            @Override
            public void run() {
                reposition();
            }
        }.runTaskLater(Main.plugin, 200);
    }

    /**
     * Reposition the cannon from its recoil position, it is now ready to be refilled.
     */
    private void reposition() {
        // Animate the reposition and play the reposition sound
        SchematicSpawner.spawnSchematic(schematicLocation, schematicReloading);
        world.playSound(sound, Sound.BLOCK_DISPENSER_DISPENSE, 5, 1);
        canRefill = true;
    }

    /**
     * Refill the cannon supposedly and allow it to be shot again
     */
    private void refill() {
        if (canRefill) {
            SchematicSpawner.spawnSchematic(schematicLocation, schematicReady);
            // Perform the logical changes
            canShoot = true;
            canRefill = false;
        }
    }

    /**
     * Launch the catapult's projectile
     */
    public void launchProjectile() {
        // Spawn a falling cobblestone block riding a snowball
        snowball = world.spawn(projectile, Snowball.class);

        // Set the snowball's velocity
        Vector vector = new Vector();
        switch (direction) {
            case "north":
                vector = new Vector(aimHorizontal, aimVertical * vertical_multiplier, -30 - aimVertical * vertical_multiplier);
                break;
            case "east":
                vector = new Vector(30 + aimVertical * vertical_multiplier, aimVertical, aimHorizontal);
                break;
            case "south":
                vector = new Vector(-aimHorizontal, aimVertical * vertical_multiplier, 30 + aimVertical * vertical_multiplier);
                break;
            case "west":
                vector = new Vector(-30 - aimVertical * vertical_multiplier, aimVertical * vertical_multiplier, -aimHorizontal);
                break;
        }
        vertical_multiplier = 1.0;
        snowball.setVelocity(vector.normalize().multiply(3.5));
    }

    /**
     * Delete the cobblestone and create an explosion on impact
     * @param event The event called when the snowball hits something
     */
    @EventHandler
    public void onImpact(ProjectileHitEvent event) {
        if (Objects.equals(snowball, event.getEntity())) {
            world.createExplosion(snowball.getLocation(), 2.5F, false, true, shooter);
        }
    }

    /**
     * Allows artillerists to refill cannons
     * @param event The event called when a block is placed
     */
    @EventHandler
    public void artilleristRefill(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        ItemStack item = player.getInventory().getItemInMainHand();
        int cooldown = player.getCooldown(Material.FIREWORK_STAR);
        int cooldown2 = player.getCooldown(Material.GUNPOWDER);
        if (block == null || InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        // Ensure that an engineer placed a block in the catapult's bucket
        if (Objects.equals(Kit.equippedKits.get(uuid).name, "Artillerist")
                && Objects.equals(player.getWorld(), world) && Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
            if (block.getLocation().distance(button) == 0) {
                if (player.getInventory().getItemInMainHand().getType() == Material.FIREWORK_STAR) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                            && cooldown == 0) {
                        if (!canRefill) {
                            Messenger.sendActionError("The cannon already has a projectile ready!", player);
                        } else {
                            // Perform the refill on the next tick to prevent overwriting the cobblestone slab
                            refill();
                            item.setAmount(item.getAmount() - 1);
                            player.setCooldown(Material.FIREWORK_STAR, 20);
                        }
                    }
                } else if (player.getInventory().getItemInMainHand().getType() == Material.GUNPOWDER) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && cooldown2 == 0) {
                        if (!canShoot && event.getClickedBlock().getType().equals(Material.TRIPWIRE_HOOK)) {
                            if (vertical_multiplier < 5) {
                                vertical_multiplier += 0.1;
                                player.setCooldown(Material.GUNPOWDER, 10);
                                item.setAmount(item.getAmount() - 1);
                                Messenger.sendActionInfo("Contains " + new DecimalFormat("0.0").format(vertical_multiplier) + "/Max 5.0 gunpowder", player);
                            }
                        }
                    }
                }
            }
        }
    }
}
