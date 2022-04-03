package me.huntifi.castlesiege.Helmsdeep.Kits.GUI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class KitGUI_Isengard_Command implements CommandExecutor, Listener {

	public static ArrayList<Player> isInGUI = new ArrayList<Player>();

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("KitIsengard")) {
			
			if (MapController.currentMapIs("Helmsdeep")) {

			if (LobbyPlayer.containsPlayer(p)) {

				KitGui_Isengard gui = new KitGui_Isengard();
				Inventory Menu = gui.KitGui(p);
				p.openInventory(Menu);
				isInGUI.add(p);

			} else {

				p.sendMessage(ChatColor.DARK_RED + "Not allowed to use this command outside of the spawns.");

			}
			
			}

		}
		return true;

	}

	@EventHandler
	public void InvenClick(InventoryClickEvent e) {

		if (e.getSlotType() == InventoryType.SlotType.CONTAINER) {

			e.setCancelled(true);

		} else {

			if (isInGUI.contains(e.getWhoClicked())) {

				e.setCancelled(true);
				isInGUI.remove(e.getWhoClicked());
			}

		}

	}


	@EventHandler
	public void onPlayerClickMenu(InventoryClickEvent e) {

		Player p = (Player) e.getWhoClicked();

		if (LobbyPlayer.containsPlayer(p) && isInGUI.contains(p)) {

			if (e.getCurrentItem() != null) {

				switch(e.getCurrentItem().getType()) {

				default:
					break;

				case AIR:
					break;

				case IRON_BLOCK:

					p.performCommand("classicgui");
					isInGUI.remove(p);
					
					break;

				case LAPIS_BLOCK:

					p.performCommand("VoterKitGUI");
					isInGUI.remove(p);
					break;

				case EMERALD_BLOCK:

					isInGUI.remove(p);
					p.getOpenInventory().close();
					break;

				case IRON_INGOT:

					isInGUI.remove(p);
					p.getOpenInventory().close();
					break;


				case COAL:

					isInGUI.remove(p);
					p.getOpenInventory().close();
					break;

				}


			}
		}

	}

}
