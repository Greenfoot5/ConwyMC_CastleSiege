package me.greenfoot5.advancements.api.advancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.database.TeamProgression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * An advancements that requires you have a certain level to view
 */
public class HiddenAdvancement extends StandardAdvancement {
    /**
     * Creates a new {@code BaseAdvancement} with a maximum progression of {@code 1}.
     * <p>The tab of this advancement will be the parent one.
     *
     * @param key     The unique key of the advancement. It must be unique among the other advancements of the tab.
     * @param display The display information of this advancement.
     * @param parent  The parent of this advancement.
     */
    public HiddenAdvancement(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent) {
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
    public HiddenAdvancement(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent, @Range(from = 1L, to = 2147483647L) int maxProgression) {
        super(key, display, parent, maxProgression);
    }

    /**
     * Advancement is visible if any team member meets the level requirements
     *
     * @param progression The team {@link TeamProgression}.
     * @return Whether the advancement is visible for the specified team.
     */
    @Override
    public boolean isVisible(@NotNull TeamProgression progression) {
        if (!super.isVisible(progression))
            return false;

        return progression.getProgression(this) >= getMaxProgression();
    }
}
