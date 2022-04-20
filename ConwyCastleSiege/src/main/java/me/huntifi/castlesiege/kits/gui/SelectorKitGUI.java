package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

/**
 * The class used to create the GUI for selecting kit GUIs
 */
public class SelectorKitGUI extends AbstractKitGUI {

    /**
     * Create the GUI
     */
    public SelectorKitGUI() {
        super(ChatColor.DARK_GREEN + "Kits");
        gui = createInventory();
    }

    /**
     * Opens the GUI corresponding to the clicked item for the player
     * @param p The player who clicked
     * @param itemName The name of the item that was clicked on
     */
    @Override
    protected void clickedItem(Player p, String itemName) {
        if (itemName.length() < 2) {
            return;
        }

        String name = itemName.substring(2).split(" ")[0];
        switch (name) {
            case "Free":
                p.performCommand("kit free");
                break;
            case "Unlocked":
                p.performCommand("kit unlocked");
                break;
            case "Shop":
            case "Rohan":
            case "Uruk-hai":
                p.sendMessage(ChatColor.DARK_RED + "This feature is currently unavailable!");
                break;
        }
    }

    /**
     * Sets the team specific kits to correspond to the given team
     * @param teamName The name of the team
     */
    public Inventory setTeam(String teamName) {
        gui.setItem(16, team(teamName));
        return this.gui;
    }

    /**
     * Create the inventory of this GUI
     * @return An inventory with the sub-GUIs
     */
    private Inventory createInventory() {
        // Create an empty inventory
        Inventory guiInventory = emptyInventory();

        // Place content in the inventory
        guiInventory.setItem(10, free());
        guiInventory.setItem(13, unlocked());
        guiInventory.setItem(40, shop());

        return guiInventory;
    }

    /**
     * Create an item used to represent the GUI for free kits
     * @return An item that represent the GUI for free kits
     */
    private ItemStack free() {
        return ItemCreator.item(new ItemStack(Material.IRON_BLOCK),
                ChatColor.YELLOW + "Free Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play as one of the free kits!",
                        ChatColor.DARK_PURPLE + "Certain kits will require voting first."), null);
    }

    /**
     * Create an item used to represent the GUI for unlocked kits
     * @return An item that represent the GUI for unlocked kits
     */
    private ItemStack unlocked() {
        return ItemCreator.item(new ItemStack(Material.GOLD_BLOCK),
                ChatColor.YELLOW + "Unlocked Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play as one of the unlocked kits!",
                        ChatColor.DARK_PURPLE + "These kits can be unlocked in the shop."), null);
    }

    /**
     * Create an item used to represent the GUI for team kits
     * @param teamName The name of the team
     * @return An item that represent the GUI for team kits
     */
    private ItemStack team(String teamName) {
        return ItemCreator.item(new ItemStack(Material.EMERALD_BLOCK),
                ChatColor.YELLOW + teamName + " Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play as one of the team-specific kits!",
                        ChatColor.DARK_PURPLE + "These kits depend on the team you are in."), null);
    }

    /**
     * Create an item used to represent the GUI for the shop
     * @return An item that represent the GUI for the shop
     */
    private ItemStack shop() {
        return ItemCreator.item(new ItemStack(Material.SUNFLOWER),
                ChatColor.YELLOW + "Shop",
                Collections.singletonList(ChatColor.DARK_PURPLE + "Unlock paid kits here!"), null);
    }
}
