package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.commands.staff.RankPoints;
import me.huntifi.castlesiege.commands.staff.punishments.PunishmentTime;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.*;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        // Assign the player to a team
        MapController.joinATeam(e.getPlayer().getUniqueId());

        // Assign stored kit
        InCombat.playerDied(uuid);
        p.performCommand(data.getKit());
        p.setExp(0);

        // Update the names stored in the database
        StoreData.updateName(uuid, "player_stats");
        StoreData.updateName(uuid, "player_rank");

        //Welcomes new players!
        if (!p.hasPlayedBefore()) {

            String broadcastPrefix = "§2[§4ConwyMC§2] ";
            Bukkit.broadcastMessage(broadcastPrefix + ChatColor.DARK_PURPLE + " ----- " + ChatColor.LIGHT_PURPLE + "Welcome " + p.getName()
                    + " to Castle Siege!" + ChatColor.DARK_PURPLE + " ----- ");
            p.sendMessage(ChatColor.DARK_PURPLE + "There are currently " + Bukkit.getOnlinePlayers().size() + " player(s) online.");
            p.sendMessage(ChatColor.DARK_PURPLE + "The max amount of players is 100.");

        } else {
            p.sendMessage(ChatColor.DARK_RED + "Hello " + ChatColor.GREEN + p.getName());
            p.sendMessage(ChatColor.DARK_RED + "Welcome to Castle Siege!");
            p.sendMessage(ChatColor.DARK_PURPLE + "There are currently " + Bukkit.getOnlinePlayers().size() + " player(s) online.");
            p.sendMessage(ChatColor.DARK_PURPLE + "The max amount of players is 100.");
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

        // The player is allowed to join, so we can start loading their data
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
        PlayerData data = LoadData.load(uuid);
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
}
