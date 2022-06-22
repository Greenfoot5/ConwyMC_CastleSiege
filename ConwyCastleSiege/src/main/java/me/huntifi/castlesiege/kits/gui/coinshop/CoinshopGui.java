package me.huntifi.castlesiege.kits.gui.coinshop;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CoinshopGui implements Listener, CommandExecutor {

    public static Inventory gui;
    public static HashMap<Integer, String> shopCommand = new HashMap<>();

    /**
     * Create an inventory
     * @param name The name of the inventory
     */
    public static Inventory CoinshopGui(String name) {
        gui = Main.plugin.getServer().createInventory(null, 54, name);
        return gui;
    }

    /**
     * Add an item to the inventory
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param location The location of the item
     */
    public static void addItem(String name, Material material, List<String> lore, int location, String command) {
        gui.setItem(location, ItemCreator.item(new ItemStack(material), name, lore, null));
        shopCommand.put(location, command);
    }

    /**
     * Open the GUI corresponding to the clicked item
     * @param e A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!Objects.equals(e.getClickedInventory(), gui)) {
            return;
        }

        // Perform the command that belongs to the clicked item slot
        String c = shopCommand.get(e.getSlot());
        if (c != null) {
            p.closeInventory();
            p.performCommand(c);
        }
    }

    //Add all the shop items
    public static void registerShop() {

        addItem(ChatColor.GOLD + "Berserker", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
            ChatColor.GREEN + "Duration: 30 days"), 0, "");

        addItem(ChatColor.GOLD + "Vanguard", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
            ChatColor.GREEN + "Duration: 30 days"), 1, "");

        addItem(ChatColor.GOLD + "Executioner", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 2, "");

        addItem(ChatColor.GOLD + "Maceman", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 3, "");

        addItem(ChatColor.GOLD + "Viking", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 4, "");

        addItem(ChatColor.GOLD + "Medic", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 5, "");

        addItem(ChatColor.GOLD + "Ranger", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 6, "");

        addItem(ChatColor.GOLD + "Cavalry", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 7, "");

        addItem(ChatColor.GOLD + "Halberdier", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 8, "");

        addItem(ChatColor.GOLD + "Engineer", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 9, "");

        addItem(ChatColor.GOLD + "Crossbowman", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 10, "");

        addItem(ChatColor.GOLD + "Warhound", Material.CHEST, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW,
                ChatColor.GREEN + "Duration: 30 days"), 11, "");


    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
    if (commandSender instanceof ConsoleCommandSender) {
    commandSender.sendMessage("Consoles can't open inventories.");
    return true;
    }

    if (commandSender instanceof Player) {
        Player p = (Player) commandSender;

        // Prevent using in lobby
        if (!InCombat.isPlayerInLobby(p.getUniqueId())) {
            Messenger.sendError("Are you sure it is safe to do this command here? Try it in the spawnrooms!", commandSender);
            return true;
        }

        p.openInventory(CoinshopGui("Coin Shop"));
        CoinshopGui.registerShop();
    }

        return true;
    }
}
