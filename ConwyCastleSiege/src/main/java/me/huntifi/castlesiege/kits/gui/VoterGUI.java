package me.huntifi.castlesiege.kits.gui;

import me.huntifi.castlesiege.kits.ItemCreator;
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

public class VoterGUI extends AbstractGUI implements CommandExecutor {

    public VoterGUI() {
        super(ChatColor.DARK_GREEN + "Voter Kits");

        // Create this GUI's pages
        gui.add(page());

        // List all kits available on these pages
        kitNames = Arrays.asList("FireArcher", /*"Horserider",*/ "Ladderman", "Scout", "Shieldman", "Skirmisher");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        p.openInventory(gui.get(0));
        onPage.put(p.getUniqueId(), 0);
        canExit.put(p.getUniqueId(), true);
        return true;
    }

    private Inventory page() {
        // Create an empty page
        Inventory page = emptyPage(54);

        // Place content on the page
        page.setItem(10, shieldman());
        page.setItem(13, skirmisher());
        page.setItem(16, fireArcher());
        page.setItem(28, horserider());
        page.setItem(31, scout());
        page.setItem(34, ladderman());

        return page;
    }

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
}
