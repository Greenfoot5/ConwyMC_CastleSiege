package me.greenfoot5.castlesiege.events.connection;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.LoadData;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.database.StoreData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.data_types.Cosmetic;
import me.greenfoot5.conwymc.data_types.PlayerCosmetics;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;

import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.TITLE;

/**
 * Handles what happens when someone logs in
 */
public class PlayerConnect implements Listener {

    /**
     * Performs the same function as a login, without the login stuff
     * @param player Player to add to Castle Siege
     */
    public static void onPlayerReload(Player player) {
        UUID uuid = player.getUniqueId();

        loadData(uuid);
        CSPlayerData data = CSActiveData.getData(uuid);
        checkCosmetics(data, uuid);

        Messenger.send(Component.text("Hello ", NamedTextColor.DARK_RED)
                .append(Component.text(player.getName()))
                .append(Component.newline())
                .append(Component.text("Welcome to Castle Siege", NamedTextColor.DARK_RED))
                .append(Component.newline())
                .append(Component.text("There are currently " + Bukkit.getOnlinePlayers().size() +
                        " player(s) online.", NamedTextColor.DARK_PURPLE)), player);

        // Assign the player to a team or spectator
        InCombat.playerDied(uuid);
        if (MapController.isMatch) {
            TeamController.joinSpectator(player);
            player.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
        } else {
            TeamController.joinSmallestTeam(uuid, MapController.getCurrentMap());

            // Assign stored kit
            Kit kit = Kit.getKit(data.getKit());
            if (kit != null && kit.canSelect(player, true, true, false))
                kit.addPlayer(uuid, true);
            else
                Kit.getKit("Swordsman").addPlayer(uuid, true);
        }

        // Reset player xp and level
        player.setExp(0);
        player.setLevel(data.getLevel());

        if (CoinKit.isFree()) {
            Messenger.broadcastInfo("It's Friday! All coin and team kits are <b>UNLOCKED!</b>");
        }

        if (data.getSetting("alwaysInfo").equals("false") || data.getLevel() <= 5) {
            sendTitlebarMessages(player);
        }
    }

