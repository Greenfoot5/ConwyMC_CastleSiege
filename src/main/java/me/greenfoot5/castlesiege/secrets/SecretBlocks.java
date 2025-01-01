package me.greenfoot5.castlesiege.secrets;

import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class SecretBlocks implements Listener {

    final String helmsDeep1 = "HD_HornSecret";
    final String thunderstone1 = "Thunderstone_Lantern";


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getClickedBlock() == null || player.getGameMode() == GameMode.SPECTATOR) { return; }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.HOPPER) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("HelmsDeep")) {

                    Location hornSecret = new Location(Bukkit.getWorld("HelmsDeep"), 981, 141, 1040);
                    if (e.getClickedBlock().getLocation().equals(hornSecret)) {
                        player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 5, 1);
                        registerFoundSecret(player, helmsDeep1, 75);
                    }
                }

            } else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SOUL_LANTERN) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Thunderstone")) {

                    Location lanternSecret = new Location(Bukkit.getWorld("Thunderstone"), 187, 112, 138);

                    if (e.getClickedBlock().getLocation().equals(lanternSecret)) {
                        registerFoundSecret(player, thunderstone1, 250);
                    }
                }
            }
        }
    }


    public void registerFoundSecret(Player player, String secretName, int coins) {
        UUID uuid = player.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

        if (!data.hasSecret(secretName)) {

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + player.getName() + " has found secret: " + secretName);
            player.sendMessage(ChatColor.GREEN + "You have found a secret! " + ChatColor.YELLOW + "(+" + coins + " coins)");
            data.addCoinsClean(coins);
            data.addFoundSecret(secretName);
            data.addSecret();

        } else {
            player.sendMessage(ChatColor.GREEN + "You have already found this secret!");
        }
    }

}

