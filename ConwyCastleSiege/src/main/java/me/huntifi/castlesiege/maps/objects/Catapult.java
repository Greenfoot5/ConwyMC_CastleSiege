package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.util.Direction;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;


public class Catapult implements Listener {



      private final String name = "";

      private String mapName;

      private World world = Bukkit.getWorld("Abrakhan");

      //In this case the name of the schematic should always be catapult_normal, other two are catapult_reloading and catapultShotSchem.
      private String catapultSchem = "catapult_normal";

      //when it comes back down, so without cobblestone yet.
      private String catapultReloadingSchem = "catapult_reloading";

      //After it shot
      private String catapultShotSchem = "catapultShotSchem";

      //Is the catapult ready?
      private boolean canShoot = true;

      //This is the same location as the location of the aim left/right sign.
      private Location schematicLocation = new Location(Bukkit.getWorld("Abrakhan"), 73, 14, 59);

      //Should basically be the middle of the catapult at the top.
      private Location catapultSoundLocation = new Location(Bukkit.getWorld("Abrakhan"), 75, 19, 56);

      //Should basically be the middle of the catapult at the top but at least 1 block higher or further than the sound loc.
      private Location catapultProjectileLocation = new Location(Bukkit.getWorld("Abrakhan"), 75, 20, 54);

      //Is the catapult ready to be refilled by an engineer?
      private boolean canBeRefilled;

      //The cooldown of the catapult default should be 40 seconds,
      // at 20 seconds the catapult comes back down but at 0 seconds it is refilled.
      //Unless an engineer fills it up first then the cooldown is put to 0 seconds.
      private final int catapultTimer = 800;

      private final int catapultComeDownTimer = 400;

      private Direction direction;

      //the value for aim up / down, which is 20.0 by default.
      private double up_down = 20.0;

      //the value for aim left/right
      private double left_right = 0.0;

      private Location lever = new Location(Bukkit.getWorld("Abrakhan"), 73, 15, 58);

      private Location left_right_sign = new Location(Bukkit.getWorld("Abrakhan"), 73, 14, 59);

      private Location up_down_sign = new Location(Bukkit.getWorld("Abrakhan"), 73, 14, 60);

      //If you wonder what this is it is the location of where the refill happens.
      private Location cobblestone_refill;

      private String catapultFacing = "north";

      //Basically the projectile

      //serves as a third value for the shooting vectors, this makes sure it shoots straight and not backwards.
      private double forwardValueNorthZ = -25;

      private double forwardValueEastX = 25;

      private double forwardValueWestX = -25;

      private double forwardValueSouthZ = 25;

      @EventHandler
      public void onSwitch(PlayerInteractEvent event) {

            if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.LEVER) {
                  return;
            }

