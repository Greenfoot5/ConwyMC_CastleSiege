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
 * The class used to create the GUI for unlocked kits
 */
public class UnlockedKitGUI extends AbstractKitGUI {

    private final List<String> kitNames;

    /**
     * Create the GUI and store all corresponding kits in a list
     */
    public UnlockedKitGUI() {
        super(ChatColor.DARK_GREEN + "Unlocked Kits");
        gui = createInventory();
        kitNames = Arrays.asList("Berserker", "Cavalry", "Crossbowman", "Engineer", "Executioner",
                "Halberdier", "Maceman", "Medic", "Ranger", "Vanguard", "Viking", "Warhound");
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
     * @return An inventory with the unlocked kits
     */
    private Inventory createInventory() {
        // Create an empty inventory
        Inventory inv = emptyInventory();

        // Place content in the inventory
        inv.setItem(10, berserker());
        inv.setItem(12, vanguard());
        inv.setItem(14, executioner());
        inv.setItem(16, maceman());

        inv.setItem(28, viking());
        inv.setItem(37, halberdier());

        inv.setItem(30, medic());
        inv.setItem(39, engineer());

        inv.setItem(32, ranger());
        inv.setItem(41, crossbowman());

        inv.setItem(34, cavalry());
        inv.setItem(43, warhound());

        return inv;
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
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Insta-kills enemies that are below 40% hp.",
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
     * Create an item used to represent the vanguard kit
     * @return An item that represent the vanguard kit
     */
    private ItemStack vanguard() {
        return ItemCreator.item(new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET + ChatColor.GREEN + " Vanguard",
                Arrays.asList(ChatColor.GOLD + "------------------------------",
                        ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A warrior with the ability to charge.",
                        ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor, iron leggings.",
                        ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Reinforced Iron Sword.",
                        ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Jump Boost",
                        ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Charge at high speed and more damage on impact.",
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

