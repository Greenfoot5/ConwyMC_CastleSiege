package me.huntifi.castlesiege.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class Woolheads {

	public static void setHead(Player p) {

		if (currentMaps.getCurrentMap().equalsIgnoreCase("HelmsDeep")) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {

				p.getInventory().clear();
				ItemStack Wool = new ItemStack(Material.BLACK_WOOL);
				ItemMeta WoolMeta = Wool.getItemMeta();
				WoolMeta.setDisplayName(ChatColor.GREEN + "Woolhead");
				p.getInventory().setHelmet(Wool);
			}

			if (PlayerTeam.playerIsInTeam(p, 2)) {

				p.getInventory().clear();
				ItemStack Wool = new ItemStack(Material.GREEN_WOOL);
				ItemMeta WoolMeta = Wool.getItemMeta();
				WoolMeta.setDisplayName(ChatColor.GREEN + "Woolhead");
				p.getInventory().setHelmet(Wool);
			}

		}
	}

}
