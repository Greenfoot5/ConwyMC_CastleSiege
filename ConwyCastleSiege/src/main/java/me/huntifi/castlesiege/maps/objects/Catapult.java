package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.util.Direction;
import me.huntifi.castlesiege.Main;
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
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;


public class Catapult implements Listener {

      private final String name;

      private String worldName;

      //In this case the name of the schematic should always be catapult_normal, other two are catapult_reloading and catapultShotSchem.
      private String catapultSchem;

      //when it comes back down, so without cobblestone yet.
      private String catapultReloadingSchem;

      //After it shot
      private String catapultShotSchem;

      //Is the catapult ready?
      private boolean canShoot = true;

      //This is the same location as the location of the aim left/right sign.
      private Vector schematicLocation;

      //Should basically be the middle of the catapult at the top.
      private Vector catapultSoundLocation;

      //Should basically be the middle of the catapult at the top but at least 1 block higher or further than the sound loc.
      private Vector catapultProjectileLocation;

      //Is the catapult ready to be refilled by an engineer?
      private boolean canBeRefilled;

      //The cooldown of the catapult default should be 40 seconds,
      // at 20 seconds the catapult comes back down but at 0 seconds it is refilled.
      //Unless an engineer fills it up first then the cooldown is put to 0 seconds.
      private final int catapultTimer = 800;

      private final int catapultComeDownTimer = 400;

      //the value for aim up / down, which is 20.0 by default.
      private double up_down = 20.0;
      //the value for aim left/right
      private double left_right = 0.0;
      private Vector lever;
      private Vector left_right_sign;
      private Vector up_down_sign;
      //If you wonder what this is it is the location of where the refill happens.
      private Vector cobblestone_refill;
      private String catapultFacing;
      //serves as a third value for the shooting vectors, this makes sure it shoots straight and not backwards.
      private double forwardValueNorthZ = -30.0;
      private double forwardValueEastX = 30.0;
      private double forwardValueWestX = -30.0;
      private double forwardValueSouthZ = 30.0;


      /**
       * Creates a new catapult
       * @param displayName The display name of the gate
       */
      public Catapult(String displayName) {
            this.name = displayName;
      }

      public String getName() {
            return name;
      }


      /**
       *
       * @param world the world name for bukkit.getWorld
       */
      public void setWorldName(String world) {

            this.worldName = world;

      }

      public String getWorldName() { return worldName; }

      /**
       *
       * @param direction This has to be defined as "north" or "east" or "west" or "south"
       */
      public void setCatapultFacing(String direction) {
            this.catapultFacing = direction;
      }

      public String getCatapultDirection() {
            return catapultFacing;
      }

      /**
       *
       * @param leverLoc Location of the lever
       */
      public void setLeverLocation(Vector leverLoc) {
            this.lever = leverLoc;
      }

      public Vector getleverLocation() {  return lever; }

      /**
       *
       * @param leverLoc Location of the schematic
       */
      public void setSchematicLocation(Vector schemLoc) {
            this.schematicLocation = schemLoc;
      }

      public Vector getSchematicLocation() {  return schematicLocation; }

      /**
       *
       * @param leverLoc Location of the catapult sounds
       */
      public void setSoundLocation(Vector soundLoc) {
            this.catapultSoundLocation = soundLoc;
      }

      public Vector getsoundLocation() {  return catapultSoundLocation; }

      /**
       *
       * @param leverLoc Location of the catapult projectile, its initial point.
       */
      public void setProjectileLocation(Vector projectileLoc) {
            this.catapultProjectileLocation = projectileLoc;
      }

      public Vector getProjectileLocation() {  return catapultProjectileLocation; }

      /**
       * @param leverLoc Location of the Up/Down sign
       */
      public void setUpDownLocation(Vector upDownSignLocation) {
            this.up_down_sign = upDownSignLocation;
      }

      public Vector getupdownLocation() {  return up_down_sign; }

      /**
       * @param leverLoc Location of the Aim Right/Left sign
       */
      public void setRightLeftLocation(Vector rightLeftSignLocation) {
            this.left_right_sign = rightLeftSignLocation;
      }

      public Vector getrightleftLocation() {  return left_right_sign; }

      /**
       *
       * @param schematicName the schematic when the catapult is ready to fire
       * @param location schem spawn loc
       */
      public void setCatapultSchematic(String schematicName, Vector location) {
            this.catapultSchem = schematicName;
            this.schematicLocation = location;
      }

