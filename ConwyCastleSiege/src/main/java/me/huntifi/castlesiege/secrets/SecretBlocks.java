package me.huntifi.castlesiege.secrets;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

public class SecretBlocks implements Listener {

    String helmsDeep1 = "HD_HornSecret";
    String thunderstone1 = "Thunderstone_Lantern";


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getClickedBlock() == null) { return; }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.HOPPER) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("HelmsDeep")) {

                    Location hornSecret = new Location(Bukkit.getWorld("HelmsDeep"), 981, 141, 1040);
                    if (e.getClickedBlock().getLocation().equals(hornSecret)) {
                        player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 20F, 3F);
                        registerFoundSecret(player, helmsDeep1);
                    }
                }

            } else if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SOUL_LANTERN) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Thunderstone")) {

                    Location lanternSecret = new Location(Bukkit.getWorld("Thunderstone"), 187, 112, 138);

                    if (e.getClickedBlock().getLocation().equals(lanternSecret)) {
                        registerFoundSecret(player, thunderstone1);
                    }
                }
            }
        }
    }


    public void registerFoundSecret(Player player, String secretName) {
        UUID uuid = player.getUniqueId();

        PlayerData data = ActiveData.getData(uuid);

        if (!data.getFoundSecrets().contains(secretName)) {

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + player.getName() + " has found secret: " + secretName);
            player.sendMessage(ChatColor.GREEN + "You have found a secret! " + ChatColor.YELLOW + "(+50 coins)");
            data.addCoins(50);
            data.addFoundSecret(secretName);
            data.addSecret();

        } else {
            player.sendMessage(ChatColor.GREEN + "You have already found this secret!");
        }
    }

}

