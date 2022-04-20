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

/**
 * A class used for creating kit related GUIs
 */
public abstract class AbstractKitGUI implements Listener {

    private final String guiName;
    protected Inventory gui;
    private final ItemStack panel;

    /**
     * Initialise all commonly used values
     * @param guiName The name for the GUI
     */
    protected AbstractKitGUI(String guiName) {
        this.guiName = guiName;
        panel = ItemCreator.item(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, null);
    }

    /**
     * Calls the GUI's click event handler after performing checks
     * @param e A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!e.getView().getTitle().equalsIgnoreCase(guiName)) {
            return;
        }
        e.setCancelled(true); // TODO - Remove when general prevention is implemented

        // Make sure an actual item was clicked
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null ||
                clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            return;
        }

        // Call the GUI specific click handler
        String itemName = clicked.getItemMeta().getDisplayName();
        clickedItem(p, itemName);
    }

    /**
     * Handles the click event after onClick performed its checks
     * @param p The player who clicked
     * @param itemName The name of the item that was clicked on
     */
    protected abstract void clickedItem(Player p, String itemName);

    /**
     * Creates an inventory and fills it with panels
     * @return An inventory with all slots set to the panel item
     */
    protected Inventory emptyInventory() {
        // Create page
        Inventory page = Main.plugin.getServer().createInventory(
                null, 54, guiName);
        // Fill page with panels
        for (int i = 0; i < 54; i++) {
            page.setItem(i, panel);
        }

        return page;
    }

    /**
     * Gets this GUI
     */
    public Inventory getGui() {
        return this.gui;
    }
}
