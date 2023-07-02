package me.huntifi.castlesiege.secrets.Thunderstone;

import me.huntifi.castlesiege.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SecretPortal implements Listener {

    final Location etableLoc = new Location(Main.plugin.getServer().getWorld("Thunderstone"), 166, 65, 82);

    final Location portalLoc = new Location(Main.plugin.getServer().getWorld("Thunderstone"), 184, 113, 133);

    @EventHandler
    public void onInteract(PlayerInteractEvent event){


        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE) && event.getClickedBlock().getLocation().equals(etableLoc)) {

                Player p = event.getPlayer();

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "You have found a secret portal!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 4F, 3F);
                    p.teleport(portalLoc);

            }
        }
    }

}
