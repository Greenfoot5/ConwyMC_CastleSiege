package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;


public class Catapult implements Listener {

    // General variables used by the catapult
    private final String direction;
    private final World world;

    // Location and names of the schematics used for the different catapult states
    private final Location schematicLocation;
    private final String schematicReloading;
    private final String schematicShot;

    // Booleans used to represent the catapult state
    private boolean canRefill = false;
    private boolean canShoot = true;

    // Variables used for operating the catapult
    private double aimHorizontal = 0.0;
    private double aimVertical = 20.0;
    private final Location lever;
    private final Location signHorizontal;
    private final Location signVertical;

    // Variables used for shooting the catapult
    private final Location cobblestone;
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
    public Catapult(String world, String direction, Vector location) {
        // Set direction independent variables
        this.world = Bukkit.getWorld(world);
        if (this.world == null)
            throw new IllegalArgumentException("The world " + world + " does not exist!");
        this.direction = direction.toLowerCase();
        this.schematicLocation = location.toLocation(this.world);
        this.signHorizontal = location.toLocation(this.world);

        // Set direction dependent variables
        switch (this.direction) {
            case "north":
                // Locations
                this.lever = new Vector(0, 1, -1).add(location).toLocation(this.world);
                this.signVertical = new Vector(0, 0, 1).add(location).toLocation(this.world);
                this.sound = new Vector(2, 6, -4).add(location).toLocation(this.world);
                this.projectile = new Vector(2, 6, -5).add(location).toLocation(this.world);
                this.cobblestone = new Vector(2, 1, 5).add(location).toLocation(this.world);

                // Schematics
                this.schematicReloading = "Catapult_Reloading_North";
                this.schematicShot = "Catapult_Shot_North";
                break;

            case "east":
                // Locations
                this.lever = new Vector(1, 1, 0).add(location).toLocation(this.world);
                this.signVertical = new Vector(-1, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(4, 6, 2).add(location).toLocation(this.world);
                this.projectile = new Vector(5, 6, 2).add(location).toLocation(this.world);
                this.cobblestone = new Vector(-5, 1, 2).add(location).toLocation(this.world);

                // Schematics
                this.schematicReloading = "Catapult_Reloading_East";
                this.schematicShot = "Catapult_Shot_East";
                break;

            case "south":
                // Locations
                this.lever = new Vector(0, 1, 1).add(location).toLocation(this.world);
                this.signVertical = new Vector(0, 0, -1).add(location).toLocation(this.world);
                this.sound = new Vector(-2, 6, 4).add(location).toLocation(this.world);
                this.projectile = new Vector(-2, 6, 5).add(location).toLocation(this.world);
                this.cobblestone = new Vector(-2, 1, -5).add(location).toLocation(this.world);

                // Schematics
                this.schematicReloading = "Catapult_Reloading_South";
                this.schematicShot = "Catapult_Shot_South";
                break;

            case "west":
                // Locations
                this.lever = new Vector(-1, 1, 0).add(location).toLocation(this.world);
                this.signVertical = new Vector(1, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(-4, 6, -2).add(location).toLocation(this.world);
                this.projectile = new Vector(-5, 6, -2).add(location).toLocation(this.world);
                this.cobblestone = new Vector(5, 1, -2).add(location).toLocation(this.world);

                // Schematics
                schematicReloading = "Catapult_Reloading_West";
                schematicShot = "Catapult_Shot_West";
                break;

            default:
                // Ensure that a valid direction is supplied
                throw new IllegalArgumentException("The direction " + direction
                        + " cannot be applied to a catapult!");
        }
    }

    /**
     * Handles shooting the catapult
     * @param event The event called when pulling a lever
     */
    @EventHandler
    public void onPullLever(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.LEVER
                || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Make sure the lever belongs to this catapult
        Player player = event.getPlayer();
        if (Objects.equals(player.getWorld(), world) && clickedBlock.getLocation().distance(lever) == 0) {

            // Check if the catapult can be shot
            if (canShoot) {
                shooter = player;
                shoot();
            } else {
                Messenger.sendActionError("This catapult is still reloading", player);
                event.setCancelled(true);
            }
        }

    }

    /**
     * Shoot the catapult and start the timer for it coming back down again
     */
    private void shoot() {
        // Animate the shot and play the shooting sound
        SchematicSpawner.spawnSchematic(schematicLocation, schematicShot);
        world.playSound(sound, Sound.ENTITY_BLAZE_SHOOT, 5, 1);

        // Perform the shot
        launchProjectile();
        canShoot = false;

        // Start the 20s timer for the catapult coming back down
        new BukkitRunnable() {
            @Override
            public void run() {
                tension();
            }
        }.runTaskLater(Main.plugin, 400);
    }

    /**
     * Tension the catapult and start the timer for it being refilled
     */
    private void tension() {
        // Animate the tightening and play the tightening sound
        SchematicSpawner.spawnSchematic(schematicLocation, schematicReloading);
        world.playSound(sound, Sound.BLOCK_DISPENSER_DISPENSE, 5, 1);
        canRefill = true;

        // Start the 20s timer for the catapult being refilled automatically
        new BukkitRunnable() {
            @Override
            public void run() {
                refill();
            }
        }.runTaskLater(Main.plugin, 400);
    }

    /**
     * Refill the catapult's bucket and allow it to be shot again
     */
    private void refill() {
        if (canRefill) {
            // Perform the visual changes
            if (lever.getBlock().getBlockData() instanceof Powerable) {
                Powerable leverData = (Powerable) lever.getBlock().getBlockData();
                leverData.setPowered(false);
                lever.getBlock().setBlockData(leverData);
            }
            cobblestone.getBlock().setType(Material.COBBLESTONE_SLAB);

            // Perform the logical changes
            canShoot = true;
            canRefill = false;
        }
    }

    /**
     * Handles interactions with the aim up/down sign of the catapult
     * @param event The event called when interacting with a sign
     */
    @EventHandler
    public void onClickAimVertical(PlayerInteractEvent event) {
        Block target = event.getClickedBlock();
        Player p = event.getPlayer();
        if (target != null && target.getState() instanceof Sign &&
                Objects.equals(p.getWorld(), world) && target.getLocation().distance(signVertical) == 0) {

            // Decrease (down) the catapult's vertical aim
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && aimVertical > -15) {
                aimVertical -= 1;
            }

            // Increase (up) the catapult's vertical aim
            if (event.getAction() == Action.LEFT_CLICK_BLOCK && aimVertical < 90) {
                aimVertical += 1;
            }

            // Update the sign
            Sign sign = (Sign) target.getState();
            sign.getSide(Side.FRONT).line(2, Component.text(aimVertical));
            sign.update();
        }
    }

    /**
     * Handles interactions with the aim left/right sign of the catapult
     * @param event The event called when interacting with a sign
     */
    @EventHandler
    public void onClickAimHorizontal(PlayerInteractEvent event) {
        Block target = event.getClickedBlock();
        Player p = event.getPlayer();
        if (target != null && target.getState() instanceof Sign &&
                Objects.equals(p.getWorld(), world) && target.getLocation().distance(signHorizontal) == 0) {

            // Increase (right) the catapult's horizontal aim
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && aimHorizontal < 55) {
                aimHorizontal += 0.5;
            }

            // Decrease (left) the catapult's horizontal aim
            if (event.getAction() == Action.LEFT_CLICK_BLOCK && aimHorizontal > -55) {
                aimHorizontal -= 0.5;
            }

            // Update the sign
            Sign sign = (Sign) target.getState();
            sign.getSide(Side.FRONT).line(2, Component.text(aimHorizontal));
            sign.update();
        }
    }