            // Make sure the player is playing, and the lever is on the correct map
            if (Objects.equals(Objects.requireNonNull(lever.getWorld()).getName(), MapController.getCurrentMap().worldName)) {

                  Player player = event.getPlayer();

                  if (event.getClickedBlock().getLocation().distanceSquared(lever) <= 1) {

                        if (!((Powerable) event.getClickedBlock().getBlockData()).isPowered() && canShoot == true) {

                              //In here do whatever must be done after activating the lever.

                              catapultShot(Bukkit.getWorld("Abrakhan"));

                              shootCatapultProjectile(catapultProjectileLocation, "north", Bukkit.getWorld("Abrakhan"));

                              canShoot = false;

                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                    //In here make the catapult come down,
                                          // the catapult should also be able to be refilled by engineers.

                                          Powerable leverData = (Powerable) event.getClickedBlock().getBlockData();

                                          catapultReloading(Bukkit.getWorld("Abrakhan"));

                                          if (leverData == null) { return; }

                                          leverData.setPowered(false);
                                          event.getClickedBlock().setBlockData(leverData);

                                    }

                              }.runTaskLater(Main.plugin, catapultComeDownTimer);


                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                          catapultRefilled(Bukkit.getWorld("Abrakhan"));
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


      private void catapultShot(World world) {

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation, catapultShotSchem, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

            Objects.requireNonNull(world).playSound(
                    catapultSoundLocation, Sound.ENTITY_BLAZE_SHOOT , 5, 1 );

      }

      private void catapultReloading(World world) {

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation, catapultReloadingSchem, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

            Objects.requireNonNull(world).playSound(
                    catapultSoundLocation, Sound.BLOCK_DISPENSER_DISPENSE , 5, 1);

      }

      private void catapultRefilled(World world) {

            try {
                  SchematicSpawner.spawnSchematic(schematicLocation, catapultSchem, world.getName());
            } catch (WorldEditException e) {
                  e.printStackTrace();
            }

      }

      @EventHandler
      public void onClickAimVertical(PlayerInteractEvent event) {

            Block target = event.getClickedBlock();

            Player p = event.getPlayer();

            if (target != null && target.getState() instanceof Sign && p.getLocation().distance(up_down_sign) <= 4) {

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

      @EventHandler
      public void onClickAimHorizontal(PlayerInteractEvent event) {

            Block target = event.getClickedBlock();

            Player p = event.getPlayer();

            if (target != null && target.getState() instanceof Sign && p.getLocation().distance(left_right_sign) <= 4) {

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


     //This is the projectile that needs to be shot and the direction.
      public void shootCatapultProjectile(Location projectileLoc, String facing, World world) {

            if (world == null) {
                  return;
            }

      projectileLoc = catapultProjectileLocation;

            Byte blockData = 0x0;

            Snowball projectile = (Snowball) world.spawnEntity(projectileLoc, EntityType.SNOWBALL);

            MiscDisguise cobbleDisguise = new MiscDisguise(DisguiseType.FALLING_BLOCK);
            FallingBlockWatcher watcher = (FallingBlockWatcher) cobbleDisguise.getWatcher();
            ItemStack cobble = new ItemStack(Material.COBBLESTONE);
            watcher.setNoGravity(false);
            watcher.setYawLocked(true);
            watcher.setBlock(cobble);
            cobbleDisguise.setEntity(projectile);
            cobbleDisguise.startDisguise();

            switch (facing) {

                  case "north":

                        double vecX = left_right;

                        double vecZ = forwardValueNorthZ;

                        double vecY = up_down;

                        Vector v = new Vector(vecX, vecY, vecZ);

                        projectile.setVelocity(v);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.5));

                        break;

                  case "east":

                        double vecX2 = projectileLoc.getZ();

                        double vecY2 = up_down;

                        double vecZ2 = left_right;

                        Vector v2 = new Vector(vecX2, vecY2, vecZ2);

                        projectile.setVelocity(v2);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.5));


                        break;

                  case "west":

                        double vecZ3 = left_right;

                        double vecX3 = projectileLoc.getX();

                        double vecY3 = up_down;

                        Vector v3 = new Vector(vecX3, vecY3, vecZ3);

                        projectile.setVelocity(v3);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.5));
                        break;

                  case "south":

                        double vecY4 = up_down;

                        double vecX4 = left_right;

                        double vecZ4 = forwardValueSouthZ;

                        Vector v4 = new Vector(vecX4, vecY4, vecZ4);

                        projectile.setVelocity(v4);
                        projectile.setVelocity(projectile.getVelocity().normalize().multiply(3.5));

                        break;


                  default:
                        break;
            }

      }

      @EventHandler
      public void onImpact(ProjectileHitEvent event) {

            if (event.getEntity() instanceof Snowball) {

                  Snowball ball = (Snowball) event.getEntity();

                  if (world == null) {
                    return;
                  }

                  if (ball.getShooter() instanceof Player) {
                        return;
                  }

                  event.getHitBlock().getWorld().createExplosion(event.getHitBlock().getLocation(), 5F, false, true);
            }


      }

}
