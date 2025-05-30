package me.greenfoot5.castlesiege.secrets.Helmsdeep;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.conwymc.util.Messenger;
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

public class SecretDoor implements Listener {

    boolean isOpen;

    final Location doorLoc = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 61, 982);

    final Location chestLoc = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 992, 62, 983);

    final Location stoneButtonLoc = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 63, 979);

    @EventHandler
    public void onInteract(PlayerInteractEvent event){


        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(event.getClickedBlock().getType().equals(Material.CHEST) && event.getClickedBlock().getLocation().equals(chestLoc)) {

                Player p = event.getPlayer();
                if (!isOpen) {

                    Messenger.sendSecret("You have opened a secret door!", p);
                    isOpen = true;
                    openDoor();

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            isOpen = false;
                            closeDoor();
                        }
                    }.runTaskLater(Main.plugin, 60);
                }
            }
        }
    }


    public void openDoor() {

        Bukkit.getWorld("HelmsDeep").playSound(doorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN , 1, 2);
        Location Block1 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 62, 981);
        Block1.getBlock().setType(Material.AIR);
        Location Block2 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 63, 981);
        Block2.getBlock().setType(Material.AIR);
        Location Block3 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 64, 981);
        Block3.getBlock().setType(Material.AIR);
        Location Block4 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 65, 981);
        Block4.getBlock().setType(Material.AIR);
        Location Block5 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 62, 981);
        Block5.getBlock().setType(Material.AIR);
        Location Block6 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 63, 981);
        Block6.getBlock().setType(Material.AIR);
        Location Block7 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 64, 981);
        Block7.getBlock().setType(Material.AIR);
        Location Block8 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 65, 981);
        Block8.getBlock().setType(Material.AIR);

    }


    public void closeDoor() {

        Bukkit.getWorld("HelmsDeep").playSound(doorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN , 1, 2);
        Location Block1 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 62, 981);
        Block1.getBlock().setType(Material.STONE);
        Location Block2 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 63, 981);
        Block2.getBlock().setType(Material.STONE);
        Location Block3 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 64, 981);
        Block3.getBlock().setType(Material.STONE);
        Location Block4 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 989, 65, 981);
        Block4.getBlock().setType(Material.STONE);
        Location Block5 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 62, 981);
        Block5.getBlock().setType(Material.STONE);
        Location Block6 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 63, 981);
        Block6.getBlock().setType(Material.STONE);
        Location Block7 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 64, 981);
        Block7.getBlock().setType(Material.STONE);
        Location Block8 = new Location(Main.plugin.getServer().getWorld("HelmsDeep"), 990, 65, 981);
        Block8.getBlock().setType(Material.STONE);
    }


    @EventHandler
    public void onInteract2(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(event.getClickedBlock().getType().equals(Material.STONE_BUTTON) && event.getClickedBlock().getLocation().equals(stoneButtonLoc)) {

                Player p = event.getPlayer();

                if (!isOpen) {
                    Messenger.sendSuccess("You have reopened the secret door!", p);
                    isOpen = true;
                    openDoor();

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            isOpen = false;
                            closeDoor();
                        }
                    }.runTaskLater(Main.plugin, 60);
                }
            }
        }
    }
}
