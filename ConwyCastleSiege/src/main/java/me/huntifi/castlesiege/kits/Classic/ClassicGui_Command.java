package me.huntifi.castlesiege.kits.Classic;

import java.util.ArrayList;

import me.huntifi.castlesiege.kits.Enderchest;
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

import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class ClassicGui_Command implements CommandExecutor, Listener {

	public static ArrayList<Player> isInGUI = new ArrayList<Player>();
	public static ArrayList<Player> isInGUI2 = new ArrayList<Player>();

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("ClassicGUI")) {

			if (LobbyPlayer.containsPlayer(p)) {

				KitGUI_Classic gui = new KitGUI_Classic();
				Inventory Menu = gui.ClassicKitGuiPage1();
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

				/*case AIR:
					break;

				case IRON_SWORD:

					StatsChanging.setKit(p.getUniqueId(), "Swordsman");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;

				case BOW:

					StatsChanging.setKit(p.getUniqueId(), "Archer");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory();});
					isInGUI.remove(p);

					break;

				case STICK:

					StatsChanging.setKit(p.getUniqueId(), "Spearman");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);


					break;

				case POTION:

					StatsChanging.setKit(p.getUniqueId(), "Berserker");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;


				case LIME_DYE:

					StatsChanging.setKit(p.getUniqueId(), "Ranger");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;

				case DIAMOND_CHESTPLATE:

					StatsChanging.setKit(p.getUniqueId(), "Halberdier");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;

				case ARROW:

					if (e.getSlot() == 53) {

						KitGUI_Classic gui = new KitGUI_Classic();
						Inventory Menu = gui.ClassicKitGuiPage2();
						p.openInventory(Menu);

					}

					break;*/

				}


			}
		}

	}

	@EventHandler
	public void onPlayerClickMenu2(InventoryClickEvent e) {

		Player p = (Player) e.getWhoClicked();

		if (LobbyPlayer.containsPlayer(p) && isInGUI.contains(p)) {

			if (e.getCurrentItem() != null) {

				switch(e.getCurrentItem().getType()) {

				default:
					break;

				case AIR:
					break;

				/*case IRON_AXE:

					StatsChanging.setKit(p.getUniqueId(), "Executioner");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;

				case CAKE:

					StatsChanging.setKit(p.getUniqueId(), "Medic");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory();});
					isInGUI.remove(p);

					break;

				case IRON_SHOVEL:

					StatsChanging.setKit(p.getUniqueId(), "Maceman");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);


					break;

				case CHAINMAIL_CHESTPLATE:

					StatsChanging.setKit(p.getUniqueId(), "Viking");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;


				case CROSSBOW:

					StatsChanging.setKit(p.getUniqueId(), "Crossbowman");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;

				case IRON_HORSE_ARMOR:

					StatsChanging.setKit(p.getUniqueId(), "Cavalry");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

					break;

				case COBWEB:

						StatsChanging.setKit(p.getUniqueId(), "Engineer");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
						isInGUI.remove(p);

					break;
					
				case GHAST_TEAR:

					StatsChanging.setKit(p.getUniqueId(), "Warhound");
					Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
					isInGUI.remove(p);

				break;


				case ARROW:

					if (e.getSlot() == 45) {

						KitGUI_Classic gui = new KitGUI_Classic();
						Inventory Menu = gui.ClassicKitGuiPage1();
						p.openInventory(Menu);

					}

					break;*/

				}




			}
		}

	}

}
