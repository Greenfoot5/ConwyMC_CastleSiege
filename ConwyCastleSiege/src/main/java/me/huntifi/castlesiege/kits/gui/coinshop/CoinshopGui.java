package me.huntifi.castlesiege.kits.gui.coinshop;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CoinshopGui implements Listener, CommandExecutor {

    public static Inventory gui;
    public static HashMap<Integer, String> shopCommand = new HashMap<>();

    /**
     * Create an inventory
     * @param name The name of the inventory
     */
    public static Inventory CoinshopGui(String name) {
        gui = Main.plugin.getServer().createInventory(null, 54, name);
        return gui;
    }

    /**
     * Add an item to the inventory
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param location The location of the item
     */
    public static void addItem(String name, Material material, List<String> lore, int location, String command) {
        gui.setItem(location, ItemCreator.item(new ItemStack(material), name, lore, null));
        shopCommand.put(location, command);
    }

    /**
     * Open the GUI corresponding to the clicked item
     * @param e A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (gui == null || !Objects.equals(e.getClickedInventory(), gui)) {
            return;
        }

        // Perform the command that belongs to the clicked item slot
        String c = shopCommand.get(e.getSlot());
        if (c != null) {
            p.closeInventory();
            p.performCommand("buykit " + p.getName() + " " + c);
        }
    }

    //Add all the shop items
    public static void registerShop() {

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Alchemist", Material.CAULDRON, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Alchemist")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 0, "donator Alchemist");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Berserker", Material.POTION, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Berserker")).getPrice(),
            ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 1, "donator Berserker");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Vanguard", Material.DIAMOND_SWORD, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Vanguard")).getPrice(),
            ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 2, "donator Vanguard");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Executioner", Material.IRON_AXE, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Executioner")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 3, "donator Executioner");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Maceman", Material.DIAMOND_SHOVEL, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Maceman")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 4, "donator Maceman");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Viking", Material.IRON_CHESTPLATE, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Viking")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 5, "donator Viking");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Medic", Material.CAKE, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Medic")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 6, "donator Medic");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Ranger", Material.BOW, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Ranger")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 7, "donator Ranger");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Cavalry", Material.IRON_HORSE_ARMOR, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Cavalry")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 8, "donator Cavalry");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Halberdier", Material.DIAMOND_CHESTPLATE, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Halberdier")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 9, "donator Halberdier");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Engineer", Material.COBWEB, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Engineer")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 10, "donator Engineer");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Crossbowman", Material.CROSSBOW, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Crossbowman")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 11, "donator Crossbowman");

        addItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Warhound", Material.GHAST_TEAR, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((DonatorKit) Kit.getKit("Warhound")).getPrice(),
                ChatColor.GREEN + "Duration: 30 days", ChatColor.YELLOW + "Click here to buy!"), 12, "donator Warhound");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Elytrier", Material.ELYTRA, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("Elytrier")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Thunderstone",
                ChatColor.GREEN + "Team: " + ChatColor.GOLD + "Thunderstone Guard", ChatColor.YELLOW + "Click here to buy!"), 13, "team Elytrier");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Fallen", Material.BONE, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("Fallen")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Royal Crypts",
                ChatColor.GREEN + "Team: " + ChatColor.WHITE + "Tomb Guardians", ChatColor.YELLOW + "Click here to buy!"), 14, "team Fallen");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Moria Orc", Material.POISONOUS_POTATO, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("MoriaOrc")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Moria",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_GREEN + "The Orcs", ChatColor.YELLOW + "Click here to buy!"), 15, "team MoriaOrc");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Uruk Berserker", Material.BEETROOT_SOUP, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("UrukBerserker")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Helm's Deep",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_GRAY + "The Uruk-hai", ChatColor.YELLOW + "Click here to buy!"), 16, "team UrukBerserker");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Lancer", Material.STICK, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("Lancer")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Helm's Deep",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_GREEN + "Rohan", ChatColor.YELLOW + "Click here to buy!"), 17, "team Lancer");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Ranged Cavalry", Material.BOW, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("RangedCavalry")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Helm's Deep",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_GREEN + "Rohan", ChatColor.YELLOW + "Click here to buy!"), 18, "team RangedCavalry");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Abyssal", Material.GREEN_DYE, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("Abyssal")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Firelands",
                ChatColor.GREEN + "Team: " + ChatColor.GREEN + "The Burning Legion", ChatColor.YELLOW + "Click here to buy!"), 19, "team Abyssal");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Hellsteed", Material.MAGMA_BLOCK, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("Hellsteed")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Firelands",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_PURPLE + "Hellfire Guards", ChatColor.YELLOW + "Click here to buy!"), 20, "team Hellsteed");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Royal Knight", Material.DIAMOND_HORSE_ARMOR, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("RoyalKnight")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Conwy",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_RED + "The English", ChatColor.YELLOW + "Click here to buy!"), 21, "team RoyalKnight");

        addItem(ChatColor.BLUE + "" + ChatColor.BOLD + "Arbalester", Material.CROSSBOW, Arrays.asList(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + ((TeamKit) Kit.getKit("Arbalester")).getPrice(),
                ChatColor.GREEN + "Duration: permanent", ChatColor.GREEN + "Map: " + ChatColor.BOLD + "Conwy",
                ChatColor.GREEN + "Team: " + ChatColor.DARK_RED + "The English", ChatColor.YELLOW + "Click here to buy!"), 22, "team Arbalester");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
    if (commandSender instanceof ConsoleCommandSender) {
    commandSender.sendMessage("Consoles can't open inventories.");
    return true;
    }

    if (commandSender instanceof Player) {
        Player p = (Player) commandSender;

        // Prevent using in lobby
        if (!InCombat.isPlayerInLobby(p.getUniqueId())) {
            Messenger.sendError("Are you sure it is safe to do this command here? Try it in the spawnrooms!", commandSender);
            return true;
        }

        p.openInventory(CoinshopGui("Coin Shop"));
        CoinshopGui.registerShop();
    }

        return true;
    }
}
