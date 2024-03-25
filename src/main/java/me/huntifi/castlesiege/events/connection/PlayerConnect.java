package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.conwymc.commands.staff.chat.BroadcastCommand;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;
import java.util.UUID;

/**
 * Handles what happens when someone logs in
 */
public class PlayerConnect implements Listener {


    /**
     * Assign the player's data and join a team
     * @param e The event called when a player join the game
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        CSPlayerData data = CSActiveData.getData(uuid);

        // Ensure the player's data was loaded correctly
        if (data == null) {
            p.kick(Component.text("Something went wrong loading your data!", NamedTextColor.DARK_RED)
                    .append(Component.newline()).append(Component.text("Please try joining again or contact staff if this issue persists.")));
            return;
        }

        // Assign the player to a team or spectator
        InCombat.playerDied(uuid);
        if (MapController.isMatch) {
            SpectateCommand.spectators.add(uuid);
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
        } else {
            MapController.joinATeam(p.getUniqueId());

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
        StoreData.updateName(uuid, "player_stats");

        //Welcomes new players!
        if (!p.hasPlayedBefore()) {

            Messenger.broadcast(BroadcastCommand.broadcastPrefix
                    .append(Messenger.mm.deserialize("<gradient:#663dff:#cc4499:#663dff><st>━━━━━</st> " +
                            "Welcome <color:#cc4499>" + p.getName() + "</color> to Castle Siege! <st>━━━━━</st>")));
            Messenger.send(Component.text("If you encounter a problem or need help, contact us on " +
                            "<yellow><click:suggest_command:/discord>/discord</click></yellow> or " +
                            "<yellow><click:open_url:https://conwymc.alchemix.dev/contact>email us</click></yellow>!",
                    NamedTextColor.GREEN), p);

        } else {
            Messenger.send(Component.text("Hello ", NamedTextColor.DARK_RED)
                    .append(Component.text(p.getName()))
                    .append(Component.newline())
                    .append(Component.text("Welcome to Castle Siege", NamedTextColor.DARK_RED))
                    .append(Component.newline())
                    .append(Component.text("There are currently " + Bukkit.getOnlinePlayers().size() +
                            " player(s) online.", NamedTextColor.DARK_PURPLE)), p);
        }

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
}
