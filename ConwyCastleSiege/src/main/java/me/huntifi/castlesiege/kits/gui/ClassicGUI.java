package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The class used to create the GUI for classic kits
 */
public class ClassicGUI extends AbstractGUI implements CommandExecutor {

    private final List<String> kitNames;

    /**
     * Create the GUI and store all corresponding kits in a list
     */
    public ClassicGUI() {
        super(ChatColor.DARK_GREEN + "Classic Kits");

        // Create this GUI's pages
        gui.add(page0());
        gui.add(page1());

        // List all kits available on these pages
        kitNames = Arrays.asList("Archer", "Berserker", "Cavalry", "Crossbowman", "Engineer", "Executioner",
                "Halberdier", "Maceman", "Medic", "Ranger", "Spearman", "Swordsman", "Viking", "Warhound");
    }

    /**
     * Perform the action (select kit or switch page) corresponding to the clicked item
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
        // Go to next page
        } else if (splitName[0].contains("Next")) {
            onPage.merge(p.getUniqueId(), 1, Integer::sum);
            newPage(p);
        // Go to previous page
        } else if (splitName[0].contains("Previous")) {
            onPage.merge(p.getUniqueId(), -1, Integer::sum);
            newPage(p);
        }
    }

    /**
     * Opens the classic kits GUI for the command source
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
     * Create the first page of this GUI
     * @return An inventory with part of the classic kits
     */
    private Inventory page0() {
        // Create an empty page
        Inventory page = emptyPage(54);

        // Place content on the page
        page.setItem(10, swordsman());
        page.setItem(13, archer());
        page.setItem(16, spearman());
        page.setItem(28, berserker());
        page.setItem(31, ranger());
        page.setItem(34, halberdier());
        page.setItem(53, nextPage);

        return page;
    }

    /**
     * Create the second page of this GUI
     * @return An inventory with part of the classic kits
     */
    private Inventory page1() {
        // Create an empty page
        Inventory page = emptyPage(54);

        // Place content on the page
        page.setItem(10, executioner());
        page.setItem(12, medic());
        page.setItem(14, maceman());
        page.setItem(16, viking());
        page.setItem(28, crossbowman());
        page.setItem(30, cavalry());
        page.setItem(32, engineer());
        page.setItem(34, warhound());
        page.setItem(45, prevPage);

        return page;
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
     * Create an item used to represent the berserker kit
     * @return An item that represent the berserker kit
     */
    private ItemStack berserker() {
        return ItemCreator.item(new ItemStack(Material.POTION),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Berserker",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " An armorless warrior with high dps and speed.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " No armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron sword, berserker potion.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the ranger kit
     * @return An item that represent the ranger kit
     */
    private ItemStack ranger() {
        return ItemCreator.item(new ItemStack(Material.LIME_DYE),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Ranger",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A ranged unit with more speed and bows.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Wooden sword, 3 special bows and 48 arrows.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Speed 1.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the halberdier kit
     * @return An item that represent the halberdier kit
     */
    private ItemStack halberdier() {
        return ItemCreator.item(new ItemStack(Material.DIAMOND_CHESTPLATE),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Halberdier",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A tank kit that deals extra damage to mounted units.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Diamond chestplate, chainmail leggings and iron boots.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron axe.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness 4, mining fatigue and hunger.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Does 50% more damage to mounted units.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the executioner kit
     * @return An item that represent the executioner kit
     */
    private ItemStack executioner() {
        return ItemCreator.item(new ItemStack(Material.IRON_AXE),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Executioner",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A light unit with the capability to execute enemies.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron axe.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Insta-kills enemies that are below 35% hp.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the medic kit
     * @return An item that represent the medic kit
     */
    private ItemStack medic() {
        return ItemCreator.item(new ItemStack(Material.CAKE),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Medic",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A support kit that heals allies and places down cakes.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor, golden boots.",
                        ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Wooden sword, bandages, 16x cakes.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Heals allies on right click with bandages.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Cakes placed on the ground heal allies when they right click them.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the maceman kit
     * @return An item that represent the maceman kit
     */
    private ItemStack maceman() {
        return ItemCreator.item(new ItemStack(Material.IRON_SHOVEL),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Maceman",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A kit with the ability to stun enemies.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Chainmail chestplate, leather leggings & iron boots.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron shovel.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Stun ability with 10 seconds cooldown.",
                        ChatColor.DARK_GREEN + "[On Stun]:" + ChatColor.GREEN + " First hit deals +25% damage, gives slowness and blindness.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the viking kit
     * @return An item that represent the viking kit
     */
    private ItemStack viking() {
        return ItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Viking",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " An axeman with the ability to ignore armor.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Iron chestplate, chainmail leggings & boots.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron axe.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Ignore the target's armor.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the crossbowman kit
     * @return An item that represent the crossbowman kit
     */
    private ItemStack crossbowman() {
        return ItemCreator.item(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Crossbowman",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A ranged unit that functions like a sniper.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor, iron chestplate.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Crossbow and 32 arrows.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness 2",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Shoots in a straight line, 5 second cooldown.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the cavalry kit
     * @return An item that represent the cavalry kit
     */
    private ItemStack cavalry() {
        return ItemCreator.item(new ItemStack(Material.IRON_HORSE_ARMOR),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Cavalry",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A basic mounted unit with a sword.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Chainmail armor and iron boots.",
                        ChatColor.DARK_GREEN + "[Weapon-Item]:" + ChatColor.GREEN + " Iron sword, 1x wheat",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Right click the wheat to mount your horse",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the engineer kit
     * @return An item that represent the engineer kit
     */
    private ItemStack engineer() {
        return ItemCreator.item(new ItemStack(Material.COBWEB),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Engineer",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A support kit that specializes in using tools.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.",
                        ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Stone sword, 16x cobwebs, 16x stone, 16x wood, 8x traps.",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " The ability to place traps and cobwebs, repair walls and wooden",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " structures. You can also refill catapults and operate cannons.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }

    /**
     * Create an item used to represent the warhound kit
     * @return An item that represent the warhound kit
     */
    private ItemStack warhound() {
        return ItemCreator.item(new ItemStack(Material.GHAST_TEAR),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Warhound",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A special kit where you play as a wolf.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " No armor.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Fangs.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Resistance, Jump Boost, Speed 4",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Slow down enemies and make them bleed.",
                        ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Unlocked with coins.",
                        ChatColor.GOLD + "------------------------------"), null);
    }
}
