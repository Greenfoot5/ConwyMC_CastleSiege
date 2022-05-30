package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.util.Direction;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;


public class Catapults implements Listener {

      private final String name = "";

      private String mapName;

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

                              canShoot = false;

                              new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                    //In here make the catapult come down,
                                          // the catapult should also be able to be refilled by engineers.

                                          Powerable leverData = (Powerable) event.getClickedBlock().getBlockData();
                                          if (leverData.isPowered())
                                                return;

                                          catapultReloading(Bukkit.getWorld("Abrakhan"));

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

}
