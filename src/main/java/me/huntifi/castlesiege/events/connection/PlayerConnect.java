package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.donations.RankPoints;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.commands.staff.punishments.PunishmentTime;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.database.Permissions;
import me.huntifi.castlesiege.database.Punishments;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        PlayerData data = ActiveData.getData(uuid);

        // Ensure the player's data was loaded correctly
        if (data == null) {
            p.kickPlayer(ChatColor.DARK_RED + "Something went wrong loading your data!\n"
                    + "Please try joining again or contact staff if this issue persists.");
            return;
        }

        // Set the join message
        if (!data.getJoinMessage().isEmpty()) {
            e.setJoinMessage(ChatColor.YELLOW + data.getJoinMessage());
        }

        // Assign the player's staff and donator permissions
        Permissions.addPlayer(uuid);
        Permissions.setStaffPermission(uuid, data.getStaffRank());
        Permissions.setDonatorPermission(uuid, data.getRank());

        // Assign the player to a team or spectator
        InCombat.playerDied(uuid);
        if (MapController.isMatch) {
            SpectateCommand.spectators.add(uuid);
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
            NameTag.give(p);
        } else {
            MapController.joinATeam(p.getUniqueId());

            // Assign stored kit
            Kit kit = Kit.getKit(data.getKit());
            if (kit != null && kit.canSelect(p, true, false))
                kit.addPlayer(uuid);
            else
                Kit.getKit("Swordsman").addPlayer(uuid);
        }

        // Reset player xp and level
        p.setExp(0);
        p.setLevel(ActiveData.getData(p.getUniqueId()).getLevel());

        // Update the names stored in the database
        StoreData.updateName(uuid, "player_stats");
        StoreData.updateName(uuid, "player_rank");

        //Welcomes new players!
        if (!p.hasPlayedBefore()) {

            String broadcastPrefix = "§2[§4ConwyMC§2] ";
            Bukkit.broadcastMessage(broadcastPrefix + ChatColor.DARK_PURPLE + " ----- " + ChatColor.LIGHT_PURPLE + "Welcome " + p.getName()
                    + " to Castle Siege!" + ChatColor.DARK_PURPLE + " ----- ");
            p.sendMessage(ChatColor.GREEN + "If you encounter a problem or need help, contact us on Discord and create a support ticket");

        } else {
            p.sendMessage(ChatColor.DARK_RED + "Hello " + ChatColor.GREEN + p.getName());
            p.sendMessage(ChatColor.DARK_RED + "Welcome to Castle Siege!");
            p.sendMessage(ChatColor.DARK_PURPLE + "There are currently " + Bukkit.getOnlinePlayers().size() + " player(s) online.");
            p.sendMessage(ChatColor.DARK_PURPLE + "The max amount of players is 100.");
        }

        if (CoinKit.isFree()) {
            Messenger.broadcastInfo("It's Friday! All donator and team kits are " + ChatColor.BOLD + "UNLOCKED!");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ActiveData.getData(player.getUniqueId()).getSetting("joinPing").equals("true")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);
            }
        }
        if (ActiveData.getData(p.getUniqueId()).getSetting("woolmapTitleMessage").equals("true")) {
            sendTitlebarMessages(p);
        }
    }

    /**
     * Check if the player is allowed to join the game
     * Load the player's data
     * @param e The event called when a player attempts to join the server
     * @throws SQLException If something goes wrong executing a query
     */
    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent e) throws SQLException {
        Tuple<String, Timestamp> banned = getBan(e.getUniqueId(), e.getAddress());
        if (banned != null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                    ChatColor.DARK_RED + "\n[BAN] " + ChatColor.RED + banned.getFirst()
                            + ChatColor.DARK_RED + "\n[EXPIRES IN] " + ChatColor.RED + PunishmentTime.getExpire(banned.getSecond()));
            return;
        }

        // The player is allowed to join, so we can start loading their data if we haven't already
        loadData(e.getUniqueId());
    }

    /**
     * Get the player's active ban
     * @param uuid The unique ID of the player
     * @param ip The IP-address of the player
     * @return The reason and end of an active ban, null if no active ban was found
     * @throws SQLException If something goes wrong executing the query
     */
    private Tuple<String, Timestamp> getBan(UUID uuid, InetAddress ip) throws SQLException {
        // Check all ban records for this uuid to see if one is still active
        Tuple<PreparedStatement, ResultSet> prUUID = Punishments.getActive(uuid, "ban");
        Tuple<String, Timestamp> uuidBan = checkBan(prUUID.getSecond());
        prUUID.getFirst().close();
        if (uuidBan != null) {
            return uuidBan;
        }

        // Check all ban records for this IP to see if one is still active
        Tuple<PreparedStatement, ResultSet> prIP = Punishments.getIPBan(ip);
        Tuple<String, Timestamp> ipBan = checkBan(prIP.getSecond());
        prIP.getFirst().close();
        return ipBan;
    }

    /**
     * Check if the query result contains an active ban
     * @param rs The result of a query
     * @return The reason and end of an active ban, null if no active ban was found
     * @throws SQLException If something goes wrong getting data from the query
     */
    private Tuple<String, Timestamp> checkBan(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Tuple<>(rs.getString("reason"), rs.getTimestamp("end"));
        }
        return null;
    }

    /**
     * Load the player's data
     * Actively store the loaded data
     * @param uuid The unique ID of the player
     */
    private void loadData(UUID uuid) {
        // Load the player's data
        PlayerData data = ActiveData.hasPlayer(uuid) ? ActiveData.getData(uuid) : LoadData.load(uuid);
        assert data != null;

        // Actively store data
        ActiveData.addPlayer(uuid, data);
        MVPStats.addPlayer(uuid);

        // Set the player's donator top rank
        data.setRank(RankPoints.getRank(data.getRankPoints()));
        if (data.getRankPoints() > 0) {
            String rank = RankPoints.getTopRank(uuid);
            if (!rank.isEmpty()) {
                data.setRank(rank);
            }
        }
    }

    /**
     * send a title bar to the player after 5 seconds, then another time after 30 seconds.
     * But only if they are in the spawn room still.
     * @param p The player
     */
    public static void sendTitlebarMessages(Player p) {
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (InCombat.isPlayerInLobby(p.getUniqueId())) {
                p.sendTitle("", "Click a sign on the woolmap to join the fight!", 20, 60, 20);
            }
        }, 100);

        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (InCombat.isPlayerInLobby(p.getUniqueId())) {
                p.sendTitle("", "Click a sign on the woolmap to join the fight!", 20, 60, 20);
            }
        }, 600);

    }
}
