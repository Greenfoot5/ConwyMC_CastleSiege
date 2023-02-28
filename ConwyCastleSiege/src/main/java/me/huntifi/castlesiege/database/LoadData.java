package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.*;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Load a player's data when they join the game
 * These methods should only be called asynchronously
 */
public class LoadData {

    /**
     * Load a player's stats and rank data
     * @param uuid The unique ID of the player
     * @return The PlayeaData of the uuid
     */
    public static PlayerData load(UUID uuid) {
        try {

            // Unlock achievements data
            ArrayList<String> unlockedAchievements = getUnlockedAchievements(uuid);

            // Unlock kits data
            ArrayList<String> unlockedKits = getUnlockedKits(uuid);

            ArrayList<String> foundSecrets = getFoundSecrets(uuid);

            // Mute data
            Tuple<PreparedStatement, ResultSet> prMute = Punishments.getActive(uuid, "mute");

            // Stats data
            createEntry(uuid, "player_stats");
            Tuple<PreparedStatement, ResultSet> prStats = getData(uuid, "player_stats");

            // Rank data
            createEntry(uuid, "player_rank");
            Tuple<PreparedStatement, ResultSet> prRank = getData(uuid, "player_rank");

            // Votes data
            createEntry(uuid, "VotingPlugin_Users");
            HashMap<String, Long> votes = getVotes(uuid);

            // Settings data
            HashMap<String, String> settings = getSettings(uuid);

            // Boosters
            ArrayList<Booster> boosters = getBoosters(uuid);

            // Collect data and release resources
            PlayerData data = new PlayerData(unlockedAchievements, unlockedKits, foundSecrets, prMute.getSecond(),
                    prStats.getSecond(), prRank.getSecond(), votes, settings, MapController.isMatch, boosters);
            prMute.getFirst().close();
            prStats.getFirst().close();
            prRank.getFirst().close();

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all currently unlocked premium and team kits from the database
     * @param uuid The unique id of the player whose data to get
     * @return A list of all currently unlocked kits
     */
    private static ArrayList<String> getUnlockedKits(UUID uuid) {
        ArrayList<String> unlockedKits = new ArrayList<>();

        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT unlocked_kits FROM player_unlocks WHERE uuid = ? AND unlocked_until > ?")) {
            ps.setString(1, uuid.toString());
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String kit = rs.getString("unlocked_kits");
                if (DonatorKit.getKits().contains(kit)) {
                    unlockedKits.add(kit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return unlockedKits;
    }

    /**
     * Get a user's data from a table in the database
     * @param uuid The unique id of the player whose data to get
     * @param table The table to get the data from
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    private static Tuple<PreparedStatement, ResultSet> getData(UUID uuid, String table) throws SQLException {
        // Get player stats from the database
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM " + table + " WHERE uuid=?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Return result set with pointer on first (and only) row
        rs.next();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get the vote data from the database
     * @param uuid The unique id of the player whose data to get
     * @return The votes string saved in the database
     * @throws SQLException If something goes wrong executing the query
     */
    private static HashMap<String, Long> getVotes(UUID uuid) throws SQLException {
        // Get votes from the database
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT LastVotes FROM VotingPlugin_Users WHERE uuid=?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Get votes from the query result
        HashMap<String, Long> votes = new HashMap<>();
        if (rs.next() && rs.getString(1) != null) {
            String[] voteArray = rs.getString(1).split("%line%");
            for (String vote : voteArray) {
                if (Long.parseLong(vote.split("//")[1]) > System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                    votes.put(vote.split("//")[0], Long.parseLong(vote.split("//")[1]));
                }
            }
        }

        ps.close();
        return votes;
    }

    /**
     * Create an entry in the specified table, does nothing if entry already exists
     * @param uuid The player for whom to create an entry
     * @param table The table to create an entry in
     * @throws SQLException If something goes wrong executing the insert
     */
    private static void createEntry(UUID uuid, String table) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "INSERT IGNORE INTO " + table + " (uuid) VALUES (?)");
        ps.setString(1, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Get the top players from the database
     * @param order The category to order by
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getTop(String order, int offset) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM vw_toplist ORDER BY " + order + " DESC LIMIT 10 OFFSET ?");
        ps.setInt(1, offset);

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get the UUID of a player from our database
     * @param name The name of the player
     * @return The player's UUID, or null if the name is not in the database
     * @throws SQLException If something goes wrong executing the query
     */
    public static UUID getUUID(String name) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT uuid FROM player_rank WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        UUID uuid;
        if (rs.next()) {
            uuid = UUID.fromString(rs.getString(1));
        } else {
            uuid = null;
        }

        ps.close();
        return uuid;
    }

    /**
     * Get the rank points of a player from our database
     * @param name The name of the player
     * @return The player's rank points, or -1 if the name is not in the database
     * @throws SQLException If something goes wrong executing the query
     */
    public static double getRankPoints(String name) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT rank_points FROM player_rank WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        double rankPoints;
        if (rs.next()) {
            rankPoints = rs.getDouble(1);
        } else {
            rankPoints = -1;
        }

        ps.close();
        return rankPoints;
    }

    /**
     * Get the top 10 donators
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getTopDonators() throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM player_rank ORDER BY rank_points DESC LIMIT 10");

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get 10 donators from the database
     * @param offset The amount of donators to skip
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getDonators(int offset) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM vw_donator LIMIT 10 OFFSET ?");
        ps.setInt(1, offset);

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get a players kit from the database
     * @param uuid The unique ID of the player whose data to get
     * @param kitName The type of kit to get from the database
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getActiveKit(UUID uuid, String kitName) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT unlocked_kits, unlocked_until FROM player_unlocks WHERE uuid = ? AND unlocked_kits = ? AND unlocked_until > ?"
                        + " ORDER BY unlocked_until DESC LIMIT 1");
        ps.setString(1, uuid.toString());
        ps.setString(2, kitName);
        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Check if the query result contains an active kit
     * @param rs The result of a query
     * @return The timestamp of the kit, null if no active kit was found
     * @throws SQLException If something goes wrong getting data from the query
     */
    public static Timestamp getKitTimestamp(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getTimestamp("unlocked_until");
        }
        return new Timestamp(0);
    }

    public static HashMap<String, String> getSettings(UUID uuid) {
        HashMap<String, String> loadedSettings = new HashMap<>();

        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT setting, value FROM player_settings WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                loadedSettings.put(rs.getString("setting"), rs.getString("value"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return loadedSettings;
    }

    /**
     *
     * @param uuid uuid of the player
     * @return this returns an array representation of all the secrets this player has found so far.
     * You may use the size of this arraylist as a representation of how many secrets this player has found, with on top of that
     * the names of those secrets. If ever needed.
     */
    public static ArrayList<String> getFoundSecrets(UUID uuid) {
        ArrayList<String> foundSecrets = new ArrayList<>();

        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT secret FROM player_secrets WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                foundSecrets.add(rs.getString("secret"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return foundSecrets;
    }

    /**
     * Get all currently unlocked achievements from the database
     * @param uuid The unique id of the player whose data to get
     * @return A list of all currently unlocked achievements
     */
    private static ArrayList<String> getUnlockedAchievements(UUID uuid) {
        ArrayList<String> unlockedAchievements = new ArrayList<>();

        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT achievement FROM player_achievements WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String gadget = rs.getString("achievement");
                unlockedAchievements.add(gadget);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return unlockedAchievements;
    }

    private static ArrayList<Booster> getBoosters(UUID uuid) {
        ArrayList<Booster> boosters = new ArrayList<>();

        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT booster_id, booster_type, duration, boost_value FROM player_boosters WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int boostId = rs.getInt("booster_id");
                String type = rs.getString("booster_type");
                int duration = rs.getInt("duration");
                String other = rs.getString("boost_value");
                Booster booster;
                Booster.updateID(boostId);
                double multiplier;
                switch (type.toUpperCase()) {
                    case "COIN":
                    case "COINS":
                    case "C":
                        try {
                            multiplier = Double.parseDouble(other);
                            booster = new CoinBooster(duration, multiplier);
                            booster.id = boostId;
                            boosters.add(booster);
                        } catch (NumberFormatException ignored) {
                            Main.instance.getLogger().warning("Booster id: " + boostId + " has a malformed double multiplier!");
                        }
                        break;
                    case "BATTLEPOINT":
                    case "BP":
                        if (other == null) {
                            booster = new BattlepointBooster(duration);
                            booster.id = boostId;
                            boosters.add(booster);
                            break;
                        }
                        try {
                            multiplier = Double.parseDouble(other);
                            booster = new BattlepointBooster(duration, multiplier);
                            booster.id = boostId;
                            boosters.add(booster);
                        } catch (NumberFormatException ignored) {
                            Main.instance.getLogger().warning("Booster id: " + boostId + " has a malformed double multiplier!");
                        }
                    case "KIT":
                    case "K":
                        if (Kit.getKit(other) == null
                                && !other.equalsIgnoreCase("WILD")
                                && !other.equalsIgnoreCase("RANDOM")) {
                            break;
                        }
                        booster = new KitBooster(duration, other);
                        booster.id = boostId;
                        boosters.add(booster);
                        break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return boosters;
    }
}
