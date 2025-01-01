package me.greenfoot5.castlesiege.advancements.displays;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class HiddenNodeDisplay extends NodeDisplay implements HiddenVisibility {
    public HiddenNodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y, @NotNull String title, @NotNull String description, String parent) {
        super(icon, frame, showToast, announceChat, x, y, title, description);
        setParentKey(parent);
    }
}