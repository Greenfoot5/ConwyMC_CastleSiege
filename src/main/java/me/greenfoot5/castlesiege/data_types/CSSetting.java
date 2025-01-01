package me.greenfoot5.castlesiege.data_types;

import me.greenfoot5.conwymc.data_types.Setting;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A data holder for a castle siege setting
 */
public class CSSetting extends Setting {

    /**
     * Creates a CSSetting from a Setting
     * @param setting The setting to use
     */
    public CSSetting(@NotNull Setting setting) {
        super(setting);
    }

    /**
     * Generates a full array of settings for Castle Siege
     * @return Global settings + Castle Siege settings
     */
    public static Setting[] generateSettings() {
        Setting[] global = me.greenfoot5.conwymc.data_types.Setting.generateSettings();

        Setting[] castleSiege =  new Setting[] {
                new Setting("randomDeath", Material.SUSPICIOUS_STEW,
                        Component.text("Random Death"),
                        Collections.singletonList(Component.text("Each time you die, runs /random to give you a new random class"))),

                new Setting("deathMessages", Material.SKELETON_SKULL,
                        Component.text("Death Messages"),
                        Collections.singletonList(Component.text("View all death & kill messages, not just your own"))),

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
        Setting[] total = new Setting[global.length + castleSiege.length];
        System.arraycopy(global, 0, total, 0, global.length);
        System.arraycopy(castleSiege, 0, total, global.length, castleSiege.length);
        return total;
    }

    /**
     * Gets the default value for setting
     * @param setting The key of the setting
     * @return The default value for that setting or null if one isn't found
     */
    public static String getDefault(String setting) {
        Setting[] settings = generateSettings();
        for (Setting s : settings) {
            if (Objects.equals(s.key, setting)) {
                return s.values[0];
            }
        }
        return null;
    }
}