      /**
       *
       * @param schematicName the schematic when the catapult is reloading
       * @param location schem spawn loc
       */
      public void setCatapultReloadingSchematic(String schematicName, Vector location) {
            this.catapultReloadingSchem = schematicName;
            this.schematicLocation = location;
      }

      /**
       *
       * @param schematicName the schematic when the catapult shoots/has shot
       * @param location schem spawn loc
       */
      public void setCatapultShootSchematic(String schematicName, Vector location) {
            this.catapultShotSchem = schematicName;
            this.schematicLocation = location;
      }

      /**
       *
       * @param event This entire event is the catapult animation from the moment you activate the lever.
       */

      @EventHandler
      public void onSwitch(PlayerInteractEvent event) {

            if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.LEVER) {
                  return;
            }

            // Make sure the player is playing, and the lever is on the correct map
            if (Objects.equals(Objects.requireNonNull(lever.toLocation(Bukkit.getWorld(worldName)).getWorld()).getName(), MapController.getCurrentMap().worldName)) {

                  Player player = event.getPlayer();

                  if (event.getClickedBlock().getLocation().distanceSquared(lever.toLocation(Bukkit.getWorld(worldName))) <= 1) {

                        if (!((Powerable) event.getClickedBlock().getBlockData()).isPowered() && canShoot == true) {

                              //In here do whatever must be done after activating the lever.

                              catapultShot(Bukkit.getWorld(MapController.getCurrentMap().worldName));

                              shootCatapultProjectile(catapultProjectileLocation.toLocation(Bukkit.getWorld(worldName)), catapultFacing, Bukkit.getWorld(MapController.getCurrentMap().worldName));

                              canShoot = false;

                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                    //In here make the catapult come down,
                                          // the catapult should also be able to be refilled by engineers.

                                          Powerable leverData = (Powerable) event.getClickedBlock().getBlockData();

                                          catapultReloading(Bukkit.getWorld(MapController.getCurrentMap().worldName));

                                          if (leverData == null) { return; }

                                          leverData.setPowered(false);
                                          event.getClickedBlock().setBlockData(leverData);

                                    }

                              }.runTaskLater(Main.plugin, catapultComeDownTimer);


                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                          catapultRefilled(Bukkit.getWorld(MapController.getCurrentMap().worldName));
                                          canShoot = true;

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

            if (catapultFacing.equalsIgnoreCase("north")) { catapultShotSchem = "Catapult_Shot_North"; }
            if (catapultFacing.equalsIgnoreCase("south")) { catapultShotSchem = "Catapult_Shot_South"; }
            if (catapultFacing.equalsIgnoreCase("east")) { catapultShotSchem = "Catapult_Shot_East"; }
            if (catapultFacing.equalsIgnoreCase("west")) { catapultShotSchem = "Catapult_Shot_West"; }

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation.toLocation(Bukkit.getWorld(worldName)), catapultShotSchem, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

            Objects.requireNonNull(world).playSound(
                    catapultSoundLocation.toLocation(Bukkit.getWorld(worldName)), Sound.ENTITY_BLAZE_SHOOT , 5, 1 );

      }

      /**
       *
       * @param world The world the schematic should be spawned in
       */

      private void catapultReloading(World world) {

            if (catapultFacing.equalsIgnoreCase("north")) { catapultReloadingSchem = "Catapult_Reloading_North"; }
            if (catapultFacing.equalsIgnoreCase("south")) { catapultReloadingSchem = "Catapult_Reloading_South"; }
            if (catapultFacing.equalsIgnoreCase("east")) { catapultReloadingSchem = "Catapult_Reloading_East"; }
            if (catapultFacing.equalsIgnoreCase("west")) { catapultReloadingSchem = "Catapult_Reloading_West"; }

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation.toLocation(Bukkit.getWorld(worldName)), catapultReloadingSchem, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

            Objects.requireNonNull(world).playSound(
                    catapultSoundLocation.toLocation(Bukkit.getWorld(worldName)), Sound.BLOCK_DISPENSER_DISPENSE , 5, 1);

      }

      /**
       *
       * @param world The world the schematic should be spawned in
       */

