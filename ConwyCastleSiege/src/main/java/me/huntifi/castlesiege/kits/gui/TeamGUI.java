package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * The class used to create the GUI for selecting kit GUIs
 */
public class TeamGUI extends AbstractGUI implements CommandExecutor {

    /**
     * Create the GUI
     */
    public TeamGUI(String team) {
        super(ChatColor.DARK_GREEN + team);

        // Create this GUI's page
        gui.add(page(team));
    }

    /**
     * Opens the GUI corresponding to the clicked item for the player
     * @param p The player who clicked
     * @param itemName The name of the item that was clicked on
     */
    @Override
    protected void clickedItem(Player p, String itemName) {
        String name = itemName.substring(2).split(" ")[0];
        switch (name) {
            case "Classic":
                p.performCommand("classicGUI");
                break;
            case "Voter":
                p.performCommand("voterGUI");
                break;
            case "Secret":
            case "Rohan":
            case "Elven":
            case "Uruk-hai":
            case "Orc":
                p.sendMessage(ChatColor.DARK_RED + "This feature is currently unavailable!");
                break;
        }
    }

    /**
     * Opens the kits GUI selector for the command source
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        p.openInventory(gui.get(0));
        onPage.put(p.getUniqueId(), 0);
        canExit.put(p.getUniqueId(), true);
        return true;
    }

    /**
     * Create the page of this GUI
     * @return An inventory with the sub-GUIs
     */
    private Inventory page(String team) {
        // Create an empty page
        Inventory page = emptyPage(54);

        // Place common content on the page
        page.setItem(10, classic());
        page.setItem(13, voter());
        page.setItem(16, secret());
        // Place team dependant content
        switch (team) {
            case "Rohan":
                page.setItem(38, rohan());
                page.setItem(42, elves());
                break;
            case "Uruk-hai":
                page.setItem(38, urukhai());
                page.setItem(42, orcs());
                break;
        }

        return page;
    }

    /**
     * Create an item used to represent the GUI for classic kits
     * @return An item that represent the GUI for classic kits
     */
    private ItemStack classic() {
        return ItemCreator.item(new ItemStack(Material.IRON_BLOCK),
                ChatColor.YELLOW + "Classic Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play as one of the original Dark Age kits!",
                        ChatColor.DARK_PURPLE + "However, certain kits will have",
                        ChatColor.DARK_PURPLE + "to be unlocked with coins first."), null);
    }

    /**
     * Create an item used to represent the GUI for voter kits
     * @return An item that represent the GUI for voter kits
     */
    private ItemStack voter() {
        return ItemCreator.item(new ItemStack(Material.LAPIS_BLOCK),
                ChatColor.YELLOW + "Voter Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play as one of the voter kits!",
                        ChatColor.DARK_PURPLE + "This requires you to vote first."), null);
    }

    /**
     * Create an item used to represent the GUI for secret kits
     * @return An item that represent the GUI for secret kits
     */
    private ItemStack secret() {
        return ItemCreator.item(new ItemStack(Material.EMERALD_BLOCK),
                ChatColor.YELLOW + "Secret Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play as one of the secret kits!",
                        ChatColor.DARK_PURPLE + "This requires you to find their",
                        ChatColor.DARK_PURPLE + "respective secrets first."), null);
    }

    /**
     * Create an item used to represent the GUI for rohan kits
     * @return An item that represent the GUI for rohan kits
     */
    private ItemStack rohan() {
        return ItemCreator.item(new ItemStack(Material.LEATHER_HORSE_ARMOR),
                ChatColor.DARK_GREEN + "Rohan Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play kits unique to Rohan!",
                        ChatColor.DARK_PURPLE + "However, certain kits will have",
                        ChatColor.DARK_PURPLE + "to be unlocked with coins first."), null);
    }

    /**
     * Create an item used to represent the GUI for elven kits
     * @return An item that represent the GUI for elven kits
     */
    private ItemStack elves() {
        return ItemCreator.item(new ItemStack(Material.OAK_LEAVES),
                ChatColor.DARK_GREEN + "Elven Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play kits unique to the Elves!",
                        ChatColor.DARK_PURPLE + "However, certain kits will have",
                        ChatColor.DARK_PURPLE + "to be unlocked with coins first."), null);
    }

    /**
     * Create an item used to represent the GUI for uruk-hai kits
     * @return An item that represent the GUI for uruk-hai kits
     */
    private ItemStack urukhai() {
        return ItemCreator.item(new ItemStack(Material.IRON_INGOT),
                ChatColor.DARK_GRAY + "Uruk-hai Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play kits unique to the Uruk-hai!",
                        ChatColor.DARK_PURPLE + "However, certain kits will have",
                        ChatColor.DARK_PURPLE + "to be unlocked with coins first."), null);
    }

    /**
     * Create an item used to represent the GUI for orc kits
     * @return An item that represent the GUI for orc kits
     */
    private ItemStack orcs() {
        return ItemCreator.item(new ItemStack(Material.COAL),
                ChatColor.DARK_GRAY + "Orc Kits",
                Arrays.asList(ChatColor.DARK_PURPLE + "Play kits unique to the Orcs!",
                        ChatColor.DARK_PURPLE + "However, certain kits will have",
                        ChatColor.DARK_PURPLE + "to be unlocked with coins first."), null);
    }
}
