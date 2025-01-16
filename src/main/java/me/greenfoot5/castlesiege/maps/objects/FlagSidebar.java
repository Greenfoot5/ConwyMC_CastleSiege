package me.greenfoot5.castlesiege.maps.objects;

import me.greenfoot5.castlesiege.maps.CoreMap;
import me.greenfoot5.castlesiege.maps.Gamemode;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Scoreboard;
import me.greenfoot5.castlesiege.maps.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The scoreboard displaying all the flags and their capture status
 */
public class FlagSidebar {
    private final Sidebar sidebar;
    private final ComponentSidebarLayout componentSidebar;
    private final SidebarAnimation<Component> titleAnimation;
    private final SidebarAnimation<SidebarComponent> pagesAnimation;
    private int lastChangeSeconds = 0;

    /**
     * @param sidebar The sidebar to create the flags scoreboard on
     */
    public FlagSidebar(@NotNull Sidebar sidebar) {
        this.sidebar = sidebar;

        this.titleAnimation = Scoreboard.createGradientAnimation(Component.text("Mode: ")
                .append(Component.text(MapController.getCurrentMap().gamemode.toString(),
                        Style.style(TextDecoration.BOLD))));
        SidebarComponent title = SidebarComponent.animatedLine(titleAnimation);

        // Calculate the number of lines needed to display stuff
        int availableLines = sidebar.maxLines() - 3;

        // Calculate how many lines we need to display cores, lives & flags
        int requiredLines = getRequiredLines();

        // If we need multiple pages we'll need to display the page number
        final int pages;
        if (requiredLines > availableLines) {
            availableLines -= 1;
            pages = requiredLines / availableLines + (requiredLines % availableLines == 0 ? 0 : 1);
        } else {
            pages = 1;
        }

        int coreIndex = 0;
        int lifeIndex = 0;
        int flagIndex = 0;

        List<SidebarComponent> sidebarPages = new ArrayList<>();
        for (int page = 0; page < pages; page++) {
            // Map Info
            SidebarComponent.Builder lines = SidebarComponent.builder()
                    .addDynamicLine(() -> {
                        Component time = Scoreboard.getTimeText();
                        return time.color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD);
                    })
                    .addStaticLine(Component.text("Map: ", NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
                            .append(Component.text(MapController.getCurrentMap().name, NamedTextColor.GREEN)
                                    .decoration(TextDecoration.BOLD, false)))
                    // 30 dashes to define minimum width
                    .addStaticLine(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY).decorate(TextDecoration.STRIKETHROUGH));

            int linesLeft = availableLines;

            // Core Map
            if (MapController.getCurrentMap() instanceof CoreMap coreMap && coreIndex < coreMap.getCores().length) {
                lines.addStaticLine(Component.text("Cores", NamedTextColor.WHITE).decorate(TextDecoration.BOLD));
                linesLeft--;

                // Display the core scoreboard
                while (coreIndex < coreMap.getCores().length && linesLeft > 0) {
                    lines.addComponent(coreMap.getCore(coreIndex));
                    linesLeft--;
                    coreIndex++;
                }

                if (MapController.getCurrentMap().flags.length > 0 && linesLeft > 5) {
                    lines.addBlankLine();
                    linesLeft--;
                }
            } else if (MapController.getCurrentMap().gamemode == Gamemode.Assault && lifeIndex < MapController.getCurrentMap().teams.length) {
                lines.addStaticLine(Component.text("Lives", NamedTextColor.WHITE).decorate(TextDecoration.BOLD));
                linesLeft--;

                while (lifeIndex < MapController.getCurrentMap().teams.length && linesLeft > 0) {
                    if (MapController.getCurrentMap().teams[lifeIndex].getLives() >= 0) {
                        lines.addComponent(MapController.getCurrentMap().teams[lifeIndex]);
                        linesLeft--;
                    }
                    lifeIndex++;
                }

                if (MapController.getCurrentMap().flags.length > 0 && linesLeft > 5) {
                    lines.addBlankLine();
                    linesLeft--;
                }
            }

            // Flip the order of flags, so they're in a more logical order
            Flag[] reversedFlags = MapController.getCurrentMap().flags.clone();
            ArrayUtils.reverse(reversedFlags);
            // Display the flag scoreboard
            while (flagIndex < reversedFlags.length && linesLeft > 0) {
                if (reversedFlags[flagIndex].isActive()) {
                    lines.addComponent(reversedFlags[flagIndex]);
                    linesLeft--;
                    flagIndex++;
                }
            }

            if (pages > 1) {
                lines.addStaticLine(Component.text("Page " + (page + 1) + " of " + pages, NamedTextColor.WHITE));
            }

            sidebarPages.add(lines.build());
        }

        pagesAnimation = new CollectionSidebarAnimation<>(sidebarPages);
        SidebarComponent paginatedComponent = SidebarComponent.animatedComponent(pagesAnimation);
        this.componentSidebar = new ComponentSidebarLayout(title, paginatedComponent);
    }

    private static int getRequiredLines() {
        int requiredLines = 0;
        if (MapController.getCurrentMap() instanceof CoreMap coreMap) {
            requiredLines += coreMap.getCores().length + 1;
            if (coreMap.flags.length > 0)
                requiredLines++;
        } else if (MapController.getCurrentMap().gamemode == Gamemode.Assault) {
            requiredLines += 2;
            for (Team team : MapController.getCurrentMap().teams) {
                if (team.getLives() >= 0) {
                    requiredLines += 1;
                }
            }
        }

        for (Flag flag : MapController.getCurrentMap().flags) {
            if (flag.isActive()) {
                requiredLines++;
            }
        }
        return requiredLines;
    }

    /**
     * Calls the next tick of the animation
     */
    public void tick() {
        // Advance title animation to the next frame
        titleAnimation.nextFrame();
        if (MapController.timer != null
                && MapController.timer.seconds % 5 == 0
                && lastChangeSeconds != MapController.timer.seconds) {
            pagesAnimation.nextFrame();
            lastChangeSeconds = MapController.timer.seconds;
        }

        // Update sidebar title & lines
        componentSidebar.apply(sidebar);
    }

    /**
     * @param player The player to add to the scoreboard
     */
    public void addPlayer(Player player) {
        sidebar.addPlayer(player);
    }

    /**
     * @param player The player to remove
     */
    public void removePlayer(Player player) {
        sidebar.removePlayer(player);
    }

    /**
     * Closes the sidebar
     */
    public void close() {
        sidebar.close();
    }
}
