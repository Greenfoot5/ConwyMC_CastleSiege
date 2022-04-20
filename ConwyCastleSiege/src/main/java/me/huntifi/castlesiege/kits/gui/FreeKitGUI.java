package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * The class used to create the GUI for free kits
 */
public class FreeKitGUI extends AbstractKitGUI {

    private final List<String> kitNames;

    /**
     * Create the GUI and store all corresponding kits in a list
     */
    public FreeKitGUI() {
        super(ChatColor.DARK_GREEN + "Free Kits");
        gui = createInventory();
        kitNames = Arrays.asList("Archer", "Spearman", "Swordsman",
                "FireArcher", /*"Horserider",*/ "Ladderman", "Scout", "Shieldman", "Skirmisher");
    }

    /**
     * Select the kit corresponding to the clicked item for the player
     * @param p The player who clicked
     * @param itemName The name of the item that was clicked on
     */
    @Override
    protected void clickedItem(Player p, String itemName) {
        String[] splitName = itemName.split(" ");
        String kitName = splitName[splitName.length - 1];

        // Select kit
        if (kitNames.contains(kitName)) {
            p.performCommand(kitName);
            Bukkit.getScheduler().runTask(Main.plugin, p::closeInventory);
        }
    }

    /**
     * Create the inventory of this GUI
     * @return An inventory with the free kits
     */
    private Inventory createInventory() {
        // Create an empty inventory
        Inventory inv = emptyInventory();

        // Place content in the inventory
        inv.setItem(10, archer());
        inv.setItem(13, spearman());
        inv.setItem(16, swordsman());

        inv.setItem(28, fireArcher());
        inv.setItem(37, horserider());
        inv.setItem(31, ladderman());
        inv.setItem(40, scout());
        inv.setItem(34, shieldman());
        inv.setItem(43, skirmisher());

        return inv;
    }

    /**
     * Create an item used to represent the archer kit
     * @return An item that represent the archer kit
     */
    private ItemStack archer() {
        return ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Archer",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " The standard ranged kit.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Wooden sword, bow and 32 arrows.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Free to play!",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the spearman kit
     * @return An item that represent the spearman kit
     */
    private ItemStack spearman() {
        return ItemCreator.item(new ItemStack(Material.STICK),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Spearman",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A kit that can throw spears.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Chainmail armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Spears.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Free to play!",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the swordsman kit
     * @return An item that represent the swordsman kit
     */
    private ItemStack swordsman() {
        return ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Swordsman",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " The standard melee kit.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Iron armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron sword.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Free to play!",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the fire archer kit
     * @return An item that represent the fire archer kit
     */
    private ItemStack fireArcher() {
        return ItemCreator.item(new ItemStack(Material.BLAZE_POWDER),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " FireArcher",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A slow archer that can put enemies on fire.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Bow, cauldron (firepit), 48 arrows.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Can put down a firepit. By clicking the",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " firepit with arrows, you create fire-arrows.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the horserider kit
     * @return An item that represent the horserider kit
     */
    private ItemStack horserider() {
        return ItemCreator.item(new ItemStack(Material.BARRIER),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Horserider",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_RED + "[Note]:" + ChatColor.RED + " This kit is not available yet!",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A light cavalry unit.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor with iron boots.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron sword, wheat.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Speed 1.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Can mount a horse when clicking with the wheat.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the ladderman kit
     * @return An item that represent the ladderman kit
     */
    private ItemStack ladderman() {
        return ItemCreator.item(new ItemStack(Material.LADDER),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Ladderman",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A useful kit for climbing up walls.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather chestplate, iron leggings & boots.",
                        ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Stone sword, 25 ladders.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Jump Boost 2.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the scout kit
     * @return An item that represent the scout kit
     */
    private ItemStack scout() {
        return ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Scout",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A very fast melee kit.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Wooden sword.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Speed 2.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the shieldman kit
     * @return An item that represent the shieldman kit
     */
    private ItemStack shieldman() {
        return ItemCreator.item(new ItemStack(Material.SHIELD),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Shieldman",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A tank melee kit with a shield.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather chestplate, chainmail leggings and iron boots.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Stone Sword.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness and damage resistance.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the skirmisher kit
     * @return An item that represent the skirmisher kit
     */
    private ItemStack skirmisher() {
        return ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Skirmisher",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A more mobile melee kit.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Iron armor & leather leggings.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron sword.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Speed and Jump Boost.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.",
                        ChatColor.GOLD + "------------------------------"), null);
    }
}
