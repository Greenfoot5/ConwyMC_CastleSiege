package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a player's data
 */
public class CSPlayerData extends PlayerData {

    /** All kits the player has unlocked */
    private final ArrayList<String> unlockedKits;

    /** The secrets a player has collected */
    private final ArrayList<String> foundSecrets;

    /** All-time stats
     * kill streak is a temporary stat to see if max needs updating */
    private final CSStats stats;

    /** Max all-time kill streak */
    private int maxKillStreak;
    /** Player's all-time MVPs */
    private int mvps;
    /** Amount of secrets collected */
    private int secrets;
    /** Kit the player has selected */
    private String kit;
    /** Player's current level */
    private int level;
    /** Votes player has collected and when */
    private HashMap<String, Long> votes;

    /** Any boosters the player owns */
    private final ArrayList<Booster> boosters;

    /**
     * Initialize the player's data for active data
     * @param unlockedKits The list of kit names the player has unlocked
     * @param foundSecrets A list of the keys for found secrets
     * @param mute If a player has been muted
     * @param statsData The data retrieved from player_stats
     * @param rankData The data retrieved from player_rank
     * @param votes The votes a player has made
     * @param settings The settings a player has
     * @param boosters Any boosters the player owns
     * @throws SQLException If the columns don't match up
     */
    public CSPlayerData(ArrayList<String> unlockedKits, ArrayList<String> foundSecrets, ResultSet mute, ResultSet statsData,
                        ResultSet rankData, HashMap<String, Long> votes, HashMap<String, String> settings, ArrayList<Booster> boosters) throws SQLException {
        super(statsData.getDouble("coins"), rankData, mute, settings);

        this.unlockedKits = unlockedKits;
        this.foundSecrets = foundSecrets;

        this.stats = new CSStats(statsData.getDouble("score"), statsData.getDouble("kills"),
                statsData.getDouble("deaths"), statsData.getDouble("captures"),
                statsData.getDouble("heals"), statsData.getDouble("supports"),
                statsData.getDouble("assists"));

        this.level = statsData.getInt("level");
        this.mvps = statsData.getInt("mvps");
        this.secrets = statsData.getInt("secrets");
        this.kit = statsData.getString("kit");
        this.maxKillStreak = statsData.getInt("kill_streak");

        this.boosters = boosters;

        this.votes = votes;
    }

    /**
     * Get the player's current score
     * @return The player's score
     */
    public double getScore() {
        return stats.getScore();
    }

    /**
     * Get the player's current kills
     * @return The player's kills
     */
    public double getKills() {
        return this.stats.getKills();
    }

    /**
     * Increase the player's kills by 1
     * Add 1 point and coin per kill
     * Update the current kill streak
     */
    public void addKill() {
        this.stats.addKill();
        addCoins(2);
        addKillStreak();
    }

    /**
     * Get the player's current deaths
     * @return The player's deaths
     */
    public double getDeaths() {
        return this.stats.getDeaths();
    }

    /**
     * Add to the player's deaths
     * Subtract 1 point per death
     * Reset the current kill streak
     * @param deaths The deaths to add
     */
    public void addDeaths(double deaths) {
        this.stats.addDeaths(deaths);
        resetKillStreak();
    }

    /**
     * Get the player's current captures
     * @return The player's captures
     */
    public double getCaptures() {
        return this.stats.getCaptures();
    }

    /**
     * Add to the player's captures
     * Add 1 point and coin per capture
     * @param captures The captures to add
     */
    public void addCaptures(double captures) {
        this.stats.addCaptures(captures);
        addCoins(captures);
    }

    /**
     * Get the player's current heals
     * @return The player's heals
     */
    public double getHeals() {
        return this.stats.getHeals();
    }

    /**
     * Increase the player's heals.
     * Add 0.5 points and coins per heal.
     * @param heals The amount of heals to add
     */
    public void addHeals(double heals) {
        this.stats.addHeals(heals);
        addCoins(CSStats.HEAL_MULTIPLIER * heals);
    }

