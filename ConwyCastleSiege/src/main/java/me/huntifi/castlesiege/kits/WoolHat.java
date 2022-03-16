package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.huntifi.castlesiege.maps.MapController;

import java.util.UUID;

public class WoolHat {

	public static void setHead(Player player) {
		Team team = MapController.getCurrentMap().getTeam(player.getUniqueId());

		ItemStack wool = new ItemStack(team.primaryWool);
		ItemMeta woolMeta = wool.getItemMeta();
		woolMeta.setDisplayName(ChatColor.GREEN + "WoolHat");
		player.getInventory().setHelmet(wool);
	}
}
