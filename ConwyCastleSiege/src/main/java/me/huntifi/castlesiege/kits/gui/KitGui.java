package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * A GUI used to select and buy kits
 */
public class KitGui implements Listener {

    private final Inventory gui;

    /**
     * Create an inventory
     * @param name The name of the inventory
     */
    public KitGui(String name) {
        gui = Main.plugin.getServer().createInventory(null, 54, name);
    }

    /**
     * Add an item to the inventory
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param location The location of the item
     */
    public void addItem(String name, Material material, List<String> lore, int location) {
        gui.setItem(location, ItemCreator.item(new ItemStack(material), name, lore, null));
    }

    /**
     * Open this GUI for the player
     * @param p The player
     */
    public void open(Player p) {
        p.openInventory(gui);
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

        // Make sure an actual item was clicked
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null || !clicked.getItemMeta().hasDisplayName()) {
            return;
        }

        // Call the GUI specific click handler
        String itemName = clicked.getItemMeta().getDisplayName();
        KitGui clickedGui = KitGuiController.get(itemName);
        if (clickedGui != null) {
            clickedGui.open(p);
        }
    }
}
