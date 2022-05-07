package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.*;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
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

        // Assign the player's staff and donator permissions
        Permissions.addPlayer(uuid);
        Permissions.setStaffPermission(uuid, data.getStaffRank());
        Permissions.setDonatorPermission(uuid, data.getRank());

        // Assign the player to a team
        MapController.joinATeam(e.getPlayer().getUniqueId());

        // Assign stored kit
        InCombat.playerDied(uuid);
        p.performCommand(data.getKit());

        // Update the names stored in the database
        StoreData.updateName(uuid, "player_stats");
        StoreData.updateName(uuid, "player_rank");
    }

    /**
     * Check if the player is allowed to join the game
     * Load the player's data
     * @param e The event called when a player attempts to join the server
     * @throws SQLException If something goes wrong executing a query
     */
    // TODO - Load other punishment data and actively store them?
    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent e) throws SQLException {
        Tuple<Boolean, String> banned = isBanned(e.getUniqueId(), e.getAddress());
        if (banned.getFirst()) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, banned.getSecond());
            return;
        }

        // The player is allowed to join, so we can start loading their data
        loadData(e.getUniqueId());
    }

    /**
     * Check if the player is banned
     * @param uuid The unique ID of the player
     * @param ip The IP-address of the player
     * @return Whether the player is banned, and the reason for that ban
     * @throws SQLException If something goes wrong executing the query
     */
    private Tuple<Boolean, String> isBanned(UUID uuid, InetAddress ip) throws SQLException {
        // Check all ban records for this uuid to see if one is still active
        Tuple<PreparedStatement, ResultSet> prUUID = Punishments.get(uuid, "ban");
        Tuple<Boolean, String> uuidBan = checkBan(prUUID.getSecond());
        prUUID.getFirst().close();
        if (uuidBan.getFirst()) {
            return uuidBan;
        }

        // Check all ban records for this IP to see if one is still active
        Tuple<PreparedStatement, ResultSet> prIP = Punishments.getIPBan(ip);
        Tuple<Boolean, String> ipBan = checkBan(prIP.getSecond());
        prIP.getFirst().close();
        if (ipBan.getFirst()) {
            return ipBan;
        }

        // No active ban record was found
        return new Tuple<>(false, "");
    }

    /**
     * Check if the query result contains an active ban
     * @param rs The result of a query
     * @return Whether an active ban is present, and the reason for that ban
     * @throws SQLException If something goes wrong getting data from the query
     */
    private Tuple<Boolean, String> checkBan(ResultSet rs) throws SQLException {
        while (rs.next()) {
            if (rs.getTimestamp("end").after(new Timestamp(System.currentTimeMillis()))) {
                return new Tuple<>(true, rs.getString("reason"));
            }
        }
        return new Tuple<>(false, "");
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
    }
}
