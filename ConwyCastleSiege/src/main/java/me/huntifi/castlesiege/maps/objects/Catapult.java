package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;


public class Catapult implements Listener {

    // Name of the catapult and the world that it's in
    private final String name;
    private final World world;

    // Different locations used by the catapult
    private final Location cobblestone;
    private final Location projectile;
    private final Location sound;

    // Location and names of the schematics for the different catapult states
    private final Location schematicLocation;
    private final String schematicNormal;
    private final String schematicReloading;
    private final String schematicShot;

    // Booleans used to represent the catapult state
    private boolean canRefill = false;
    private boolean canShoot = true;

    // TODO - Sort out the following variables
    //the value for aim up / down, which is 20.0 by default.
    private double up_down = 20.0;
    //the value for aim left/right
    private double left_right = 0.0;
    private final Location lever;
    private final Location signLeftRight;
    private final Location signUpDown;
    private final String direction;
    //serves as a third value for the shooting vectors, this makes sure it shoots straight and not backwards.
    private double forwardValueNorthZ = -34.0;
    private double forwardValueEastX = 34.0;
    private double forwardValueWestX = -34.0;
    private double forwardValueSouthZ = 34.0;

    private FallingBlock projectilePassenger;

    private Snowball ball;

    /**
     * Create a new catapult
     * @param name The name of the catapult
     * @param world The name of the world in which the catapult is location
     * @param direction The direction in which the catapult is facing
     * @param location The base location for the catapult
     */
    public Catapult(String name, String world, String direction, Vector location) {
        // Set direction independent variables
        this.name = name;
        this.world = Bukkit.getWorld(world);
        this.direction = direction;

        // Ensure that a valid world is supplied
        if (this.world == null)
            throw new IllegalArgumentException("The world " + world + " does not exist!");

        this.schematicLocation = location.toLocation(this.world);
        this.signLeftRight = location.toLocation(this.world);

        // Set direction dependent variables
        switch (direction.toLowerCase()) {
            case "north":
                // Locations
                this.lever = new Vector(0, 1, -1).add(location).toLocation(this.world);
                this.signUpDown = new Vector(0, 0, 1).add(location).toLocation(this.world);
                this.sound = new Vector(2, 6, -4).add(location).toLocation(this.world);
                this.projectile = new Vector(2, 6, -5).add(location).toLocation(this.world);
                this.cobblestone = new Vector(2, 1, 5).add(location).toLocation(this.world);

                // Schematics
                this.schematicNormal = "Catapult_Normal_North";
                this.schematicReloading = "Catapult_Reloading_North";
                this.schematicShot = "Catapult_Shot_North";
                break;

            case "east":
                // Locations
                this.lever = new Vector(1, 1, 0).add(location).toLocation(this.world);
                this.signUpDown = new Vector(-1, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(4, 6, 2).add(location).toLocation(this.world);
                this.projectile = new Vector(5, 6, 2).add(location).toLocation(this.world);
                this.cobblestone = new Vector(-5, 1, 2).add(location).toLocation(this.world);

                // Schematics
                this.schematicNormal = "Catapult_Normal_East";
                this.schematicReloading = "Catapult_Reloading_East";
                this.schematicShot = "Catapult_Shot_East";
                break;

            case "south":
                // Locations
                this.lever = new Vector(0, 1, 1).add(location).toLocation(this.world);
                this.signUpDown = new Vector(0, 0, -1).add(location).toLocation(this.world);
                this.sound = new Vector(-2, 6, 4).add(location).toLocation(this.world);
                this.projectile = new Vector(-2, 6, 5).add(location).toLocation(this.world);
                this.cobblestone = new Vector(-2, 1, -5).add(location).toLocation(this.world);

                // Schematics
                this.schematicNormal = "Catapult_Normal_South";
                this.schematicReloading = "Catapult_Reloading_South";
                this.schematicShot = "Catapult_Shot_South";
                break;

            case "west":
                // Locations
                this.lever = new Vector(-1, 1, 0).add(location).toLocation(this.world);
                this.signUpDown = new Vector(1, 0, 0).add(location).toLocation(this.world);
                this.sound = new Vector(-4, 6, -2).add(location).toLocation(this.world);
                this.projectile = new Vector(-5, 6, -2).add(location).toLocation(this.world);
                this.cobblestone = new Vector(5, 1, -2).add(location).toLocation(this.world);

                // Schematics
                schematicNormal = "Catapult_Normal_West";
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
                shoot(player);
            } else {
                Messenger.sendActionError("This catapult is still reloading", player);
                event.setCancelled(true);
            }
        }

    }

