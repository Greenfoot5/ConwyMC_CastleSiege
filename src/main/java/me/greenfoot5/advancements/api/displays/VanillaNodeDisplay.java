package me.greenfoot5.advancements.api.displays;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class VanillaNodeDisplay extends NodeDisplay implements VanillaVisibility {

    public VanillaNodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y, @NotNull String title, @NotNull String description, String parent) {
        super(icon, frame, showToast, announceChat, x, y, title, description);
        setParentKey(parent);
    }
}