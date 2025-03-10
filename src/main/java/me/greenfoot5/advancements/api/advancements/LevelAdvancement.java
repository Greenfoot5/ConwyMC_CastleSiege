package me.greenfoot5.advancements.api.advancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.database.TeamProgression;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.LoadData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.UUID;

/**
 * An advancements that requires you have a certain level to view
 */
public class LevelAdvancement extends StandardAdvancement {
    private final int level;

    /**
     * Creates a new {@code BaseAdvancement} with a maximum progression of {@code 1}.
     * <p>The tab of this advancement will be the parent one.
     *
     * @param key     The unique key of the advancement. It must be unique among the other advancements of the tab.
     * @param display The display information of this advancement.
     * @param parent  The parent of this advancement.
     * @param requiredLevel Minimum level required to view
     */
    public LevelAdvancement(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent, @NotNull int requiredLevel) {
        super(key, display, parent);
        this.level = requiredLevel;
    }

    /**
     * Creates a new {@code BaseAdvancement}.
     * <p>The tab of this advancement will be the parent one.
     *
     * @param key            The unique key of the advancement. It must be unique among the other advancements of the tab.
     * @param display        The display information of this advancement.
     * @param parent         The parent of this advancement.
     * @param requiredLevel  Minimum level required to view
     * @param maxProgression The maximum advancement progression.
     */
    public LevelAdvancement(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent, @NotNull int requiredLevel, @Range(from = 1L, to = 2147483647L) int maxProgression) {
        super(key, display, parent, maxProgression);
        this.level = requiredLevel;
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

        for (UUID member : progression.getMembersCopy())
        {
            CSPlayerData data = CSActiveData.getData(member);
            if (data == null)
                data = LoadData.load(member);

            if (data != null)
                if (data.getLevel() >= level)
                    return true;
        }

        return false;
    }
}