    /**
     * Get the player's current supports
     * @return The player's supports
     */
    public double getSupports() {
        return this.stats.getSupports();
    }

    /**
     * Add to the player's supports
     * Add 1/6 points and coins per support
     * @param supports The supports to add
     */
    public void addSupports(double supports) {
        this.stats.addSupports(supports);
        addCoins(CSStats.SUPPORT_MULTIPLIER * supports);
    }

    /**
     * Get the player's current assists
     * @return The player's assists
     */
    public double getAssists() {
        return this.stats.getAssists();
    }

    /**
     * Increase the player's assists by 1
     * Add 1 point and coin per assist
     */
    public void addAssist() {
        this.stats.addAssist();
        addCoins(1);
    }

    /**
     * Get the player's current kill streak
     * @return The player's kill streak
     */
    public int getKillStreak() {
        return maxKillStreak;
    }

    /**
     * Increase the player's kill streak by 1
     * Update max kill streak when surpassed
     */
    private void addKillStreak() {
        maxKillStreak += 1;
        if (this.stats.getKillStreak() < maxKillStreak)
            this.stats.setKillStreak(maxKillStreak);
    }

    /**
     * Increase the player's kill streak by 1
     * Update max kill streak when surpassed
     */
    private void resetKillStreak() {
        maxKillStreak = 0;
    }

    /**
     * Get the player's highest kill streak ever achieved
     * @return The player's highest kill streak
     */
    public int getMaxKillStreak() {
        return this.stats.getKillStreak();
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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> this.mvps += 1);
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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> secrets += 1);
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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> level += 1);
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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> votes.put(vote, System.currentTimeMillis()));
    }

    /**
     * Check if the player currently has access to a kit
     * @param kitName The name of a premium or team kit without spaces
     * @return Whether the player has access to the kit
     */
    public boolean hasKit(String kitName) {
        return unlockedKits.contains(kitName) || CoinKit.boostedKits.contains(kitName);
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
     * @return A list of boosters the player currently owns
     */
    public List<Booster> getBoosters() {
        return boosters;
    }

    /**
     * Adds a booster to the player's booster inventory
     * @param booster The booster to add to the player
     */
    public void addBooster(Booster booster) {
        boosters.add(booster);
    }

    /**
     * @param uuid The UUID of the player activating the booster
     * @param booster The booster to activate
     */
    public void useBooster(UUID uuid, Booster booster) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        // Coin Booster
        if (booster instanceof CoinBooster) {
            CoinBooster coinBooster = (CoinBooster) booster;
            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                coinMultiplier += coinBooster.multiplier;
                Messenger.broadcastInfo(player.getName() + " has activated a " + coinBooster.getPercentage() + "% coin booster " +
                        "for " + booster.getDurationAsString() + "!");
                Messenger.broadcastInfo("The total coin multiplier is now " + getCoinMultiplier() + ".");
            });
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                coinMultiplier -= coinBooster.multiplier;
                Messenger.broadcastWarning(player.getName() + "'s " + coinBooster.getPercentage() + "% coin booster has expired!");
                Messenger.broadcastInfo("The total coin multiplier is now " + getCoinMultiplier() + ".");
            }, booster.duration * 20L);

        // Kit booster
        } else if (booster instanceof KitBooster) {
            KitBooster kitBooster = (KitBooster) booster;
            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                CoinKit.boostedKits.add(kitBooster.kitName);
                Messenger.broadcastInfo(player.getName() + " has activated a " + kitBooster.kitName + " kit booster " +
                        "for " + booster.getDurationAsString() + "!");
            });
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                CoinKit.boostedKits.remove(kitBooster.kitName);
                Messenger.broadcastWarning(player.getName() + "'s " + kitBooster.kitName + " kit booster has expired! ");
            }, booster.duration * 20L);
        } else {
            Main.instance.getLogger().warning("Failed to use booster " + booster.id);
            return;
        }

        boosters.remove(booster);
    }

    @Override
    public String getSetting(String name) {
        return super.getSetting(name) == null ? Objects.requireNonNull(CSSetting.getDefault(name)) : super.getSetting(name);
    }
}
