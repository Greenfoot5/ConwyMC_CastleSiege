package me.greenfoot5.advancements.api.displays;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.database.TeamProgression;
import com.fren_gor.ultimateAdvancementAPI.visibilities.IVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ShownNodeDisplay extends NodeDisplay implements IVisibility {
    public ShownNodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y, @NotNull String title, @NotNull String description, String parent) {
        super(icon, frame, showToast, announceChat, x, y, title, description);
        setParentKey(parent);
    }

    public ShownNodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y, @NotNull String title, @NotNull String description, String parent, @NotNull int maxProgression) {
        super(icon, frame, showToast, announceChat, x, y, title, description, maxProgression);
        setParentKey(parent);
    }

    /**
     * Whether the provided advancement is visible for the specified team.
     *
     * @param advancement The advancement.
     * @param progression The team {@link TeamProgression}.
     * @return Whether the advancement is visible for the specified team.
     */
    @Override
    public boolean isVisible(@NotNull Advancement advancement, @NotNull TeamProgression progression) {
        return true;
    }
}