    /**
     * Assign the player's data and join a team
     * @param e The event called when a player join the game
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!Main.instance.hasLoaded) {
            p.kick(Component.text("Sorry, the server's not quite finished loading!")
                    .append(Component.newline())
                    .append(Component.text("Please try again in a minute.")));
        }

        UUID uuid = p.getUniqueId();
        CSPlayerData data = CSActiveData.getData(uuid);

        // Ensure the player's data was loaded correctly
        if (data == null) {
            p.kick(Component.text("Something went wrong loading your data!", NamedTextColor.DARK_RED)
                    .append(Component.newline()).append(Component.text("Please try joining again or contact staff if this issue persists.")));
            return;
        }

        checkCosmetics(data, uuid);

        //Welcomes new players!
        if (!p.hasPlayedBefore()) {

            Messenger.broadcast(Messenger.mm.deserialize("<gradient:#663dff:#cc4499:#663dff><st>━━━━━</st> " +
                            "Welcome <color:#cc4499>" + p.getName() + "</color> to Castle Siege! <st>━━━━━</st>"));
            Messenger.send(Messenger.mm.deserialize("<green>If you encounter a problem or need help, contact us on " +
                            "<yellow><click:suggest_command:/discord>/discord</click></yellow> or " +
                            "<yellow><click:open_url:https://conwymc.alchemix.dev/contact>email us</click></yellow>!</green>"), p);

        } else {
            Messenger.send(Component.text("Hello ", NamedTextColor.DARK_RED)
                    .append(Component.text(p.getName()))
                    .append(Component.newline())
                    .append(Component.text("Welcome to Castle Siege", NamedTextColor.DARK_RED))
                    .append(Component.newline())
                    .append(Component.text("There are currently " + TeamController.getPlayers().size() +
                            " player(s) battling.", NamedTextColor.DARK_PURPLE)), p);
        }

        // Assign the player to a team or spectator
        InCombat.playerDied(uuid);
        if (MapController.isMatch) {
            TeamController.joinSpectator(e.getPlayer());
            p.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
        } else {
            TeamController.joinSmallestTeam(p.getUniqueId(), MapController.getCurrentMap());

            // Assign stored kit
            Kit kit = Kit.getKit(data.getKit());
            if (kit != null && kit.canSelect(p, true, true, false))
                kit.addPlayer(uuid, true);
            else
                Kit.getKit("Swordsman").addPlayer(uuid, true);
        }

        // Reset player xp and level
        p.setExp(0);
        p.setLevel(CSActiveData.getData(p.getUniqueId()).getLevel());

        // Update the names stored in the database
        StoreData.updateName(uuid);

        if (CoinKit.isFree()) {
            Messenger.broadcastInfo("It's Friday! All coin and team kits are <b>UNLOCKED!</b>");
        }

        if (data.getSetting("alwaysInfo").equals("false") || data.getLevel() <= 5) {
            sendTitlebarMessages(p);
        }
    }

    /**
     * Check if the player is allowed to join the game
     * Load the player's data
     * @param e The event called when a player attempts to join the server
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preLogin(AsyncPlayerPreLoginEvent e) {
        // The player is allowed to join, so we can start loading their data if we haven't already
        loadData(e.getUniqueId());
    }

    /**
     * Load the player's data
     * Actively store the loaded data
     * @param uuid The unique ID of the player
     */
    private void loadData(UUID uuid) {
        // Load the player's data
        CSPlayerData data = CSActiveData.hasPlayer(uuid) ? CSActiveData.getData(uuid) : LoadData.load(uuid);
        assert data != null;

        // Actively store data
        CSActiveData.addPlayer(uuid, data);
        MVPStats.addPlayer(uuid);
    }

    /**
     * send a title bar to the player after 5 seconds, then another time after 30 seconds.
     * But only if they are in the spawn room still.
     * @param p The player
     */
    public static void sendTitlebarMessages(Player p) {
        Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ofMillis(750));
        Title title = Title.title(Component.text("Click a sign on the woolmap to join the fight!"), Component.text(""), times);
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (InCombat.isPlayerInLobby(p.getUniqueId())) {
                p.showTitle(title);
            }
        }, 100);

        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (InCombat.isPlayerInLobby(p.getUniqueId())) {
                p.showTitle(title);
            }
        }, 600);

    }

    public static void checkCosmetics(PlayerData data, UUID uuid) {
        try {
            Cosmetic reaper = new Cosmetic(TITLE, "Reaper", "<color:#3B3B3B>☠<b><gradient:#592E31:#92191E>Reaper</b>☠");
            Tuple<PreparedStatement, ResultSet> leaderboard = LoadData.getTop("kills", 0);
            PlayerCosmetics.isTop(data, uuid, leaderboard.getSecond(), 3, reaper);
            leaderboard.getFirst().close();
        } catch (SQLException e) {
            ConwyMC.plugin.getLogger().severe("Error in checkTopCosmetics for " + uuid);
            ConwyMC.plugin.getLogger().severe(e.getMessage());
        }

        try {
            Cosmetic foolish = new Cosmetic(TITLE, "Foolish", "<gradient:#7F7FD5:#86A8E7:#91EAE4><font:illageralt>7..cL2k</font>");
            Tuple<PreparedStatement, ResultSet> leaderboard = LoadData.getTop("deaths", 0);
            PlayerCosmetics.isTop(data, uuid, leaderboard.getSecond(), 3, foolish);
            leaderboard.getFirst().close();
        } catch (SQLException e) {
            ConwyMC.plugin.getLogger().severe("Error in checkTopCosmetics for " + uuid);
            ConwyMC.plugin.getLogger().severe(e.getMessage());
        }
    }
}
