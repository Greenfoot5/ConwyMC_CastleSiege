package me.greenfoot5.castlesiege.advancements.displays;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class NodeDisplay extends AdvancementDisplay {
    private String parentKey;
    public int maxProgression;

    public NodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                               @NotNull Component title, @NotNull Component description) {
        super(icon, LegacyComponentSerializer.legacySection().serialize(title), frame, showToast, announceChat, x, y,
                LegacyComponentSerializer.legacySection().serialize(description));
    }

    public NodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                               @NotNull String title, @NotNull Component description) {
        super(icon, mmConvert(title), frame, showToast, announceChat, x, y,
                LegacyComponentSerializer.legacySection().serialize(description));
    }

    public NodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                               @NotNull Component title, @NotNull String description) {
        super(icon, LegacyComponentSerializer.legacySection().serialize(title), frame, showToast, announceChat, x, y,
                mmConvert(description));
    }

    public NodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                               @NotNull String title, @NotNull String description) {
        super(icon, mmConvert(title), frame, showToast, announceChat, x, y,
                mmConvert(description));
    }

    public NodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                          @NotNull String title, @NotNull String description, @NotNull int maxProgression) {
        super(icon, mmConvert(title), frame, showToast, announceChat, x, y,
                mmConvert(description));
        this.maxProgression = maxProgression;
    }

    public static String mmConvert(String input) {
        Component convert = MiniMessage.miniMessage().deserialize(input);
        return LegacyComponentSerializer.legacySection().serialize(convert);
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = cleanKey(parentKey);
    }

    /**
     * Remove invalid characters for a key
     * @param input The key to clean
     * @return A cleaned key allowing [a-zA-Z0-9|._\-\/]
     */
    public static String cleanKey(String input) {
        if (input == null) return null;
        // Remove MiniMessage
        input = input.toLowerCase().replaceAll("<.*?>", "");
        // Remove illegal characters
        return input.toLowerCase().replaceAll("[^a-zA-Z0-9|._\\-/]","");
    }
}