    /**
     * Launch the catapult's projectile
     */
    public void launchProjectile() {
        // Spawn a falling cobblestone block riding a snowball
        BlockData blockData = Bukkit.createBlockData(Material.COBBLESTONE);
        FallingBlock cobblestone = world.spawnFallingBlock(projectile, blockData);
        snowball = world.spawn(projectile, Snowball.class);
        snowball.addPassenger(cobblestone);

        // Set the snowball's velocity
        Vector vector = new Vector();
        switch (direction) {
            case "north":
                vector = new Vector(aimHorizontal, aimVertical, -34);
                break;
            case "east":
                vector = new Vector(34, aimVertical, aimHorizontal);
                break;
            case "south":
                vector = new Vector(-aimHorizontal, aimVertical, 34);
                break;
            case "west":
                vector = new Vector(-34, aimVertical, -aimHorizontal);
                break;
        }
        snowball.setVelocity(vector.normalize().multiply(3.5));
    }

    /**
     * Delete the cobblestone and create an explosion on impact
     * @param event The event called when the snowball hits something
     */
    @EventHandler
    public void onImpact(ProjectileHitEvent event) {
        if (Objects.equals(snowball, event.getEntity())) {
            snowball.getPassengers().get(0).remove();
            world.createExplosion(snowball.getLocation(), 4F, false, true, shooter);
        }
    }

    /**
     * Allows engineers to refill catapults
     * @param event The event called when a block is placed
     */
    @EventHandler
    public void engineerRefill(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getBlock();

        // Ensure that an engineer placed a block in the catapult's bucket
        if (Objects.equals(Kit.equippedKits.get(uuid).name, "Engineer")
                && Objects.equals(player.getWorld(), world)
                && block.getLocation().distance(cobblestone) <= 1) {

            if (event.getBlock().getType() != Material.COBBLESTONE) {
                Messenger.sendActionError("The catapult can only be reloaded with cobblestone", player);
            } else if (!canRefill) {
                Messenger.sendActionError("The catapult already has a projectile ready", player);
            } else {
                // Perform the refill on the next tick to prevent overwriting the cobblestone slab
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        refill();
                        ItemStack item = event.getItemInHand();
                        item.setAmount(item.getAmount() - 1);
                    }
                }.runTask(Main.plugin);

            }
        }
    }
}
