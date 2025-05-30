package me.greenfoot5.castlesiege.kits.items;

import me.greenfoot5.castlesiege.events.curses.CurseExpired;
import me.greenfoot5.castlesiege.events.curses.GreaterBlindnessCurse;
import me.greenfoot5.castlesiege.events.curses.TrueBlindnessCurse;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

/**
 * The wool block worn as hat by all players
 */
public class WoolHat implements Listener {
	private static boolean hideTeamColour = false;
	public static boolean trueHideTeamColour = false;

	/**
	 * Sets a wool block with a player's team's color as their hat
	 * @param player The player for whom to set the wool hat
	 */
	public static void setHead(@Nullable Player player) {
		if (player == null)
			return;

		Team team = TeamController.getTeam(player.getUniqueId());

		ItemStack wool;
		if (hideTeamColour || trueHideTeamColour)
			wool = new ItemStack(Material.WHITE_WOOL);
		else
			wool = new ItemStack(team.primaryWool);
		ItemMeta woolMeta = wool.getItemMeta();
		assert woolMeta != null;
		woolMeta.displayName(Component.text("WoolHat", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
		wool.setItemMeta(woolMeta);
		player.getInventory().setHelmet(wool);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void greaterBlindnessActive(GreaterBlindnessCurse curse) {
		hideTeamColour = true;

		for (UUID uuid : TeamController.getPlayers()) {
			setHead(Bukkit.getPlayer(uuid));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void beginHidingAll(TrueBlindnessCurse curse) {
		trueHideTeamColour = true;

		for (UUID uuid : TeamController.getPlayers()) {
			setHead(Bukkit.getPlayer(uuid));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void blindnessExpired(CurseExpired curse) {
		if (Objects.equals(curse.getDisplayName(), GreaterBlindnessCurse.name)) {
			hideTeamColour = false;
		} else if (Objects.equals(curse.getDisplayName(), TrueBlindnessCurse.name)) {
			trueHideTeamColour = false;
		} else {
			return;
		}

		for (UUID uuid : TeamController.getPlayers()) {
			setHead(Bukkit.getPlayer(uuid));
		}
	}
}
