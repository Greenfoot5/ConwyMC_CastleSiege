package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.coin_kits.Alchemist;
import me.huntifi.castlesiege.kits.kits.coin_kits.Barbarian;
import me.huntifi.castlesiege.kits.kits.coin_kits.Berserker;
import me.huntifi.castlesiege.kits.kits.coin_kits.Cavalry;
import me.huntifi.castlesiege.kits.kits.coin_kits.Crossbowman;
import me.huntifi.castlesiege.kits.kits.coin_kits.Engineer;
import me.huntifi.castlesiege.kits.kits.coin_kits.Executioner;
import me.huntifi.castlesiege.kits.kits.coin_kits.Halberdier;
import me.huntifi.castlesiege.kits.kits.coin_kits.Maceman;
import me.huntifi.castlesiege.kits.kits.coin_kits.Medic;
import me.huntifi.castlesiege.kits.kits.coin_kits.Paladin;
import me.huntifi.castlesiege.kits.kits.coin_kits.Priest;
import me.huntifi.castlesiege.kits.kits.coin_kits.Ranger;
import me.huntifi.castlesiege.kits.kits.coin_kits.Rogue;
import me.huntifi.castlesiege.kits.kits.coin_kits.Vanguard;
import me.huntifi.castlesiege.kits.kits.coin_kits.Viking;
import me.huntifi.castlesiege.kits.kits.coin_kits.Warhound;
import me.huntifi.castlesiege.kits.kits.free_kits.Archer;
import me.huntifi.castlesiege.kits.kits.free_kits.Spearman;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.kits.kits.coin_kits.Sorcerer;
import me.huntifi.castlesiege.kits.kits.coin_kits.Warlock;
import me.huntifi.castlesiege.kits.kits.in_development.Armorer;
import me.huntifi.castlesiege.kits.kits.level_kits.BattleMedic;
import me.huntifi.castlesiege.kits.kits.level_kits.Hypaspist;
import me.huntifi.castlesiege.kits.kits.level_kits.SpearKnight;
import me.huntifi.castlesiege.kits.kits.staff_kits.Warbear;
import me.huntifi.castlesiege.kits.kits.voter_kits.FireArcher;
import me.huntifi.castlesiege.kits.kits.voter_kits.Ladderman;
import me.huntifi.castlesiege.kits.kits.voter_kits.Scout;
import me.huntifi.castlesiege.kits.kits.free_kits.Shieldman;
import me.huntifi.castlesiege.kits.kits.voter_kits.Skirmisher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates dynamic kit selection GUIs
 */
public class KitGUIs {

    public final static String[] OPTIONS = {"damage", "tank", "support", "debuff", "movement", "ranged"};

    /**
     * Displays the kit gui for a category or category selection
     * @param category The category to select. Displays all categories if null
     * @param p The player opening the menu
     * @return The menu to show the player
     */
    public static Gui getGUI(String category, Player p) {
        if (category == null) {
            return selector();
        }

        switch (category.toLowerCase()) {
            case "damage":
                return damage(p);
            case "tank":
                return tank(p);
            case "support":
                return support(p);
            case "debuff":
                return debuff(p);
            case "movement":
                return movement(p);
            case "ranged":
                return ranged(p);
            default:
                Messenger.sendError("Unknown category: " + ChatColor.RED + category, p);
                return null;
        }
    }

