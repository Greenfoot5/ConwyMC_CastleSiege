package me.greenfoot5.castlesiege.gui;

import me.greenfoot5.castlesiege.kits.kits.coin_kits.Alchemist;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Barbarian;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Berserker;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Cavalry;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Crossbowman;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Engineer;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Executioner;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Halberdier;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Maceman;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Medic;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Paladin;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Priest;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Ranger;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Rogue;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Sorcerer;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Vanguard;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Viking;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Warhound;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Warlock;
import me.greenfoot5.castlesiege.kits.kits.event_kits.HallowedHorseman;
import me.greenfoot5.castlesiege.kits.kits.event_kits.Vampire;
import me.greenfoot5.castlesiege.kits.kits.event_kits.Werewolf;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Archer;
import me.greenfoot5.castlesiege.kits.kits.level_kits.Shieldman;
import me.greenfoot5.castlesiege.kits.kits.level_kits.Spearman;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Swordsman;
import me.greenfoot5.castlesiege.kits.kits.in_development.Armorer;
import me.greenfoot5.castlesiege.kits.kits.level_kits.BattleMedic;
import me.greenfoot5.castlesiege.kits.kits.level_kits.Hypaspist;
import me.greenfoot5.castlesiege.kits.kits.level_kits.SpearKnight;
import me.greenfoot5.castlesiege.kits.kits.staff_kits.Warbear;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.FireArcher;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.Ladderman;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.Scout;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.Skirmisher;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Generates dynamic kit selection GUIs
 */
public class KitGUIs {

    public final static String[] OPTIONS = {"damage", "tank", "support", "debuff", "movement", "ranged", "event", "secret"};

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
            case "event":
                return event(p);
            case "secret":
                return secret(p);
            default:
                Messenger.sendError("Unknown category: <red>" + category, p);
                return null;
        }
    }

    /**
     * Creates a kit category GUI
     * @return The generated GUI
     */
    // TODO - Maybe list how many of the total have been collected?
    private static Gui selector() {
        Gui gui = new Gui(Component.text("Categories"), 6);
        gui.addItem(Component.text("Damager", NamedTextColor.GREEN), Material.IRON_SWORD,
                Collections.singletonList(Component.text("Select a damager kit here!", NamedTextColor.DARK_GREEN)),
                        10, "kit damage", true);

        gui.addItem(Component.text("Tank", NamedTextColor.GREEN), Material.DIAMOND_CHESTPLATE,
                Collections.singletonList(Component.text("Select a tank kit here!", NamedTextColor.DARK_GREEN)),
                12, "kit tank", true);

        gui.addItem(Component.text("Ranged", NamedTextColor.GREEN), Material.BOW,
                Collections.singletonList(Component.text("Select a ranged kit here!", NamedTextColor.DARK_GREEN)),
                14, "kit ranged", true);

        gui.addItem(Component.text("Movement", NamedTextColor.GREEN), Material.IRON_BOOTS,
                Collections.singletonList(Component.text("Select a movement kit here!", NamedTextColor.DARK_GREEN)),
                16, "kit movement", true);

        gui.addItem(Component.text("Support", NamedTextColor.GREEN), Material.GOLDEN_APPLE,
                Collections.singletonList(Component.text("Select a support kit here!", NamedTextColor.DARK_GREEN)),
                28, "kit support", true);

        gui.addItem(Component.text("Debuff", NamedTextColor.GREEN), Material.REDSTONE,
                Collections.singletonList(Component.text("Select a debuff kit here!", NamedTextColor.DARK_GREEN)),
                30, "kit debuff", true);

        gui.addItem(Component.text("Event", NamedTextColor.GOLD), Material.CLOCK,
                Collections.singletonList(Component.text("Select an event kit here!", NamedTextColor.YELLOW)),
                32, "kit event", true);

        gui.addItem(Component.text("Secret", NamedTextColor.DARK_RED), Material.END_CRYSTAL,
                Collections.singletonList(Component.text("Select a secret kit here!", NamedTextColor.RED)),
                34, "kit secret", true);

        gui.addItem(Component.text("The Shop", NamedTextColor.DARK_PURPLE), Material.SUNFLOWER,
                Collections.singletonList(Component.text("Get teleported to the shop!", NamedTextColor.LIGHT_PURPLE)),
                48, "coinshop", true);

        gui.addItem(Component.text("Random kit", NamedTextColor.BLUE), Material.SUSPICIOUS_STEW,
                Collections.singletonList(Component.text("You will be given a random kit that you own.", NamedTextColor.DARK_AQUA)),
                50, "Random", true);

        return gui;
    }

    /**
     * A GUI for the kit category of damage
     * @return The generated GUI
     */
    private static Gui damage(Player player) {
        Gui gui = new Gui(Component.text("Damage Kits"), 2);

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
        Gui gui = new Gui(Component.text("Tank Kits"), 2);

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
        Gui gui = new Gui(Component.text("Support Kits"), 2);

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
        Gui gui = new Gui(Component.text("Debuff Kits"), 2);

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
        Gui gui = new Gui(Component.text("Movement Kits"), 2);

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
        Gui gui = new Gui(Component.text("Ranged Kits"), 2);

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

    /**
     * A GUI for the kit category of events
     * @return The generated GUI
     */
    private static Gui event(Player player) {
        Gui gui = new Gui(Component.text("Event Kits"), 2);
        gui.addKitItem(player, new Vampire(), 0, "Vampire");
        gui.addKitItem(player, new HallowedHorseman(), 1, "HallowedHorseman");
        gui.addKitItem(player, new Werewolf(), 2, "Werewolf");
        gui.addBackItem(13, "kit");
        return gui;
    }

    /**
     * A GUI for the kit category of secret
     * @return The generated GUI
     */
    private static Gui secret(Player player) {
        Gui gui = new Gui(Component.text("Secret Kits", TextColor.color(198, 0, 0)), 2);
        gui.addBackItem(13, "kit");
        return gui;
    }


}
