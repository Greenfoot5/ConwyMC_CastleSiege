package me.huntifi.castlesiege.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

public class MenuGUI {

    /**
     * Displays the kit gui for a category or category selection
     * @return The menu to show the player
     */
    public static Gui getGUI() {
            return selector();
    }

    /**
     * Creates a menu GUI
     * @return The generated GUI
     */
    private static Gui selector() {
        Gui gui = new Gui(Component.text("Game Menu"), 6);
        gui.addItem(Component.text("Your Stats", NamedTextColor.GREEN), Material.WRITABLE_BOOK,
                Collections.singletonList(Component.text("Check your stats!", NamedTextColor.DARK_GREEN)),
                10, "mystats", true);

        gui.addItem(Component.text("Secrets", NamedTextColor.GREEN), Material.GOLDEN_SHOVEL,
                Collections.singletonList(Component.text("Show your secrets!", NamedTextColor.DARK_GREEN)),
                12, "secrets", true);

        gui.addItem(Component.text("Voting Websites", NamedTextColor.GREEN), Material.DIAMOND,
                Collections.singletonList(Component.text("See the voting websites", NamedTextColor.DARK_GREEN)),
                14, "vote", true);

        gui.addItem(Component.text("Coin-shop", NamedTextColor.GREEN), Material.SUNFLOWER,
                Collections.singletonList(Component.text("Enter the Coin-Shop", NamedTextColor.DARK_GREEN)),
                16, "Coinshop", true);

        gui.addItem(Component.text("Daily Reward", NamedTextColor.GREEN), Material.EMERALD,
                Collections.singletonList(Component.text("Open your daily reward.", NamedTextColor.DARK_GREEN)),
                30, "Daily", true);

        gui.addItem(Component.text("Rankpoint Rewards", NamedTextColor.GREEN), Material.ENCHANTED_GOLDEN_APPLE,
                Collections.singletonList(Component.text("See your rankpoint rewards", NamedTextColor.DARK_GREEN)),
                28, "rprewards", true);

        gui.addItem(Component.text("Settings", NamedTextColor.DARK_PURPLE), Material.IRON_HORSE_ARMOR,
                Collections.singletonList(Component.text("Open your Castle Siege settings", NamedTextColor.LIGHT_PURPLE)),
                32, "settings", true);

        gui.addItem(Component.text("Respawn", NamedTextColor.BLUE), Material.SUSPICIOUS_STEW,
                Collections.singletonList(Component.text("Respawn at the lobby", NamedTextColor.DARK_AQUA)),
                34, "Suicide", true);

        gui.addBackItem(49, "menu");

        return gui;
    }

    public ItemStack createSkullItem(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        item.setItemMeta(meta);
        return item;
    }
}
