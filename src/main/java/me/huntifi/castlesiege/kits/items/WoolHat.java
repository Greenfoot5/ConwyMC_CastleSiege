package me.huntifi.castlesiege.kits.items;

import me.huntifi.castlesiege.events.curses.CurseExpired;
import me.huntifi.castlesiege.events.curses.GreaterBlindnessCurse;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.UUID;

/**
 * The wool block worn as hat by all players
 */
public class WoolHat implements Listener {
	private static boolean hideTeamColour = false;

	/**
	 * Sets a wool block with a player's team's color as their hat
	 * @param player The player for whom to set the wool hat
	 */
	public static void setHead(Player player) {
		Team team = TeamController.getTeam(player.getUniqueId());

		ItemStack wool;
		if (hideTeamColour)
			wool = new ItemStack(team.primaryWool);
		else
			wool = new ItemStack(Material.WHITE_WOOL);
		ItemMeta woolMeta = wool.getItemMeta();
		assert woolMeta != null;
		woolMeta.setDisplayName(ChatColor.GREEN + "WoolHat");
		wool.setItemMeta(woolMeta);
		player.getInventory().setHelmet(wool);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void greaterBlindnessActive(GreaterBlindnessCurse curse) {
		hideTeamColour = true;

		for (Team t : MapController.getCurrentMap().teams) {
			for (UUID uuid : t.getPlayers()) {
				setHead(Bukkit.getPlayer(uuid));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void bindingExpired(CurseExpired curse) {
		if (Objects.equals(curse.getDisplayName(), GreaterBlindnessCurse.name)) {
			hideTeamColour = false;
		} else {
			return;
		}

		for (Team t : MapController.getCurrentMap().teams) {
			for (UUID uuid : t.getPlayers()) {
				setHead(Bukkit.getPlayer(uuid));
			}
		}
	}
}