    /**
     * Shoot the catapult and start the timer for it coming back down again
     * @param player The player who shot the catapult
     */
    private void shoot(Player player) {
        try {
            // Animate the shot and play the shooting sound
            SchematicSpawner.spawnSchematic(schematicLocation, schematicShot, world.getName());
            world.playSound(sound, Sound.ENTITY_BLAZE_SHOOT, 5, 1);

            // Perform the shot
            launchProjectile(player);
            canShoot = false;
            canRefill = false;

            // Start the 20s timer for the catapult coming back down
            new BukkitRunnable() {
                @Override
                public void run() {
                    tension();
                }
            }.runTaskLater(Main.plugin, 400);

        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tension the catapult and start the timer for it being refilled
     */
    private void tension() {
        try {
            // Animate the tensioning and play the tensioning sound
            SchematicSpawner.spawnSchematic(schematicLocation, schematicReloading, world.getName());
            world.playSound(sound, Sound.BLOCK_DISPENSER_DISPENSE, 5, 1);
            canRefill = true;

            // Start the 20s timer for the catapult being refilled automatically
            new BukkitRunnable() {
                @Override
                public void run() {
                    refill();
                }
            }.runTaskLater(Main.plugin, 400);

        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refill the catapult's bucket
     */
    private void refill() {
        if (canRefill) {
            // Perform the visual changes
            Powerable leverData = (Powerable) lever.getBlock().getBlockData();
            leverData.setPowered(false);
            lever.getBlock().setBlockData(leverData);
            cobblestone.getBlock().setType(Material.COBBLESTONE_SLAB);

            // Perform the logical changes
            canShoot = true;
            canRefill = false;
        }
    }

    /**
     * TODO
     * @param event Makes the aim up/down sign of the catapult interactable
     */

    @EventHandler
    public void onClickAimVertical(PlayerInteractEvent event) {

        Block target = event.getClickedBlock();

        Player p = event.getPlayer();

        if (target != null && target.getState() instanceof Sign &&
                p.getLocation().distance(signUpDown) <= 4 && // TODO - Check the world first
                p.getWorld() == world) {

            Sign sign = (Sign) event.getClickedBlock().getState();

            if (sign.getLine(0).equalsIgnoreCase("Aim up/down")) {

                up_down = Double.parseDouble(sign.getLine(2));

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (up_down >= 5) {

                        up_down = Double.parseDouble(sign.getLine(2)) - 1;
                        sign.setLine(2, up_down + "");
                        sign.update();

                    }
                }

                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                    if (up_down <= 90) {

                        up_down = Double.parseDouble(sign.getLine(2)) + 1;
                        sign.setLine(2, up_down + "");
                        sign.update();

                    }
                }

            }
        } else {
            return;
        }
    }

    /**
     * TODO
     * @param event makes the left/right sign of a catapult interactable
     */
    @EventHandler
    public void onClickAimHorizontal(PlayerInteractEvent event) {

        Block target = event.getClickedBlock();

        Player p = event.getPlayer();

        if (p.getWorld() != world) {
            return;
        }

        if (target != null && target.getState() instanceof Sign
                && p.getLocation().distance(signLeftRight) <= 4
                && p.getWorld() == world) {

            Sign sign = (Sign) target.getState();

            if (sign.getLine(0).equalsIgnoreCase("Aim left/right")) {

                left_right = Double.parseDouble(sign.getLine(2));

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (left_right < 55) {

                        left_right = Double.parseDouble(sign.getLine(2)) + 0.5;
                        sign.setLine(2, left_right + "");
                        sign.update();

                    }
                }

                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                    if (left_right > -55) {

                        left_right = Double.parseDouble(sign.getLine(2)) - 0.5;
                        sign.setLine(2, left_right + "");
                        sign.update();

                    }
                }

            }
        } else {
            return;
        }

    }

