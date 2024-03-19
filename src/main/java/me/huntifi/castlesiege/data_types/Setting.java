package me.huntifi.castlesiege.data_types;

import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A data holder for a Setting
 */
public class Setting {
    public TextComponent displayName;
    public String key;
    /* Index 0 is default */
    public String[] values = new String[]{"false", "true"};
    public List<Component> itemLore;
    public Material material;

    /**
     * @param displayName The display name for the setting
     * @param key The key to use in the database
     * @param lore The item lore to use in GUIs
     * @param material The material to display in GUIs
     */
    public Setting(@NotNull String key, Material material, TextComponent displayName, List<Component> lore) {
        this.displayName = displayName.colorIfAbsent(NamedTextColor.GOLD);
        this.key = key;
        this.itemLore = setLoreBlue(lore);
        this.material = material;
    }

    /**
     * @param displayName The display name for the setting
     * @param key The key to use in the database
     * @param lore The item lore to use in GUIs
     * @param values The values the setting can have. Index 0 is default
     * @param material The material to display in GUIs
     */
    public Setting(@NotNull String key, Material material, TextComponent displayName, List<Component> lore, String[] values) {
        this.displayName = displayName.colorIfAbsent(NamedTextColor.GOLD);
        this.key = key;
        this.itemLore = setLoreBlue(lore);
        this.material = material;
        this.values = values;
    }

    /**
     * Creates a clone of a Setting
     * @param setting The setting to clone
     */
    public Setting(@NotNull Setting setting) {
        this.displayName = setting.displayName;
        this.key = setting.key;
        this.values = setting.values;
        this.itemLore = setting.itemLore;
        this.material = setting.material;
    }

    public static Setting[] generateSettings() {
        return new Setting[] {
                new Setting("randomDeath", Material.SUSPICIOUS_STEW,
                        Component.text("Random Death"),
                        Collections.singletonList(Component.text("Each time you die, runs /random to give you a new random class"))),

                new Setting("deathMessages", Material.SKELETON_SKULL,
                        Component.text("Death Messages"),
                        Collections.singletonList(Component.text("View all death & kill messages, not just your own"))),

                new Setting("joinPing", Material.BELL,
                        Component.text("Join Notification"),
                        Collections.singletonList(Component.text("Get a ping sound when another player joins the server"))),

                new Setting("scoreboard", Material.PAPER,
                        Component.text("Stats Scoreboard"),
                        List.of(Component.text("Changes which scoreboard will be displayed"),
                                Component.empty(),
                                Messenger.mm.deserialize("Types: <dark_aqua>flag</dark_aqua>, <dark_aqua>stats</dark_aqua>, <dark_aqua>stats_icons</dark_aqua>")),
                        new String[]{"flag", "stats", "stats_icons"}),

                new Setting("alwaysInfo", Material.BLUE_DYE,
                        Component.text("Level Dependent Info"),
                        List.of(Component.text("Shows any level dependent info even after you've passed the threshold"),
                                Component.empty(),
                                Component.text("By default certain info messages/reminders appear "),
                                Component.text("until you reach a certain level to remind you how to play."),
                                Component.text("Once you reach that level, the messages will no longer appear unless "),
                                Component.text("this setting is enabled.")))
        };
    }

    private List<Component> setLoreBlue(List<Component> lore) {
        if (lore == null) {
            return null;
        }

        ArrayList<Component> list = new ArrayList<>();
        for (Component c : lore) {
            if (c != null) {
                c = c.colorIfAbsent(NamedTextColor.BLUE);
                list.add(c);
            }
        }
        return list;
    }
}
