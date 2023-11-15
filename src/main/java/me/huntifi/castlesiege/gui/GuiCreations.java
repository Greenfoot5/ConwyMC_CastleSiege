package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.kits.kits.donator_kits.Alchemist;
import me.huntifi.castlesiege.kits.kits.donator_kits.Berserker;
import me.huntifi.castlesiege.kits.kits.donator_kits.Cavalry;
import me.huntifi.castlesiege.kits.kits.donator_kits.Crossbowman;
import me.huntifi.castlesiege.kits.kits.free_kits.Archer;
import me.huntifi.castlesiege.kits.kits.free_kits.Spearman;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.kits.kits.level_kits.BattleMedic;
import me.huntifi.castlesiege.kits.kits.level_kits.Hypaspist;
import me.huntifi.castlesiege.kits.kits.level_kits.SpearKnight;
import me.huntifi.castlesiege.kits.kits.voter_kits.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Collections;

public class GuiCreations {

    public static void registerGUIs() {
        GuiController.add("selector", selector());
        GuiController.add("damage", damage());
        GuiController.add("tank", tank());
        GuiController.add("support", support());
        GuiController.add("controller", controller());
        GuiController.add("lurker", lurker());
        GuiController.add("ranged", ranged());
    }

    public static Gui selector() {
        Gui gui = new Gui("selector", 6);
        gui.addItem(ChatColor.GREEN + "DPS (Damage)", Material.NETHERITE_SWORD,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a DPS (damage) kit here!"), 11, "kit damage", true);

        gui.addItem(ChatColor.GREEN + "Tank", Material.DIAMOND_CHESTPLATE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a tank kit here!"), 13, "kit tank", true);

        gui.addItem(ChatColor.GREEN + "Support", Material.DRAGON_BREATH,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a support kit here!"), 15, "kit support", true);

        gui.addItem(ChatColor.GREEN + "Controller", Material.REDSTONE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a controller kit here!"), 29, "kit controller", true);

        gui.addItem(ChatColor.GREEN + "Lurker", Material.DRIED_KELP,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a lurker kit here!"), 31, "kit lurker", true);

        gui.addItem(ChatColor.GREEN + "Ranged", Material.BOW,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a ranged kit here!"), 33, "kit ranged", true);

        gui.addItem(ChatColor.DARK_PURPLE + "The Shop", Material.SUNFLOWER,
                Collections.singletonList(ChatColor.LIGHT_PURPLE + "Get teleported to the shop!"), 48, "coinshop", true);

        gui.addItem(ChatColor.BLUE + "Random kit", Material.COOKIE,
                Collections.singletonList(ChatColor.DARK_AQUA + "You will be given a random kit that you own."), 50, "Random", true);
        return gui;
    }

    public static Gui damage() {
        Gui gui = new Gui("damage", 2);

        gui.addItem("§a§lCLASS:§r§a Swordsman", Material.IRON_SWORD,
                Swordsman.loreStats(), 0, "swordsman", true);

        gui.addItem("§a§lCLASS:§r§a Spearman", Material.STICK,
                Spearman.loreStats(), 1, "spearman", true);

        gui.addItem("§9§lCLASS:§r§9 Skirmisher", Material.IRON_BOOTS,
                Skirmisher.loreStats(), 2, "skirmisher", true);

        gui.addItem("§2§lCLASS:§r§2 Spear-knight", Material.STICK,
                SpearKnight.loreStats(), 3, "spearknight", true);

        gui.addItem("§6§lCLASS:§r§6 Berserker", Material.POTION,
                Berserker.loreStats(), 4, "berserker", true);

        gui.addItem("§6§lCLASS:§r§6 Cavalry", Material.IRON_HORSE_ARMOR,
                Cavalry.loreStats(), 5, "cavalry", true);

        gui.addItem("§6§lCLASS:§r§6 Viking", Material.IRON_CHESTPLATE,
                Collections.singletonList(""), 6, "viking", true);

        gui.addItem("§6§lCLASS:§r§6 Vanguard", Material.DIAMOND_SWORD,
                Collections.singletonList(""), 7, "vanguard", true);

        gui.addItem("§6§lCLASS:§r§6 Executioner", Material.DIAMOND_AXE,
                Collections.singletonList(""), 8, "executioner", true);

        gui.addItem("§4§lGo back", Material.BARRIER,
                Collections.singletonList("§cReturn to the previous interface."), 13, "kit selector", true);
        return gui;
    }

    public static Gui tank() {
        Gui gui = new Gui("tank", 2);

        gui.addItem("§9§lCLASS:§r§9 Shieldman", Material.SHIELD,
                Shieldman.loreStats(), 0, "shieldman", true);

        gui.addItem("§2§lCLASS:§r§2 Hypaspist", Material.GOLDEN_CHESTPLATE,
                Hypaspist.loreStats(), 1, "hypaspist", true);

        gui.addItem("§6§lCLASS:§r§6 Halberdier", Material.NETHERITE_CHESTPLATE,
                Collections.singletonList(""), 2, "halberdier", true);

        gui.addItem("§4§lGo back", Material.BARRIER,
                Collections.singletonList("§cReturn to the previous interface."), 13, "kit selector", true);
        return gui;
    }

    public static Gui support() {
        Gui gui = new Gui("support", 2);

        gui.addItem("§2§lCLASS:§r§2 Battle Medic", Material.PAPER,
                BattleMedic.loreStats(), 0, "battlemedic", true);

        gui.addItem("§6§lCLASS:§r§6 Alchemist", Material.BREWING_STAND,
                Alchemist.loreStats(), 1, "alchemist", true);

        gui.addItem("§6§lCLASS:§r§6 Medic", Material.CAKE,
                Collections.singletonList(""), 2, "medic", true);

        gui.addItem("§4§lGo back", Material.BARRIER,
                Collections.singletonList("§cReturn to the previous interface."), 13, "kit selector", true);
        return gui;
    }

    public static Gui controller() {
        Gui gui = new Gui("controller", 2);

        gui.addItem("§6§lCLASS:§r§6 Maceman", Material.IRON_SHOVEL,
                Collections.singletonList(""), 0, "maceman", true);

        gui.addItem("§6§lCLASS:§r§6 Warhound", Material.GHAST_TEAR,
                Collections.singletonList(""), 1, "warhound", true);

        gui.addItem("§6§lCLASS:§r§6 Engineer", Material.COBWEB,
                Collections.singletonList(""), 2, "engineer", true);

        gui.addItem("§4§lGo back", Material.BARRIER,
                Collections.singletonList("§cReturn to the previous interface."), 13, "kit selector", true);
        return gui;
    }

    public static Gui lurker() {
        Gui gui = new Gui("lurker", 2);

        gui.addItem("§9§lCLASS:§r§9 Scout", Material.LEATHER_BOOTS,
                Scout.loreStats(), 0, "scout", true);

        gui.addItem("§9§lCLASS:§r§9 Ladderman", Material.LADDER,
                Ladderman.loreStats(), 1, "ladderman", true);

        gui.addItem("§6§lCLASS:§r§6 Rogue", Material.NETHERITE_BOOTS,
                Collections.singletonList(""), 2, "rogue", true);

        gui.addItem("§4§lGo back", Material.BARRIER,
                Collections.singletonList("§cReturn to the previous interface."), 13, "kit selector", true);
        return gui;
    }

    public static Gui ranged() {
        Gui gui = new Gui("ranged", 2);

        gui.addItem("§a§lCLASS:§r§a Archer", Material.BOW,
                Archer.loreStats(), 0, "archer", true);

        gui.addItem("§9§lCLASS:§r§9 Fire Archer", Material.BLAZE_POWDER,
                FireArcher.loreStats(), 1, "firearcher", true);

        gui.addItem("§6§lCLASS:§r§6 Ranger", Material.LIME_DYE,
                Collections.singletonList(""), 2, "ranger", true);

        gui.addItem("§6§lCLASS:§r§6 Crossbowman", Material.CROSSBOW,
                Crossbowman.loreStats(), 3, "crossbowman", true);

        gui.addItem("§4§lGo back", Material.BARRIER,
                Collections.singletonList("§cReturn to the previous interface."), 13, "kit selector", true);
        return gui;
    }
}
