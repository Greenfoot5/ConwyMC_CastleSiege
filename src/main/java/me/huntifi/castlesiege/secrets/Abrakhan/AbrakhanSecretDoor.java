package me.huntifi.castlesiege.secrets.Abrakhan;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles the secret door on Abrakhan
 */
public class AbrakhanSecretDoor implements Listener {

    boolean isRedDoorOpen;

    boolean isTorchDoorOpen;

    final Location doorLoc = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 123, 49, -113);
    final Location redTorchLoc = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 126, 50, -116);

    final Location torchLoc = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 116, 39, -121);

    /**
     * Checks if the player interacted with a redstone torch
     * @param event Called when a player interacts with anything
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event){


        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(event.getClickedBlock().getType().equals(Material.REDSTONE_WALL_TORCH) && event.getClickedBlock().getLocation().equals(redTorchLoc)) {

                Player p = event.getPlayer();


                if (!isRedDoorOpen) {

                    Messenger.sendSecret("You have opened a secret door!", p);
                    isRedDoorOpen = true;
                    openRedstoneDoor();

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            isRedDoorOpen = false;
                            closeRedstoneDoor();
                        }
                    }.runTaskLater(Main.plugin, 60);


                }
            } else if (event.getClickedBlock().getType().equals(Material.WALL_TORCH) && event.getClickedBlock().getLocation().equals(torchLoc)) {
                Player p = event.getPlayer();
                if (!isTorchDoorOpen) {
                    Messenger.sendSecret("You have opened a secret door!", p);
                    isTorchDoorOpen = true;
                    openTorchDoor();

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            isTorchDoorOpen = false;
                            closeTorchDoor();
                        }
                    }.runTaskLater(Main.plugin, 60);


                }
            }
        }
    }


    /**
     * Opens the redstone door
     */
    public void openRedstoneDoor() {

        Bukkit.getWorld("Abrakhan").playSound(doorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN , 1, 2);
        Location Block1 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 123, 49, -113);
        Block1.getBlock().setType(Material.AIR);
        Location Block2 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 123, 48, -113);
        Block2.getBlock().setType(Material.AIR);


    }


    /**
     * Closes the redstone door
     */
    public void closeRedstoneDoor() {

        Bukkit.getWorld("Abrakhan").playSound(doorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN , 1, 2);
        Location Block1 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 123, 49, -113);
        Block1.getBlock().setType(Material.SANDSTONE);
        Location Block2 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 123, 48, -113);
        Block2.getBlock().setType(Material.SANDSTONE);
    }

    /**
     * Opens the coal torch door
     */
    public void openTorchDoor() {

        Bukkit.getWorld("Abrakhan").playSound(doorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN , 1, 2);
        Location Block1 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 116, 39, -124);
        Block1.getBlock().setType(Material.AIR);
        Location Block2 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 116, 38, -124);
        Block2.getBlock().setType(Material.AIR);


    }


    /**
     * Closes the coal torch door
     */
    public void closeTorchDoor() {

        Bukkit.getWorld("Abrakhan").playSound(doorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN , 1, 2);
        Location Block1 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 116, 39, -124);
        Block1.getBlock().setType(Material.SANDSTONE);
        Location Block2 = new Location(Main.plugin.getServer().getWorld("Abrakhan"), 116, 38, -124);
        Block2.getBlock().setType(Material.SANDSTONE);
    }
}
