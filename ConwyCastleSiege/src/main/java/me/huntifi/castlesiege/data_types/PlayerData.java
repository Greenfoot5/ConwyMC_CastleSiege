package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.gameplay.SettingsCommand;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a player's data
 */
public class PlayerData {

    private ArrayList<String> unlockedKits;

    private ArrayList<String> foundSecrets;

    private Tuple<String, Timestamp> mute;

    private double score;
    private double kills;
    private double deaths;
    private double captures;
    private double heals;
    private double supports;
    private double assists;
    private int killStreak;
    private int maxKillStreak;
    private int mvps;
    private int secrets;
    private String kit;
    private int level;
    private double rankPoints;
    private String staffRank;
    private String rank;
    private String joinMessage;
    private String leaveMessage;
    private HashMap<String, Long> votes;
    private double coins;

    private HashMap<String, String> settings;

    private static double coinMultiplier = 1;

    /**
     * Initialize the player's data for active data
     * @param statsData The data retrieved from player_stats
     * @param rankData The data retrieved from player_rank
     * @throws SQLException If the columns don't match up
     */
    public PlayerData(ArrayList<String> unlockedKits, ArrayList<String> foundSecrets, ResultSet mute, ResultSet statsData,
                      ResultSet rankData, HashMap<String, Long> votes, HashMap<String, String> settings) throws SQLException {
        this.unlockedKits = unlockedKits;
        this.foundSecrets = foundSecrets;
        this.mute = mute.next() ? new Tuple<>(mute.getString("reason"), mute.getTimestamp("end")) : null;

        this.score = statsData.getDouble("score");
        this.kills = statsData.getDouble("kills");
        this.deaths = statsData.getDouble("deaths");
        this.captures = statsData.getDouble("captures");
        this.assists = statsData.getDouble("assists");
        this.heals = statsData.getDouble("heals");
        this.supports = statsData.getDouble("supports");
        this.coins = statsData.getDouble("coins");
        this.level = statsData.getInt("level");
        this.mvps = statsData.getInt("mvps");
        this.secrets = statsData.getInt("secrets");
        this.killStreak = 0;
        this.maxKillStreak = statsData.getInt("kill_streak");
        this.kit = statsData.getString("kit");

        this.staffRank = rankData.getString("staff_rank").toLowerCase();
        this.rankPoints = rankData.getDouble("rank_points");
        this.joinMessage = rankData.getString("join_message");
        this.leaveMessage = rankData.getString("leave_message");

        this.settings = settings;

        this.votes = votes;
    }

    /**
     * Initialize the player's data for MVP stats
     */
    public PlayerData() {
        this.score = 0;
        this.kills = 0;
        this.deaths = 0;
        this.captures = 0;
        this.heals = 0;
        this.supports = 0;
        this.assists = 0;
        this.killStreak = 0;
        this.coins = 0;
    }

    /**
     * Get the player's current mute
     * @return The player's mute reason and expire timestamp, null if not muted
     */
    public Tuple<String, Timestamp> getMute() {
        return mute;
    }

    /**
     * Set the player's mute
     * @param reason The reason for the mute
     * @param end The end of the mute
     */
    public void setMute(String reason, Timestamp end) {
        if (reason == null || end == null)
            mute = null;
        else
            mute = new Tuple<>(reason, end);
    }

    /**
     * Get the player's current score
     * @return The player's score
     */
    public double getScore() {
        return score;
    }

    /**
     * Add to the player's score
     * @param score The score to add
     */
    private void addScore(double score) {
        this.score += score;
    }

    /**
     * Get the player's current kills
     * @return The player's kills
     */
    public double getKills() {
        return kills;
    }

    /**
     * Increase the player's kills by 1
     * Add 1 point and coin per kill
     * Update the current kill streak
     */
    public void addKill() {
        kills += 1;
        addScore(2);
        addCoins(2);
        addKillStreak();
    }

    /**
     * Get the player's current deaths
     * @return The player's deaths
     */
    public double getDeaths() {
        return deaths;
    }

    /**
     * Add to the player's deaths
     * Subtract 1 point per death
     * Reset the current kill streak
     * @param deaths The deaths to add
     */
    public void addDeaths(double deaths) {
        this.deaths += deaths;
        addScore(-deaths);
        killStreak = 0;
    }

    /**
     * Get the player's current captures
     * @return The player's captures
     */
    public double getCaptures() {
        return captures;
    }

