package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class AbstractGUI implements Listener {

    private final String guiName;

    protected final ArrayList<Inventory> gui;
    protected static HashMap<UUID, Integer> onPage;
    protected static HashMap<UUID, Boolean> canExit;

    private final ItemStack panel;
    protected final ItemStack nextPage;
    protected final ItemStack prevPage;

    protected AbstractGUI(String guiName) {
        this.guiName = guiName;
        gui = new ArrayList<>();
        onPage = new HashMap<>();
        canExit = new HashMap<>();

        // Common items
        panel = ItemCreator.item(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, null);
        nextPage = ItemCreator.item(new ItemStack(Material.ARROW), ChatColor.GREEN + "Next Page", null, null);
        prevPage = ItemCreator.item(new ItemStack(Material.ARROW), ChatColor.GREEN + "Previous Page", null, null);
    }

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
        if (itemName.split(" ").length > 1) {
            clickedItem(p, itemName);
        }
    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (canExit.containsKey(uuid) && canExit.get(uuid)) {
            onPage.remove(e.getPlayer().getUniqueId());
            canExit.remove(uuid);
        }
    }

    protected abstract void clickedItem(Player p, String itemName);

    protected void newPage(Player p) {
        UUID uuid = p.getUniqueId();
        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            canExit.put(uuid, false);
            p.openInventory(gui.get(onPage.get(uuid)));
            canExit.put(uuid, true);
        });
    }

    protected Inventory emptyPage(int size) {
        // Create page
        Inventory page = Main.plugin.getServer().createInventory(
                null, size, guiName);
        // Fill page with panels
        for (int i = 0; i < size; i++) {
            page.setItem(i, panel);
        }

        return page;
    }
}
