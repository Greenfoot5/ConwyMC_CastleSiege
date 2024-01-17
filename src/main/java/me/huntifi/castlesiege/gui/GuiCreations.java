package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.kits.kits.coin_kits.*;
import me.huntifi.castlesiege.kits.kits.free_kits.Archer;
import me.huntifi.castlesiege.kits.kits.free_kits.Spearman;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.kits.kits.in_development.Sorcerer;
import me.huntifi.castlesiege.kits.kits.in_development.Warlock;
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
        GuiController.add("debuff", debuff());
        GuiController.add("movement", movement());
        GuiController.add("ranged", ranged());
    }

    public static Gui selector() {
        Gui gui = new Gui("selector", 6);
        gui.addItem(ChatColor.GREEN + "Damager", Material.IRON_SWORD,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a damager kit here!"), 11, "kit damage", true);

        gui.addItem(ChatColor.GREEN + "Tank", Material.DIAMOND_CHESTPLATE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a tank kit here!"), 13, "kit tank", true);

        gui.addItem(ChatColor.GREEN + "Support", Material.GOLDEN_APPLE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a support kit here!"), 15, "kit support", true);

        gui.addItem(ChatColor.GREEN + "Debuff", Material.REDSTONE,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a debuff kit here!"), 29, "kit debuff", true);

        gui.addItem(ChatColor.GREEN + "Movement", Material.IRON_BOOTS,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a movement kit here!"), 31, "kit movement", true);

        gui.addItem(ChatColor.GREEN + "Ranged", Material.BOW,
                Collections.singletonList(ChatColor.DARK_GREEN + "Select a ranged kit here!"), 33, "kit ranged", true);

        gui.addItem(ChatColor.DARK_PURPLE + "The Shop", Material.SUNFLOWER,
                Collections.singletonList(ChatColor.LIGHT_PURPLE + "Get teleported to the shop!"), 48, "coinshop", true);

        gui.addItem(ChatColor.BLUE + "Random kit", Material.SUSPICIOUS_STEW,
                Collections.singletonList(ChatColor.DARK_AQUA + "You will be given a random kit that you own."), 50, "Random", true);
        return gui;
    }

    public static Gui damage() {
        Gui gui = new Gui("damage", 2);

        gui.addItem("§a§lCLASS:§r§a Swordsman", Material.IRON_SWORD,
                Swordsman.getGuiDescription(), 0, "swordsman", true);

        gui.addItem("§a§lCLASS:§r§a Spearman", Material.STICK,
                Spearman.getGuiDescription(), 1, "spearman", true);

        gui.addItem("§2§lCLASS:§r§2 Spear-knight", Material.STICK,
                SpearKnight.getGuiDescription(), 2, "spearknight", true);

        gui.addItem("§6§lCLASS:§r§6 Berserker", Material.POTION,
                Berserker.getGuiDescription(), 3, "berserker", true);

        gui.addItem("§6§lCLASS:§r§6 Viking", Material.IRON_CHESTPLATE,
                Collections.singletonList(""), 4, "viking", true);

        gui.addItem("§6§lCLASS:§r§6 Vanguard", Material.DIAMOND_SWORD,
                Collections.singletonList(""), 5, "vanguard", true);

        gui.addItem("§6§lCLASS:§r§6 Executioner", Material.DIAMOND_AXE,
                Executioner.getGuiDescription(), 6, "executioner", true);

        gui.addItem("§6§lCLASS:§r§6 Barbarian", Material.NETHERITE_AXE,
                Barbarian.getGuiDescription(), 7, "barbarian", true);

        gui.addBackItem(13, "kit selector");
        return gui;
    }

    public static Gui tank() {
        Gui gui = new Gui("tank", 2);

        gui.addItem("§9§lCLASS:§r§9 Shieldman", Material.SHIELD,
                Shieldman.getGuiDescription(), 0, "shieldman", true);

        gui.addItem("§2§lCLASS:§r§2 Hypaspist", Material.GOLDEN_CHESTPLATE,
                Hypaspist.getGuiDescription(), 1, "hypaspist", true);

        gui.addItem("§6§lCLASS:§r§6 Halberdier", Material.NETHERITE_CHESTPLATE,
                Halberdier.getGuiDescription(), 2, "halberdier", true);

        gui.addItem("§3§lCLASS:§r§3 Warbear", Material.POLAR_BEAR_SPAWN_EGG,
                Collections.singletonList(""), 3, "warbear", true);

        gui.addItem("§6§lCLASS:§r§6 Paladin", Material.GOLDEN_AXE,
                Paladin.getGuiDescription(), 4, "paladin", true);

        gui.addBackItem(13, "kit selector");
        return gui;
    }

    public static Gui support() {
        Gui gui = new Gui("support", 2);

        gui.addItem("§2§lCLASS:§r§2 Battle Medic", Material.PAPER,
                BattleMedic.getGuiDescription(), 0, "battlemedic", true);

        gui.addItem("§6§lCLASS:§r§6 Alchemist", Material.BREWING_STAND,
                Alchemist.getGuiDescription(), 1, "alchemist", true);

        gui.addItem("§6§lCLASS:§r§6 Medic", Material.CAKE,
                Medic.getGuiDescription(), 2, "medic", true);

        gui.addItem("§6§lCLASS:§r§6 Priest", Material.SPECTRAL_ARROW,
                Priest.getGuiDescription(), 3, "priest", true);

        gui.addBackItem(13, "kit selector");
        return gui;
    }

    public static Gui debuff() {
        Gui gui = new Gui("debuff", 2);

        gui.addItem("§6§lCLASS:§r§6 Maceman", Material.IRON_SHOVEL,
                Maceman.getGuiDescription(), 0, "maceman", true);

        gui.addItem("§6§lCLASS:§r§6 Warhound", Material.GHAST_TEAR,
                Collections.singletonList(""), 1, "warhound", true);

        gui.addItem("§6§lCLASS:§r§6 Engineer", Material.COBWEB,
                Engineer.getGuiDescription(), 2, "engineer", true);

        gui.addItem("§6§lCLASS:§r§6 Warlock", Material.WITHER_SKELETON_SKULL,
                Warlock.getGuiDescription(), 3, "warlock", true);

        gui.addBackItem(13, "kit selector");
        return gui;
    }

    public static Gui movement() {
        Gui gui = new Gui("movement", 2);

        gui.addItem("§9§lCLASS:§r§9 Skirmisher", Material.IRON_BOOTS,
                Skirmisher.getGuiDescription(), 0, "skirmisher", true);

        gui.addItem("§9§lCLASS:§r§9 Scout", Material.LEATHER_BOOTS,
                Scout.getGuiDescription(), 1, "scout", true);

        gui.addItem("§9§lCLASS:§r§9 Ladderman", Material.LADDER,
                Ladderman.getGuiDescription(), 2, "ladderman", true);

        gui.addItem("§6§lCLASS:§r§6 Rogue", Material.NETHERITE_BOOTS,
                Rogue.getGuiDescription(), 3, "rogue", true);

        gui.addItem("§6§lCLASS:§r§6 Cavalry", Material.IRON_HORSE_ARMOR,
                Cavalry.getGuiDescription(), 4, "cavalry", true);


        gui.addBackItem(13, "kit selector");
        return gui;
    }

    public static Gui ranged() {
        Gui gui = new Gui("ranged", 2);

        gui.addItem("§a§lCLASS:§r§a Archer", Material.BOW,
                Archer.getGuiDescription(), 0, "archer", true);

        gui.addItem("§9§lCLASS:§r§9 Fire Archer", Material.BLAZE_POWDER,
                FireArcher.getGuiDescription(), 1, "firearcher", true);

        gui.addItem("§6§lCLASS:§r§6 Ranger", Material.LIME_DYE,
                Ranger.getGuiDescription(), 2, "ranger", true);

        gui.addItem("§6§lCLASS:§r§6 Crossbowman", Material.CROSSBOW,
                Crossbowman.getGuiDescription(), 3, "crossbowman", true);

        gui.addItem("§6§lCLASS:§r§6 Sorcerer", Material.AMETHYST_SHARD,
                Sorcerer.getGuiDescription(), 4, "sorcerer", true);

        gui.addBackItem(13, "kit selector");
        return gui;
    }
}
