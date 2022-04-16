package me.huntifi.castlesiege.kits.VotedKits;

import java.util.ArrayList;

import me.huntifi.castlesiege.kits.Enderchest;
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

import me.huntifi.castlesiege.events.join.stats.StatsChanging;
import me.huntifi.castlesiege.kits.ClearSlots;
import me.huntifi.castlesiege.kits.Enderchest;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class VotedKitsGUI_Command implements CommandExecutor, Listener {

	public static ArrayList<Player> isInGUI = new ArrayList<Player>();

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("VoterKitGUI")) {

			if (LobbyPlayer.containsPlayer(p)) {

				KitGUI_VotedKits gui = new KitGUI_VotedKits();
				Inventory Menu = gui.VotedKitGuiPage1();
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

				if (VotesChanging.getVotes(p.getUniqueId()).contains("V#4")) {

					switch(e.getCurrentItem().getType()) {

					default:
						break;

					case AIR:
						break;

					/*case SHIELD:

						StatsChanging.setKit(p.getUniqueId(), "Shieldman");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
						isInGUI.remove(p);

						break;

					case IRON_BOOTS:

						StatsChanging.setKit(p.getUniqueId(), "Skirmisher");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory();});
						isInGUI.remove(p);

						break;

					case BLAZE_POWDER:

						StatsChanging.setKit(p.getUniqueId(), "FireArcher");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
						isInGUI.remove(p);


						break;

					case LEATHER_HORSE_ARMOR:

						p.sendMessage(ChatColor.DARK_RED + "We do apologise, this kit has not been added yet!");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
						isInGUI.remove(p);

						break;


					case LEATHER_BOOTS:

						StatsChanging.setKit(p.getUniqueId(), "Scout");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
						isInGUI.remove(p);

						break;

					case LADDER:

						StatsChanging.setKit(p.getUniqueId(), "Ladderman");
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); Enderchest.refill(p); p.closeInventory(); });
						isInGUI.remove(p);

						break;*/

					}


				} else {
					
					p.sendMessage(ChatColor.GREEN + "Dear " + p.getName() + " if you wish to play these kits, then you have to vote for the server!");
					p.getOpenInventory().close();
					isInGUI.remove(p);
				}
			}
		}

	}
}