    /**
     * Creates a kit category GUI
     * @return The generated GUI
     */
    // TODO - Maybe list how many of the total have been collected?
    private static Gui selector() {
        Gui gui = new Gui("Categories", 6);
        gui.addItem(ChatColor.GREEN + "Damager", Material.IRON_SWORD,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a damager kit here!"), 11, "kit damage", true);

        gui.addItem(ChatColor.GREEN + "Tank", Material.DIAMOND_CHESTPLATE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a tank kit here!"), 13, "kit tank", true);

        gui.addItem(ChatColor.GREEN + "Ranged", Material.BOW,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a ranged kit here!"), 15, "kit ranged", true);

        gui.addItem(ChatColor.GREEN + "Movement", Material.IRON_BOOTS,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a movement kit here!"), 29, "kit movement", true);

        gui.addItem(ChatColor.GREEN + "Support", Material.GOLDEN_APPLE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a support kit here!"), 31, "kit support", true);

        gui.addItem(ChatColor.GREEN + "Debuff", Material.REDSTONE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a debuff kit here!"), 33, "kit debuff", true);

        gui.addItem(ChatColor.DARK_PURPLE + "The Shop", Material.SUNFLOWER,
                Collections.singletonList(ChatColor.LIGHT_PURPLE + "Get teleported to the shop!"), 48, "coinshop", true);

        gui.addItem(ChatColor.BLUE + "Random kit", Material.SUSPICIOUS_STEW,
                Collections.singletonList(ChatColor.DARK_AQUA + "You will be given a random kit that you own."), 50, "Random", true);

        return gui;
    }

    /**
     * A GUI for the kit category of damage
     * @return The generated GUI
     */
    private static Gui damage(Player player) {
        Gui gui = new Gui("Damage Kits", 2);

        // Free
        gui.addKitItem(player, new Swordsman(), 0, "swordsman");

        // Voter
        gui.addKitItem(player, new Spearman(), 1, "spearman");

        // Level
        gui.addKitItem(player, new SpearKnight(), 2, "spearknight");

        // Coins
        gui.addKitItem(player, new Berserker(), 3, "berserker");
        gui.addKitItem(player, new Viking(), 4, "viking");
        gui.addKitItem(player, new Vanguard(),5, "vanguard");
        gui.addKitItem(player, new Executioner(),6, "executioner");
        gui.addKitItem(player, new Barbarian(), 7, "barbarian");

        gui.addBackItem(13, "kit");
        return gui;
    }

    /**
     * A GUI for the kit category of tank
     * @return The generated GUI
     */
    private static Gui tank(Player player) {
        Gui gui = new Gui("Tank Kits", 2);

        // Voter
        gui.addKitItem(player, new Shieldman(), 0, "shieldman");

        // Level
        gui.addKitItem(player, new Hypaspist(), 1, "hypaspist");

        // Coins
        gui.addKitItem(player, new Halberdier(), 2, "halberdier");
        gui.addKitItem(player, new Paladin(), 3, "paladin");

        // Staff
        gui.addKitItem(player, new Warbear(), 4, "warbear");

        gui.addBackItem(13, "kit");
        return gui;
    }

    /**
     * A GUI for the kit category of support
     * @return The generated GUI
     */
    private static Gui support(Player player) {
        Gui gui = new Gui("Support Kits", 2);

        // Level
        gui.addKitItem(player, new BattleMedic(),0, "battlemedic");

        // Coins
        gui.addKitItem(player, new Alchemist(), 1, "alchemist");
        gui.addKitItem(player, new Medic(), 2, "medic");
        gui.addKitItem(player, new Priest(), 3, "priest");
        gui.addKitItem(player, new Armorer(), 4, "armorer");

        gui.addBackItem(13, "kit");
        return gui;
    }

    /**
     * A GUI for the kit category of debuff
     * @return The generated GUI
     */
    private static Gui debuff(Player player) {
        Gui gui = new Gui("Debuff Kits", 2);

        // Coins
        gui.addKitItem(player, new Maceman(), 0, "maceman");
        gui.addKitItem(player, new Warhound(), 1, "warhound");
        gui.addKitItem(player, new Engineer(), 2, "engineer");
        gui.addKitItem(player, new Warlock(), 3, "warlock");

        gui.addBackItem(13, "kit");
        return gui;
    }

    /**
     * A GUI for the kit category of movement
     * @return The generated GUI
     */
    private static Gui movement(Player player) {
        Gui gui = new Gui("Movement Kits", 2);

        // Voter
        gui.addKitItem(player, new Skirmisher(), 0, "skirmisher");
        gui.addKitItem(player, new Scout(), 1, "scout");
        gui.addKitItem(player, new Ladderman(), 2, "ladderman");

        // Coins
        gui.addKitItem(player, new Rogue(), 3, "rogue");
        gui.addKitItem(player, new Cavalry(), 4, "cavalry");

        gui.addBackItem(13, "kit");
        return gui;
    }

    /**
     * A GUI for the kit category of ranged
     * @return The generated GUI
     */
    private static Gui ranged(Player player) {
        Gui gui = new Gui("Ranged Kits", 2);

        // Free
        gui.addKitItem(player, new Archer(), 0, "archer");

        // Voter
        gui.addKitItem(player, new FireArcher(), 1, "firearcher");

        // Coins
        gui.addKitItem(player, new Ranger(), 2, "ranger");
        gui.addKitItem(player, new Crossbowman(), 3, "crossbowman");
        gui.addKitItem(player, new Sorcerer(), 4, "sorcerer");

        gui.addBackItem(13, "kit");
        return gui;
    }
}
