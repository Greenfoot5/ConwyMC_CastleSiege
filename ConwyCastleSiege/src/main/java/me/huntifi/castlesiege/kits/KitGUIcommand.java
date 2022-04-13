package me.huntifi.castlesiege.kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
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

import me.huntifi.castlesiege.database.SQLStats;
import me.huntifi.castlesiege.database.StatsStrings;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class KitGUIcommand implements CommandExecutor, Listener {

	public static ArrayList<Player> isInGUI = new ArrayList<Player>();

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("KitGUI")) {

			if (LobbyPlayer.containsPlayer(p)) {

				KitGUI gui = new KitGUI();
				Inventory Menu = gui.Kitgui(p);
				p.openInventory(Menu);
				isInGUI.add(p);

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

				case IRON_SWORD:

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

						SQLStats.setKit(p.getUniqueId(), "Swordsman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); p.closeInventory(); });
						isInGUI.remove(p);
					});

					break;

				case BOW:

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

						SQLStats.setKit(p.getUniqueId(), "Archer");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); p.closeInventory();});
						isInGUI.remove(p);
					});

					break;

				case STICK:

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

						SQLStats.setKit(p.getUniqueId(), "Spearman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); p.closeInventory(); });
						isInGUI.remove(p);
					});

					break;

				case IRON_BOOTS:

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

						SQLStats.setKit(p.getUniqueId(), "Skirmisher");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); p.closeInventory(); });
						isInGUI.remove(p);
					});

					break;


				case SHIELD:

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

						SQLStats.setKit(p.getUniqueId(), "Shieldman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); p.closeInventory(); });
						isInGUI.remove(p);
					});

					break;

				case BLAZE_POWDER:

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

						SQLStats.setKit(p.getUniqueId(), "FireArcher");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); p.closeInventory(); });
						isInGUI.remove(p);
					});

					break;

				}


			}
		}

	}

}
