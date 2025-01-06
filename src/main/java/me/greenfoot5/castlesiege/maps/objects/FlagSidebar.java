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
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The scoreboard displaying all the flags and their capture status
 */
public class FlagSidebar {
    private final Sidebar sidebar;
    private final ComponentSidebarLayout componentSidebar;
    private final SidebarAnimation<Component> titleAnimation;

    /**
     * @param sidebar The sidebar to create the flags scoreboard on
     */
    public FlagSidebar(@NotNull Sidebar sidebar) {
        this.sidebar = sidebar;

        this.titleAnimation = Scoreboard.createGradientAnimation(Component.text("Mode: ")
                .append(Component.text(MapController.getCurrentMap().gamemode.toString(),
                        Style.style(TextDecoration.BOLD))));

        SidebarComponent title = SidebarComponent.animatedLine(titleAnimation);

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


        // Core Map
        if (MapController.getCurrentMap() instanceof CoreMap coreMap) {
            // Display the core scoreboard
            for (Core core : coreMap.getCores()) {
                Team owners = MapController.getCurrentMap().getTeam(core.getOwners());
                NamedTextColor color = owners == null ? NamedTextColor.GRAY : owners.primaryChatColor;
                lines.addStaticLine(Component.text(core.name, color).decorate(TextDecoration.BOLD));
                lines.addDynamicLine(() ->
                        Component.text("Health: ", color).decorate(TextDecoration.BOLD)
                                .append(Component.text(core.health, NamedTextColor.WHITE)
                                        .decoration(TextDecoration.BOLD, false)));
            }
        } else if (MapController.getCurrentMap().gamemode == Gamemode.Assault) {
            lines.addStaticLine(Component.text("Lives", NamedTextColor.WHITE).decorate(TextDecoration.BOLD));
            for (Team team : MapController.getCurrentMap().teams) {
                if (team.getLives() > 1) {
                    lines.addDynamicLine(() -> team.getDisplayName()
                            .append(Component.text(": "))
                            .append(Component.text(team.getLives())));
                }
            }
            lines.addBlankLine();
        }

        // Flip the order of flags, so they're in a more logical order
        Flag[] reversedFlags = MapController.getCurrentMap().flags.clone();
        ArrayUtils.reverse(reversedFlags);
        // Display the flag scoreboard
        for (Flag flag : reversedFlags) {
            if (flag.isActive()) {
                lines.addDynamicLine(() -> {
                    Team owners = MapController.getCurrentMap().getTeam(flag.getCurrentOwners());
                    return Component.text(flag.name,
                            owners == null ? NamedTextColor.GRAY : owners.primaryChatColor);
                });
            }
        }


        this.componentSidebar = new ComponentSidebarLayout(title, lines.build());
    }

    /**
     * Calls the next tick of the animation
     */
    public void tick() {
        // Advance title animation to the next frame
        titleAnimation.nextFrame();

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
