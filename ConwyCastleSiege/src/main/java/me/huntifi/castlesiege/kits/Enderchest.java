package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.kits.kits.Kit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class Enderchest implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK &&
				e.getClickedBlock().getType() == Material.ENDER_CHEST){
			Player p = e.getPlayer();
			UUID uuid = p.getUniqueId();

			e.setCancelled(true);
			Kit kit = Kit.equippedKits.get(uuid);
			kit.refillItems(uuid);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
					ChatColor.DARK_GREEN + "Equipment resupplied"));

			// TODO - Prevent deletion of Herugrim
			// Old code to prevent deletion
			/*Bukkit.getScheduler().runTask(Main.plugin, () -> {

			if (Herugrim.containsHerugrim.contains(p)) { if(MapController.currentMapIs("HelmsDeep")) { p.getInventory().addItem(Herugrim.getHerugrim()); }  }
			WoolHat.setHead(p);

			});*/
		}
	}
}