    /**
     * Add to the player's captures
     * Add 1 point and coin per capture
     * @param captures The captures to add
     */
    public void addCaptures(double captures) {
        this.captures += captures;
        addScore(captures);
        addCoins(captures);
    }

    /**
     * Get the player's current heals
     * @return The player's heals
     */
    public double getHeals() {
        return heals;
    }

    /**
     * Increase the player's heals.
     * Add 0.5 points and coins per heal.
     * @param heals The amount of heals to add
     */
    public void addHeals(double heals) {
        this.heals += (heals);
        addScore(0.5 * heals);
        addCoins(0.5 * heals);
    }

    /**
     * Get the player's current supports
     * @return The player's supports
     */
    public double getSupports() {
        return supports;
    }

    /**
     * Add to the player's supports
     * Add 1/6 points and coins per support
     * @param supports The supports to add
     */
    public void addSupports(double supports) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.supports += supports;
            addScore(supports / 6);
            addCoins(supports / 6);
        });
    }

    /**
     * Get the player's current assists
     * @return The player's assists
     */
    public double getAssists() {
        return assists;
    }

    /**
     * Increase the player's assists by 1
     * Add 1 point and coin per assist
     */
    public void addAssist() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            assists += 1;
            addScore(1);
            addCoins(1);
        });
    }

    /**
     * Get the player's current kill streak
     * @return The player's kill streak
     */
    public int getKillStreak() {
        return killStreak;
    }

    /**
     * Increase the player's kill streak by 1
     * Update max kill streak when surpassed
     */
    private void addKillStreak() {
        killStreak += 1;
        if (maxKillStreak < killStreak)
            maxKillStreak = killStreak;
    }

    /**
     * Get the player's highest kill streak ever achieved
     * @return The player's highest kill streak
     */
    public int getMaxKillStreak() {
        return this.maxKillStreak;
    }

    /**
     * Get the player's MVP count
     * @return The player's MVP count
     */
    public int getMVPs() {
        return this.mvps;
    }

    /**
     * Increase the player's MVP count by 1
     */
    public void addMVP() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.mvps += 1;
        });
    }

    /**
     * Get the player's amount of found secrets
     * @return The player's secret count
     */
    public int getSecrets() {
        return secrets;
    }

    /**
     * Increase the player's secret count by 1
     */
    public void addSecret() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            secrets += 1;
        });
    }

    /**
     * Get the name of the player's kit
     * @return The player's kit
     */
    public String getKit() {
        return kit;
    }

    /**
     * Set the name of the player's kit
     * @param kit The kit's name
     */
    public void setKit(String kit) {
        this.kit = kit;
    }

    /**
     * Get the player's level
     * @return The player's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increase the player's level by 1
     */
    public void addLevel() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            level += 1;
        });
    }

    /**
     * Get the player's rank points
     * @return The player's rank points
     */
    public double getRankPoints() {
        return rankPoints;
    }

    /**
     * Add to the player's rank points
     * @param rankPoints The rank points to add
     */
    public void setRankPoints(double rankPoints) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.rankPoints = rankPoints;
        });
    }

    /**
     * Get the player's staff rank
     * @return The player's staff rank
     */
    public String getStaffRank() {
        return staffRank;
    }

    /**
     * Set the player's staff rank
     * @param staffRank The rank to set
     */
    public void setStaffRank(String staffRank) {
        this.staffRank = staffRank;
    }

    /**
     * Get the player's rank
     * @return The player's rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * Set the player's rank
     * @param rank The rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * Get the player's custom join message
     * @return The player's custom join message
     */
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Set the player's custom join message
     * @param joinMessage The custom join message
     */
    public void setJoinMessage(String joinMessage) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.joinMessage = joinMessage;
        });
    }

    /**
     * Get the player's custom leave message
     * @return The player's custom leave message
     */
    public String getLeaveMessage() {

        return leaveMessage;
    }

    /**
     * Set the player's custom leave message
     * @param leaveMessage The custom leave message
     */
    public void setLeaveMessage(String leaveMessage) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.leaveMessage = leaveMessage;
        });
    }

    /**
     * Get the player's votes
     * @return The player's votes
     */
    public HashMap<String, Long> getVotes() {
        return votes;
    }

    /**
     * Get a player's specified vote
     * @param vote The vote to get
     * @return Whether the player has the specified vote
     */
    public boolean hasVote(String vote) {
        return votes.containsKey(vote);
    }

    /**
     * Reset the player's votes
     */
    public void resetVotes() {
        this.votes = new HashMap<>();
    }

    /**
     * Set the specified vote to the current time
     * @param vote The vote to set
     */
    public void setVote(String vote) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            votes.put(vote, System.currentTimeMillis());
        });
    }

    /**
     * Get the player's coins
     * @return The player's coins
     */
    public double getCoins() {
        return coins;
    }

    /**
     * Set the player's coins
     * @param coins The coins to set
     */
    public void setCoins(double coins) {
        this.coins = coins;
    }

    /**
     * Add to the player's coins
     * @param coins The amount of coins to add
     */
    public void addCoins(double coins) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.coins += coins * coinMultiplier;
        });
    }

    /**
     * Add to the player's coins without taking the multiplier into account
     * @param coins The amount of coins to add
     */
    public void addCoinsClean(double coins) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            this.coins += coins;
        });
    }

    /**
     * Take from the player's coins if they have enough
     * @param amount The amount of coins to take
     * @return Whether the player had enough coins to take
     */
    public boolean takeCoins(double amount) {
        if (this.coins < amount) {
            return false;
        }
        this.coins -= amount;
        return true;
    }

    /**
     * Take from the player's coins regardless of their current balance
     * @param amount The amount of coins to take
     */
    public void takeCoinsForce(double amount) {
        this.coins -= amount;
    }

    /**
     * Check if the player currently has access to a kit
     * @param kitName The name of a premium or team kit without spaces
     * @return Whether the player has access to the kit
     */
    public boolean hasKit(String kitName) {
        return unlockedKits.contains(kitName);
    }

    /**
     * Get all the kits that the player currently has access to
     * @return The names without spaces for all kits the player currently has access to
     */
    public ArrayList<String> getUnlockedKits() {
        ArrayList<String> unlockedKits = new ArrayList<>(FreeKit.getKits());

        if (hasVote("kits")) {
            unlockedKits.addAll(VoterKit.getKits());
        }

        unlockedKits.addAll(this.unlockedKits);

        return unlockedKits;
    }

    /**
     * Add a kit to the player's unlocked kits
     * @param kitName The name without spaces of the kit to add
     */
    public void addKit(String kitName) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (!unlockedKits.contains(kitName))
                unlockedKits.add(kitName);
        });
    }

    /**
     * Remove a kit from the player's unlocked kits
     * @param kitName The name without spaces of the kit to remove
     */
    public void removeKit(String kitName) {
        while (unlockedKits.contains(kitName))
            unlockedKits.remove(kitName);
    }

    public String getSetting(String setting) {
        return settings.get(setting) == null ? SettingsCommand.defaultSettings.get(setting)[0] : settings.get(setting);
    }

    public void setSetting(UUID uuid, String setting, String value) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (settings.get(setting) == null) {
                StoreData.addSetting(uuid, setting, value);
            } else {
                StoreData.updateSetting(uuid, setting, value);
            }
            settings.put(setting, value);
        });
    }

    /**
     * Get the active coin multiplier
     * @return The active coin multiplier
     */
    public static double getCoinMultiplier() {
        return coinMultiplier;
    }

    /**
     * Sets the coin multiplier
     * @param multiplier The multiplier to set
     */
    public static void setCoinMultiplier(double multiplier) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            coinMultiplier = multiplier;
        });
    }


    /**
     * Check if the player has found a specified secret
     * @param secretName The name of a secret
     * @return true if they found it, false if they haven't found it.
     */
    public boolean hasSecret(String secretName) {
        return foundSecrets.contains(secretName);
    }

    /**
     * Get all the kits that the player currently has access to
     * @return The names without spaces for all kits the player currently has access to
     */
    public ArrayList<String> getFoundSecrets() {
        return foundSecrets;
    }

    /**
     * Add a secret to the player's found secrets
     * @param secretName The name without spaces of the secret to add
     */
    public void addFoundSecret(String secretName) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (!foundSecrets.contains(secretName))
                foundSecrets.add(secretName);
        });
    }

    /**
     * Remove a secret from the player's found secrets
     * @param secretName The name of the secret without spaces, that will be removed.
     */
    public void removeFoundSecret(String secretName) {
        while (foundSecrets.contains(secretName))
            foundSecrets.remove(secretName);
    }
}
