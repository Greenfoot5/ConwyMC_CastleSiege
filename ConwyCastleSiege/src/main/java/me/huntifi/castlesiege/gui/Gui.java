package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.GuiItem;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Gui implements Listener {

    protected final Inventory inventory;
    protected final HashMap<Integer, GuiItem> locationToItem = new HashMap<>();

    public Gui(String name, int size) {
        inventory = Main.plugin.getServer().createInventory(null, size, name);
    }

    /**
     * Add an item to the inventory
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param location The location of the item
     * @param command The command to execute when clicking the item
     * @param shouldClose Whether the GUI should close after performing the command
     */
    public void addItem(String name, Material material, List<String> lore, int location, String command, boolean shouldClose) {
        inventory.setItem(location, ItemCreator.item(new ItemStack(material), name, lore, null));
        locationToItem.put(location, new GuiItem(command, shouldClose));
    }

    /**
     * Open this GUI for the player
     * @param player The player
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Performs an action corresponding to the clicked item
     * @param event A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Objects.equals(event.getClickedInventory(), inventory)) {
            GuiItem item = locationToItem.get(event.getSlot());
            if (item != null) {
                Player player = (Player) event.getWhoClicked();
                if (item.shouldClose)
                    player.closeInventory();
                player.performCommand(item.command);
            }
        }
    }
}
