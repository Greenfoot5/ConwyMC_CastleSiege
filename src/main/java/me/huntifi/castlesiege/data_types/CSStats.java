package me.huntifi.castlesiege.data_types;

/**
 * Contains a player's stats for Castle Siege
 */
public class CSStats {

    public static final double HEAL_MULTIPLIER = 0.5;
    public static final double SUPPORT_MULTIPLIER = (double) 1 / 6;

    private double score;
    private double kills;
    private double deaths;
    private double captures;
    private double heals;
    private double supports;
    private double assists;
    private int killStreak;

    /**
     * Create a new set of stats at 0
     */
    public CSStats() {
        this.score = 0;
        this.kills = 0;
        this.deaths = 0;
        this.captures = 0;
        this.heals = 0;
        this.supports = 0;
        this.assists = 0;
        this.killStreak = 0;
    }

    /**
     * Creates and populates a set of CS stats
     * @param score The player's score
     * @param kills The player's kill count
     * @param deaths The player's death count
     * @param captures The players capture count
     * @param heals The player's heal count
     * @param supports The player's support count
     * @param assists The player's assist count
     */
    public CSStats(double score, double kills, double deaths, double captures, double heals, double supports, double assists) {
        this.score = score;
        this.kills = kills;
        this.deaths = deaths;
        this.captures = captures;
        this.heals = heals;
        this.supports = supports;
        this.assists = assists;
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
        resetKillStreak();
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
        this.heals += heals;
        addScore(HEAL_MULTIPLIER * heals);
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
        this.supports += supports;
        addScore(SUPPORT_MULTIPLIER * supports);
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
        assists += 1;
        addScore(1);
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
    }

    /**
     * Sets the player's kill streak
     * @param killStreak The new killStreak value
     */
    public void setKillStreak(int killStreak) {
        this.killStreak = killStreak;
    }

    private void resetKillStreak() {
        killStreak = 0;
    }
}
