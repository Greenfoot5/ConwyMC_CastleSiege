package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * A GUI made with a minecraft inventory
 */
public class Gui implements Listener {

    protected final Inventory inventory;
    protected final HashMap<Integer, GuiItem> locationToItem = new HashMap<>();

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     */
    public Gui(String name, int rows) {
        inventory = Main.plugin.getServer().createInventory(null, 9 * rows, name);
    }

    /**
     * Add an item to the inventory.
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
     * Add a coin shop item to the inventory.
     * @param kitName The name of the kit without spaces
     * @param material The material of the item
     * @param location The location of the item
     */
    public void addCoinShopItem(String kitName, Material material, int location) {
        Kit kit = Kit.getKit(kitName);
        if (!(kit instanceof DonatorKit))
            throw new IllegalArgumentException(kitName + " is not a donator kit");

        String itemName = (kit instanceof TeamKit ? ChatColor.BLUE : ChatColor.GOLD) + "" + ChatColor.BOLD + kit.name;
        String price = ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) kit).getPrice();
        String duration = ChatColor.GREEN + "Duration: permanent";

        ArrayList<String> lore = new ArrayList<>();
        lore.add(price);
        lore.add(duration);
        if (kit instanceof TeamKit) {
            Map map = MapController.getMap(((TeamKit) kit).getMapName());
            if (map != null) {
                Team team = map.getTeam(((TeamKit) kit).getTeamName());
                lore.add(ChatColor.GREEN + "Map: " + ChatColor.BOLD + map.name);
                lore.add(ChatColor.GREEN + "Team: " + team.primaryChatColor + team.name);
            } else
                lore.add(ChatColor.GREEN + "Map: " + ChatColor.BOLD + "OUT OF ROTATION");
        }
        lore.add(ChatColor.YELLOW + "Click here to buy!");

        addItem(itemName, material, lore, location, "buykit " + kitName, false);
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