      private void catapultRefilled(World world) {

            if (catapultFacing.equalsIgnoreCase("north")) { catapultSchem = "Catapult_Normal_North"; }
            if (catapultFacing.equalsIgnoreCase("south")) { catapultSchem = "Catapult_Normal_South"; }
            if (catapultFacing.equalsIgnoreCase("east")) { catapultSchem = "Catapult_Normal_East"; }
            if (catapultFacing.equalsIgnoreCase("west")) { catapultSchem = "Catapult_Normal_West"; }

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation.toLocation(Bukkit.getWorld(worldName)), catapultSchem, world.getName());
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

            if (target != null && target.getState() instanceof Sign && p.getLocation().distance(up_down_sign.toLocation(Bukkit.getWorld(worldName))) <= 4) {

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

            if (target != null && target.getState() instanceof Sign && p.getLocation().distance(left_right_sign.toLocation(Bukkit.getWorld(worldName))) <= 4) {

                        Sign sign = (Sign) event.getClickedBlock().getState();

                        if (sign.getLine(0).equalsIgnoreCase("Aim left/right")) {

                              left_right = Double.parseDouble(sign.getLine(2));

                              if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                                    if (left_right < 45) {

                                          left_right = Double.parseDouble(sign.getLine(2)) + 0.5;
                                          sign.setLine(2, left_right + "");
                                          sign.update();

                                    }
                              }

                              if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                                    if (left_right > -45) {

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

           projectileLoc = catapultProjectileLocation.toLocation(Bukkit.getWorld(worldName));

            Byte blockData = 0x0;

           Snowball projectile = (Snowball) world.spawn(catapultProjectileLocation.toLocation(Bukkit.getWorld(worldName)), Snowball.class);

           projectile.setGravity(true);

           FallingBlock projectilePassenger = (FallingBlock) world.spawnFallingBlock(projectileLoc, Material.COBBLESTONE, blockData);

           projectilePassenger.setDropItem(false);

           projectile.addPassenger(projectilePassenger);

            switch (facing) {

                  case "north":

                        double vecX = left_right;

                        double vecZ = forwardValueNorthZ;

                        double vecY = up_down;

                        Vector v = new Vector(vecX, vecY, vecZ);

                        projectile.setVelocity(v);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.0));

                        break;

                  case "east":

                        double vecX2 = forwardValueEastX;

                        double vecY2 = up_down;

                        double vecZ2 = left_right;

                        Vector v2 = new Vector(vecX2, vecY2, vecZ2);

                        projectile.setVelocity(v2);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.0));


                        break;

                  case "west":

                        double vecZ3 = left_right;

                        double vecX3 = forwardValueWestX;

                        double vecY3 = up_down;

                        Vector v3 = new Vector(vecX3, vecY3, vecZ3);

                        projectile.setVelocity(v3);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.0));
                        break;

                  case "south":

                        double vecY4 = up_down;

                        double vecX4 = left_right;

                        double vecZ4 = forwardValueSouthZ;

                        Vector v4 = new Vector(vecX4, vecY4, vecZ4);

                        projectile.setVelocity(v4);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.0));

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

                  Snowball ball = (Snowball) event.getEntity();

                  if (ball.getShooter() instanceof Player) {
                        return;
                  }

                  if (ball.getPassengers() == null) {
                        return;
                  } else {
                        ball.getPassengers().remove(0);
                  }

                  event.getHitBlock().getWorld().createExplosion(event.getHitBlock().getLocation(), 2F, false, true);

            }


      }

      /**
       *
       * @param event This event makes sure the falling block doesn't turn into a block.
       */
      @EventHandler
      public void onImpact(EntityChangeBlockEvent event) {
            if (event.getEntity() instanceof FallingBlock) {
              event.setCancelled(true);
            }
      }


      /**
       *
       * @param event this event causes a little nice explosion animation.
       */
      @EventHandler
      public void onEntityExplode(EntityExplodeEvent event) {

        for (Block b : event.blockList()) {

              float x = (float) -3 + (float) (Math.random() *((3-3) + 1));
              float y = (float) -3 + (float) (Math.random() *((3-3) + 1));
              float z = (float) -3 + (float) (Math.random() *((3-3) + 1));

              FallingBlock fallingblock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
              fallingblock.setDropItem(false);
              fallingblock.setVelocity(new Vector(x,y,z));
              b.setType(Material.AIR);

        }


      }

}
