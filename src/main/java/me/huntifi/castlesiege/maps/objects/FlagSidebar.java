package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.maps.CoreMap;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Scoreboard;
import me.huntifi.castlesiege.maps.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FlagSidebar {
    private final Sidebar sidebar;
    private final ComponentSidebarLayout componentSidebar;
    private final SidebarAnimation<Component> titleAnimation;

    public FlagSidebar(@NotNull Sidebar sidebar) {
        this.sidebar = sidebar;

        this.titleAnimation = createGradientAnimation(Component.text("Mode: ")
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
                .addStaticLine(Component.text("-", NamedTextColor.DARK_GRAY));


        // Core Map
        if (MapController.getCurrentMap() instanceof CoreMap) {
            CoreMap coreMap = (CoreMap) MapController.getCurrentMap();
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

    // Called every tick
    public void tick() {
        // Advance title animation to the next frame
        titleAnimation.nextFrame();

        // Update sidebar title & lines
        componentSidebar.apply(sidebar);
    }

    private @NotNull SidebarAnimation<Component> createGradientAnimation(@NotNull Component text) {
        float step = 1f / 16f;

        TagResolver.Single textPlaceholder = Placeholder.component("text", text);
        List<Component> frames = new ArrayList<>((int) (2f / step));

        float phase = -1f;
        while (phase < 1) {
            frames.add(MiniMessage.miniMessage().deserialize("<gradient:#e9455e:#2e3468:" + phase + "><text>", textPlaceholder));
            phase += step;
        }

        return new CollectionSidebarAnimation<>(frames);
    }
}
