package me.greenfoot5.advancements.api.advancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import me.greenfoot5.advancements.CSAdvancementController;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * An override of base advancement allowing utility functions when creating
 */
public class StandardAdvancement extends BaseAdvancement {
    /**
     * Creates a new {@code BaseAdvancement} with a maximum progression of {@code 1}.
     * <p>The tab of this advancement will be the parent one.
     *
     * @param key     The unique key of the advancement. It must be unique among the other advancements of the tab.
     * @param display The display information of this advancement.
     * @param parent  The parent of this advancement.
     */
    public StandardAdvancement(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent) {
        super(key, display, parent);
    }

    /**
     * Creates a new {@code BaseAdvancement}.
     * <p>The tab of this advancement will be the parent one.
     *
     * @param key            The unique key of the advancement. It must be unique among the other advancements of the tab.
     * @param display        The display information of this advancement.
     * @param parent         The parent of this advancement.
     * @param maxProgression The maximum advancement progression.
     */
    public StandardAdvancement(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent, @Range(from = 1L, to = 2147483647L) int maxProgression) {
        super(key, display, parent, maxProgression);
    }

    @Override
    public void grant(@NotNull Player player) {
        grant(player, true);
    }

    @Override
    public void grant(@NotNull Player player, boolean giveRewards) {
        super.grant(player, giveRewards);
        // Remove duplicate whitespace from toast
        CSAdvancementController.api.displayCustomToast(player, display.getIcon(),
                display.getTitle().replaceAll("\\s+", " "), display.getFrame());
    }
}
