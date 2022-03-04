package me.huntifi.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Enderchest implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.ENDER_CHEST)) {
				Player p = event.getPlayer();
				if (!LobbyPlayer.containsPlayer(p)) {
					event.setCancelled(true);
					EnderchestRefill.refill(p);

					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Equipment resupplied"));
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

}
