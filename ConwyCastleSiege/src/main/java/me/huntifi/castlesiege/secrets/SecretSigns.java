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


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) throws SQLException {

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
                    player.sendMessage(ChatColor.GREEN + "You have found a secret!");
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
