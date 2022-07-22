package me.huntifi.castlesiege.secrets;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TimerState;
import me.huntifi.castlesiege.maps.WoolMapBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

public class SecretSigns implements Listener {

    String helmsDeep1 = "HD_HillSecret";
    String helmsDeep2 = "HD_Herugrim";
    String skyhold1 = "Skyhold_Vault";
    String thunderstone1 = "Thunderstone_Island";
    String thunderstone2 = "Thunderstone_Huntifi";
    String thunderstone3 = "Thunderstone_Skyview";
    String thunderstone4 = "Thunderstone_Cookie";
    String thunderstone5 = "Thunderstone_Fall";


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getClickedBlock() == null) { return; }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

        if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Sign) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("HelmsDeep")) {

                    Location hillSecret = new Location(Bukkit.getWorld("HelmsDeep"), 900, 62, 1116);
                    Location herugrimSecret = new Location(Bukkit.getWorld("HelmsDeep"), 980, 58, 986);

                    if (e.getClickedBlock().getLocation().equals(hillSecret)) {
                       registerFoundSecret(player, helmsDeep1);
                    } else if (e.getClickedBlock().getLocation().equals(herugrimSecret)) {
                        registerFoundSecret(player, helmsDeep2);
                    }
                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Skyhold")) {
                    Location vaultSecret = new Location(Bukkit.getWorld("Skyhold"), 1660, 94, -118);

                    if (e.getClickedBlock().getLocation().equals(vaultSecret)) {
                        registerFoundSecret(player, skyhold1);
                    }

                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Thunderstone")) {
                    Location islandSecret = new Location(Bukkit.getWorld("Thunderstone"), 112, 16, 75);
                    Location huntifiSecret = new Location(Bukkit.getWorld("Thunderstone"), 88, 80, 107);
                    Location skyviewSecret = new Location(Bukkit.getWorld("Thunderstone"), 168, 170, 72);
                    Location cookieSecret = new Location(Bukkit.getWorld("Thunderstone"), 233, 68, 78);
                    Location fallSecret = new Location(Bukkit.getWorld("Thunderstone"), 189, 130, 75);

                    if (e.getClickedBlock().getLocation().equals(islandSecret)) {
                        registerFoundSecret(player, thunderstone1);
                    } else if (e.getClickedBlock().getLocation().equals(huntifiSecret)) {
                        registerFoundSecret(player, thunderstone2);
                    } else if (e.getClickedBlock().getLocation().equals(skyviewSecret)) {
                        registerFoundSecret(player, thunderstone3);
                    } else if (e.getClickedBlock().getLocation().equals(cookieSecret)) {
                        registerFoundSecret(player, thunderstone4);
                    } else if (e.getClickedBlock().getLocation().equals(fallSecret)) {
                        registerFoundSecret(player, thunderstone5);
                    }

                }
            }
        }
    }


    public void registerFoundSecret(Player player, String secretName) {
        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!LoadData.getFoundSecrets(uuid).contains(secretName)) {

                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + player.getName() + " has found secret: " + secretName);
                    player.sendMessage(ChatColor.GREEN + "You have found a secret! " + ChatColor.YELLOW + "(+50 coins)");
                    ActiveData.getData(uuid).addCoins(50);
                    try {
                        StoreData.addFoundSecret(player.getUniqueId(), secretName);
                        ActiveData.getData(uuid).addSecret();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                } else {
                    player.sendMessage(ChatColor.GREEN + "You have already found this secret!");
                }
            }
        }.runTaskAsynchronously(Main.plugin);

    }

}
