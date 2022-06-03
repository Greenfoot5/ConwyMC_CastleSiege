package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
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

      // Names of the catapult and the world that it's in
      private final String name;
      private final String world;

      // Different locations used by the catapult
      private final Vector cobblestone;
      private final Vector projectile;
      private final Vector sound;

      // Location and names of the schematics for the different catapult states
      private final Vector schematicLocation;
      private final String schematicNormal;
      private final String schematicReloading;
      private final String schematicShot;

      // Booleans used to represent the catapult state
      private boolean canBeRefilled = false;
      private boolean canShoot = true;

      // TODO - Sort out the following variables
      //The cooldown of the catapult default should be 40 seconds,
      // at 20 seconds the catapult comes back down but at 0 seconds it is refilled.
      //Unless an engineer fills it up first then the cooldown is put to 0 seconds.
      private final int catapultTimer = 800;

      private final int catapultComeDownTimer = 400;

      //the value for aim up / down, which is 20.0 by default.
      private double up_down = 20.0;
      //the value for aim left/right
      private double left_right = 0.0;
      private final Vector lever;
      private final Vector signLeftRight;
      private final Vector signUpDown;
      private final String direction;
      //serves as a third value for the shooting vectors, this makes sure it shoots straight and not backwards.
      private double forwardValueNorthZ = -34.0;
      private double forwardValueEastX = 34.0;
      private double forwardValueWestX = -34.0;
      private double forwardValueSouthZ = 34.0;

      private FallingBlock projectilePassenger;

      private Snowball ball;

      private Player shooter;

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
            this.world = world;
            this.direction = direction;
            this.schematicLocation = location;
            this.signLeftRight = location;

            // Set direction dependent variables
            switch (direction.toLowerCase()) {
                  case "north":
                        // Locations
                        this.lever = new Vector(0, 1, -1).add(location);
                        this.signUpDown = new Vector(0, 0, 1).add(location);
                        this.sound = new Vector(2, 6, -4).add(location);
                        this.projectile = new Vector(2, 6, -5).add(location);
                        this.cobblestone = new Vector(2, 1, 5).add(location);

                        // Schematics
                        this.schematicNormal = "Catapult_Normal_North";
                        this.schematicReloading = "Catapult_Reloading_North";
                        this.schematicShot = "Catapult_Shot_North";
                        break;

                  case "east":
                        // Locations
                        this.lever = new Vector(1, 1, 0).add(location);
                        this.signUpDown = new Vector(-1, 0, 0).add(location);
                        this.sound = new Vector(4, 6, 2).add(location);
                        this.projectile = new Vector(5, 6, 2).add(location);
                        this.cobblestone = new Vector(-5, 1, 2).add(location);

                        // Schematics
                        this.schematicNormal = "Catapult_Normal_East";
                        this.schematicReloading = "Catapult_Reloading_East";
                        this.schematicShot = "Catapult_Shot_East";
                        break;

                  case "south":
                        // Locations
                        this.lever = new Vector(0, 1, 1).add(location);
                        this.signUpDown = new Vector(0, 0, -1).add(location);
                        this.sound = new Vector(-2, 6, 4).add(location);
                        this.projectile = new Vector(-2, 6, 5).add(location);
                        this.cobblestone = new Vector(-2, 1, -5).add(location);

                        // Schematics
                        this.schematicNormal = "Catapult_Normal_South";
                        this.schematicReloading = "Catapult_Reloading_South";
                        this.schematicShot = "Catapult_Shot_South";
                        break;

                  case "west":
                        // Locations
                        this.lever = new Vector(-1, 1, 0).add(location);
                        this.signUpDown = new Vector(1, 0, 0).add(location);
                        this.sound = new Vector(-4, 6, -2).add(location);
                        this.projectile = new Vector(-5, 6, -2).add(location);
                        this.cobblestone = new Vector(5, 1, -2).add(location);

                        // Schematics
                        schematicNormal = "Catapult_Normal_West";
                        schematicReloading = "Catapult_Reloading_West";
                        schematicShot = "Catapult_Shot_West";
                        break;

                  default:
                        throw new IllegalArgumentException("The direction " + direction
                                + " cannot be applied to a catapult!");
            }
      }

      /**
       *
       * @param event This entire event is the catapult animation from the moment you activate the lever.
       */

      @EventHandler
      public void onSwitch(PlayerInteractEvent event) {

            if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.LEVER || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                  return;
            }

            // Make sure the player is playing, and the lever is on the correct map
            if (Objects.equals(Objects.requireNonNull(lever.toLocation(Bukkit.getWorld(world)).getWorld()).getName(), MapController.getCurrentMap().worldName)) {

                  Player player = event.getPlayer();

                  if (event.getClickedBlock().getLocation().distanceSquared(lever.toLocation(Bukkit.getWorld(world))) <= 1) {

                        if (!((Powerable) event.getClickedBlock().getBlockData()).isPowered() && canShoot == true) {

                              //In here do whatever must be done after activating the lever.

                              catapultShot(Bukkit.getWorld(MapController.getCurrentMap().worldName));

                              shootCatapultProjectile(projectile.toLocation(Bukkit.getWorld(world)), direction, Bukkit.getWorld(MapController.getCurrentMap().worldName));

                              canShoot = false;

                              canBeRefilled = false;

                              shooter = player;

                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                    //In here make the catapult come down,
                                          // the catapult should also be able to be refilled by engineers.

                                          Powerable leverData = (Powerable) event.getClickedBlock().getBlockData();

                                          catapultReloading(Bukkit.getWorld(MapController.getCurrentMap().worldName));

                                          if (leverData == null) { return; }

                                          canBeRefilled = true;
                                          canShoot = false;
                                          leverData.setPowered(false);
                                          event.getClickedBlock().setBlockData(leverData);

                                    }

                              }.runTaskLater(Main.plugin, catapultComeDownTimer);

                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                          if (cobblestone.toLocation
                                                  (Bukkit.getWorld(world)).getBlock().getType() == Material.COBBLESTONE_SLAB
                                          || canBeRefilled == false || canShoot == true) {
                                                this.cancel();
                                                return;
                                          }

                                          catapultRefilled(Bukkit.getWorld(MapController.getCurrentMap().worldName));
                                          canShoot = true;
                                          canBeRefilled = false;

                                    }

                              }.runTaskLater(Main.plugin, catapultTimer);

                        } else {

                              player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                              TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "This catapult is still reloading."));
                              event.setCancelled(true);
                        }

                  }

            }

      }

      /**
       *
       * @param world The world the schematic should be spawned in
       */

      private void catapultShot(World world) {

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation.toLocation(Bukkit.getWorld(this.world)), schematicShot, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

            Objects.requireNonNull(world).playSound(
                    sound.toLocation(Bukkit.getWorld(this.world)), Sound.ENTITY_BLAZE_SHOOT , 5, 1 );

      }

      /**
       *
       * @param world The world the schematic should be spawned in
       */

      private void catapultReloading(World world) {

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation.toLocation(Bukkit.getWorld(this.world)), schematicReloading, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

            Objects.requireNonNull(world).playSound(
                    sound.toLocation(Bukkit.getWorld(this.world)), Sound.BLOCK_DISPENSER_DISPENSE , 5, 1);

      }

      /**
       *
       * @param world The world the schematic should be spawned in
       */

      private void catapultRefilled(World world) {

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation.toLocation(Bukkit.getWorld(this.world)), schematicNormal, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

      }

      /**
       *
       * @param event Makes the aim up/down sign of the catapult interactable
       */

      @EventHandler
      public void onClickAimVertical(PlayerInteractEvent event) {

            Block target = event.getClickedBlock();

            Player p = event.getPlayer();

            if (target != null && target.getState() instanceof Sign &&
                    p.getLocation().distance(signUpDown.toLocation(Bukkit.getWorld(world))) <= 4 &&
                    p.getWorld() == Bukkit.getWorld(world)) {

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
       *
       * @param event makes the left/right sign of a catapult interactable
       */
      @EventHandler
      public void onClickAimHorizontal(PlayerInteractEvent event) {

            Block target = event.getClickedBlock();

            Player p = event.getPlayer();

            if (p.getWorld() != Bukkit.getWorld(world)) {
                  return;
            }

            if (target != null && target.getState() instanceof Sign
                    && p.getLocation().distance(signLeftRight.toLocation(Bukkit.getWorld(world))) <= 4
                    && p.getWorld() == Bukkit.getWorld(world)) {

                        Sign sign = (Sign) event.getClickedBlock().getState();

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
       *
       * @param projectileLoc The location from where the projectile should be shot from
       * @param facing This has to be north, south, west or east!
       * @param world the world the projectile should be shot in
       */
     //This is the projectile that needs to be shot and the direction.
      public void shootCatapultProjectile(Location projectileLoc, String facing, World world) {

            if (world == null) {
                  return;
            }

           projectileLoc = projectile.toLocation(Bukkit.getWorld(this.world));

            Byte blockData = 0x0;

           ball = (Snowball) world.spawn(projectile.toLocation(Bukkit.getWorld(this.world)), Snowball.class);

           ball.setShooter(shooter);

           ball.setGravity(true);

           projectilePassenger = (FallingBlock) world.spawnFallingBlock(projectileLoc, Material.COBBLESTONE, blockData);

           projectilePassenger.setDropItem(false);

           ball.addPassenger(projectilePassenger);

            switch (facing) {

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
                          (cobblestone.toLocation(Bukkit.getWorld(world))) < 2) {

                        if (event.getBlock().getType() != Material.COBBLESTONE) {
                              p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                      TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "not cobblestone"));
                              return;
                        }

                        if (canBeRefilled == false) {
                              p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                      TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Catapult is already refilled"));
                              return;
                        }

                        if (canShoot == true) {
                              p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                      TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "canShoot == true"));
                              return;
                        }

                        cobblestone.toLocation(Bukkit.getWorld(world)).getBlock().setType(Material.COBBLESTONE_SLAB);
                        canBeRefilled = false;
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
