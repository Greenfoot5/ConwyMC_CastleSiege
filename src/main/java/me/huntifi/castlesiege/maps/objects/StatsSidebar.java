package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Scoreboard;
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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.huntifi.castlesiege.commands.info.leaderboard.Leaderboard.gradient;

public class StatsSidebar {
    private final Sidebar sidebar;
    private final ComponentSidebarLayout componentSidebar;
    private final SidebarAnimation<Component> titleAnimation;

    public StatsSidebar(@NotNull Sidebar sidebar, @NotNull UUID uuid) {
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
                // 30 dashes to define minimum width
                .addStaticLine(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY).decorate(TextDecoration.STRIKETHROUGH));


        // Display the stats scoreboard
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");
        PlayerData totalData = ActiveData.getData(uuid);
        PlayerData mapData = MVPStats.getStats(uuid);

//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<yellow>Level</yellow> ")
//                                .append(Component.text(totalData.getLevel(), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0>Score</transition> ")
//                        .append(Component.text(num.format(mapData.getScore()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.15>Kills</transition> ")
//                        .append(Component.text(num.format(mapData.getKills()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.4>Deaths</transition> ")
//                        .append(Component.text(num.format(mapData.getDeaths()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.6>KDR</transition> ")
//                        .append(Component.text(dec.format(mapData.getKills() / mapData.getDeaths()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.7>Assists</transition> ")
//                        .append(Component.text(num.format(mapData.getAssists()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.8>Captures</transition> ")
//                        .append(Component.text(num.format(mapData.getCaptures()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.9>Heals</transition> ")
//                        .append(Component.text(num.format(mapData.getHeals()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":1>Supports</transition> ")
//                        .append(Component.text(num.format(mapData.getSupports()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<red>Kill Streak</red> ")
//                        .append(Component.text(num.format(mapData.getKillStreak()), NamedTextColor.WHITE)));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<gold>Coins</gold> ")
//                        .append(Component.text(num.format(totalData.getCoins()))));

        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<yellow>" + totalData.getLevel() + "</yellow> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Level</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0>" + num.format(mapData.getScore()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Score</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.15>" + num.format(mapData.getKills()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Kills</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.4>" + num.format(mapData.getDeaths()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Deaths</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.6>" + dec.format(mapData.getKills() / mapData.getDeaths()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>KDR</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.7>" + num.format(mapData.getAssists()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Assists</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.8>" + num.format(mapData.getCaptures()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Captures</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.9>" + num.format(mapData.getHeals()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Heals</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":1>" + num.format(mapData.getSupports()) + "</transition> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Supports</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<red>" + num.format(mapData.getKillStreak()) + "</red> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Kill Streak</font>")));
        lines.addDynamicLine(() ->
                MiniMessage.miniMessage().deserialize("<gold>" + num.format(totalData.getCoins()) + "</gold> ")
                        .append(MiniMessage.miniMessage().deserialize("<font:uniform>Coins</font>")));

//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<white>" + totalData.getLevel() + "</white> ")
//                        .append(MiniMessage.miniMessage().deserialize("<yellow><font:uniform>Level</font></yellow>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<color:#cccccc>" + num.format(mapData.getScore()) + "</color> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0><font:uniform>Score</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<white>" + num.format(mapData.getKills()) + "</white> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.15><font:uniform>Kills</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<color:#cccccc>" + num.format(mapData.getDeaths()) + "</color> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.4><font:uniform>Deaths</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<white>" + dec.format(mapData.getKills() / mapData.getDeaths()) + "</white> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.6><font:uniform>KDR</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<color:#cccccc>" + num.format(mapData.getAssists()) + "</color> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.7><font:uniform>Assists</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<white>" + num.format(mapData.getCaptures()) + "</white> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.8><font:uniform>Captures</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<color:#cccccc>" + num.format(mapData.getHeals()) + "</color> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":0.9><font:uniform>Heals</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<white>" + num.format(mapData.getSupports()) + "</white> ")
//                        .append(MiniMessage.miniMessage().deserialize("<transition:" + gradient + ":1><font:uniform>Supports</font></transition>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<color:#cccccc>" + num.format(mapData.getKillStreak()) + "</color> ")
//                        .append(MiniMessage.miniMessage().deserialize("<red><font:uniform>Kill Streak</font></red>")));
//        lines.addDynamicLine(() ->
//                MiniMessage.miniMessage().deserialize("<white>" + num.format(totalData.getCoins()) + "</white> ")
//                        .append(MiniMessage.miniMessage().deserialize("<gold><font:uniform>Coins</font></gold>")));

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

    public void addPlayer(Player player) {
        sidebar.addPlayer(player);
    }

    public void close() {
        sidebar.close();
    }
}
