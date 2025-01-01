package me.greenfoot5.castlesiege.secrets.Thunderstone;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SecretPortal implements Listener {

    final Location eTableLoc = new Location(Main.plugin.getServer().getWorld("Thunderstone"), 166, 65, 82);

    final Location portalLoc = new Location(Main.plugin.getServer().getWorld("Thunderstone"), 184, 113, 133);

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE) && event.getClickedBlock().getLocation().equals(eTableLoc)) {

                Player p = event.getPlayer();
                    Messenger.sendSuccess("You have found a secret portal!", p);
                    p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 4F, 3F);
                    p.teleport(portalLoc);
            }
        }
    }
}
