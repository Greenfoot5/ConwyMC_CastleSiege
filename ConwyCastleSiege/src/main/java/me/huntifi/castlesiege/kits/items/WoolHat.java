package me.huntifi.castlesiege.kits.items;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The wool block worn as hat by all players
 */
public class WoolHat {

	/**
	 * Sets a wool block with a player's team's color as their hat
	 * @param player The player for whom to set the wool hat
	 */
	public static void setHead(Player player) {
		Team team = MapController.getCurrentMap().getTeam(player.getUniqueId());

		ItemStack wool = new ItemStack(team.primaryWool);
		ItemMeta woolMeta = wool.getItemMeta();
		assert woolMeta != null;
		woolMeta.setDisplayName(ChatColor.GREEN + "WoolHat");
		wool.setItemMeta(woolMeta);
		player.getInventory().setHelmet(wool);
	}
}