    /**
     * TODO
     */
    //This is the projectile that needs to be shot and the direction.
    public void launchProjectile(Player shooter) {

        byte blockData = 0x0;

        ball = world.spawn(projectile, Snowball.class);

        ball.setShooter(shooter);

        ball.setGravity(true);

        projectilePassenger = world.spawnFallingBlock(projectile, Material.COBBLESTONE, blockData);

        projectilePassenger.setDropItem(false);

        ball.addPassenger(projectilePassenger);

        switch (direction) {

            case "north":

                double vecX = left_right;

                double vecZ = forwardValueNorthZ;

                double vecY = up_down;

                Vector v = new Vector(vecX, vecY, vecZ);

                ball.setVelocity(v);
                ball.setVelocity(ball.getVelocity().normalize().multiply(3.5));

                break;

            case "east":

                double vecX2 = forwardValueEastX;

                double vecY2 = up_down;

                double vecZ2 = left_right;

                Vector v2 = new Vector(vecX2, vecY2, vecZ2);

                ball.setVelocity(v2);
                ball.setVelocity(ball.getVelocity().normalize().multiply(3.5));


                break;

            case "west":

                double vecZ3 = left_right;

                double vecX3 = forwardValueWestX;

                double vecY3 = up_down;

                Vector v3 = new Vector(vecX3, vecY3, vecZ3);

                ball.setVelocity(v3);
                ball.setVelocity(ball.getVelocity().normalize().multiply(3.5));
                break;

            case "south":

                double vecY4 = up_down;

                double vecX4 = left_right;

                double vecZ4 = forwardValueSouthZ;

                Vector v4 = new Vector(vecX4, vecY4, vecZ4);

                ball.setVelocity(v4);
                ball.setVelocity(ball.getVelocity().normalize().multiply(3.5));

                break;


            default:
                break;
        }

    }

    /**
     *
     * @param event This event is the catapult projectile's explosion.
     */
    @EventHandler
    public void onImpact(ProjectileHitEvent event) {

        if (event.getEntity() instanceof Snowball) {

            if (ball == event.getEntity()) {

                ball = (Snowball) event.getEntity();

                if (ball.getPassengers() == null) {
                    return;
                } else {
                    ball.getPassengers().remove(0);
                }

                if (event.getHitEntity() instanceof Player) {
                    event.getHitEntity().getWorld().createExplosion(event.getEntity().getLocation(), 4F, false, true);
                    return;
                }

                event.getHitBlock().getWorld().createExplosion(event.getHitBlock().getLocation(), 4F, false, true);

            }
        }


    }

    /**
     *
     * @param event This event makes sure the falling block doesn't turn into a block.
     */
    @EventHandler
    public void onImpact(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            if (event.getEntity() != projectilePassenger) {
                return;
            }
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void engineerRefill(BlockPlaceEvent event) {

        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        if (Objects.equals(Kit.equippedKits.get(uuid).name, "Engineer")) {

            if (event.getBlockAgainst().getLocation().distance
                    (cobblestone) < 2) {

                if (event.getBlock().getType() != Material.COBBLESTONE) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "not cobblestone"));
                    return;
                }

                if (!canRefill) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Catapult is already refilled"));
                    return;
                }

                if (canShoot) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "canShoot == true"));
                    return;
                }

                cobblestone.getBlock().setType(Material.COBBLESTONE_SLAB);
                canRefill = false;
                canShoot = true;

                ItemStack cobble = p.getInventory().getItem(5);
                if (p.getInventory().getItem(5).getType() != Material.COBBLESTONE) {
                    return;
                }
                cobble.setAmount(cobble.getAmount() - 1);

            }
        }
    }

    @EventHandler
    public void onExplosionDamage(EntityDamageEvent e) {
        if (e.isCancelled() || e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
        ) {
            return;
        }
        e.setDamage(180);
    }
}
