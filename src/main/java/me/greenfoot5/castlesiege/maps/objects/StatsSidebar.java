package me.greenfoot5.castlesiege.maps.objects;

import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.data_types.CSStats;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Scoreboard;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.UUID;

import static me.greenfoot5.castlesiege.commands.info.leaderboard.LeaderboardCommand.gradient;

/**
 * A stats scoreboard for the current game
 */
public class StatsSidebar {
    private final Sidebar sidebar;
    private final ComponentSidebarLayout componentSidebar;
    private final SidebarAnimation<Component> titleAnimation;

    /**
     * @param sidebar The sidebar to use the stats for
     * @param uuid The UUID of the player's stats to display
     */
    public StatsSidebar(@NotNull Sidebar sidebar, @NotNull UUID uuid) {
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


        // Display the stats scoreboard
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");
        CSPlayerData totalData = CSActiveData.getData(uuid);
        CSStats mapData = MVPStats.getStats(uuid);

        if (totalData.getSetting("scoreboard").equals("stats_icons")) {
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<yellow>" + totalData.getLevel() + " ⭐</yellow> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0>" + num.format(mapData.getScore()) + " \uD83D\uDCAE</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.15>" + num.format(mapData.getKills()) + " \uD83D\uDDE1</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.4>" + num.format(mapData.getDeaths()) + " ☠</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.6>" + dec.format(mapData.getKills() / mapData.getDeaths()) + " ⚔</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.7>" + num.format(mapData.getAssists()) + " ➕</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.8>" + num.format(mapData.getCaptures()) + " \uD83C\uDFF4</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.9>" + num.format(mapData.getHeals()) + " ❤</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":1>" + num.format(mapData.getSupports()) + " \uD83D\uDD28</transition> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<red>" + num.format(mapData.getKillStreak()) + " \uD83D\uDD2A</red> "));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<gold>" + num.format(totalData.getCoins()) + " ⛃</gold> "));
        } else {
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<yellow>Level</yellow> ")
                            .append(Component.text(totalData.getLevel(), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0>Score</transition> ")
                            .append(Component.text(num.format(mapData.getScore()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.15>Kills</transition> ")
                            .append(Component.text(num.format(mapData.getKills()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.4>Deaths</transition> ")
                            .append(Component.text(num.format(mapData.getDeaths()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.6>KDR</transition> ")
                            .append(Component.text(dec.format(mapData.getKills() / mapData.getDeaths()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.7>Assists</transition> ")
                            .append(Component.text(num.format(mapData.getAssists()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.8>Captures</transition> ")
                            .append(Component.text(num.format(mapData.getCaptures()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":0.9>Heals</transition> ")
                            .append(Component.text(num.format(mapData.getHeals()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<transition:" + gradient + ":1>Supports</transition> ")
                            .append(Component.text(num.format(mapData.getSupports()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<red>Kill Streak</red> ")
                            .append(Component.text(num.format(mapData.getKillStreak()), NamedTextColor.WHITE)));
            lines.addDynamicLine(() ->
                    Messenger.mm.deserialize("<gold>Coins</gold> ")
                            .append(Component.text(num.format(totalData.getCoins()))));
        }

        this.componentSidebar = new ComponentSidebarLayout(title, lines.build());
    }

    /**
     * Moves the animation on a tick
     */
    public void tick() {
        // Advance title animation to the next frame
        titleAnimation.nextFrame();

        // Update sidebar title & lines
        componentSidebar.apply(sidebar);
    }

    /**
     * @param player The player to display the sidebar for
     */
    public void addPlayer(Player player) {
        sidebar.addPlayer(player);
    }

    /**
     * Closes the sidebar
     */
    public void close() {
        sidebar.close();
    }
}
