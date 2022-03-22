package me.huntifi.castlesiege.flags;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Flag {
    public String name;
    private String startingTeam;

    // Location Data
    public Location spawnPoint;
    private final List<UUID> players;

    // Game Data
    public String currentOwners;
    private final int maxCap;
    private final int progressAmount;
    private int progress;
    private final static int progressMultiplier = 100;
    public final static double capMultiplier = 1.1;

    // Capturing data
    private AtomicInteger isRunning;
    public int animationIndex;

    public Frame[] animation;

    private Plugin plugin;

    public Flag(String name, String startingTeam, int maxCapValue, int progressAmount) {
        this.name = name;
        this.startingTeam = startingTeam;
        this.currentOwners = startingTeam;
        this.maxCap = maxCapValue;
        animationIndex = maxCapValue;
        this.progressAmount = progressAmount;
        this.players = new ArrayList<>();
        progress = progressMultiplier * maxCapValue;
        isRunning = new AtomicInteger(0);
        plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
    }

    /**
     * Attempts to change the team holding the flag.
     * @param newTeam The new flag owners
     */
    private void changeTeam(String newTeam) {
        // TODO - Notify all players of the change
        if (currentOwners == null && newTeam != null)
        {
            currentOwners = newTeam;
        }
        else if (currentOwners != null && newTeam == null)
        {
            currentOwners = null;
        }
    }

    private void captureFlag() {
        int capProgress = progress / progressMultiplier;

        if (progress == 0) {
            System.out.println("Neutral Flag");
            // Flag became neutral
            changeTeam(null);
            animationIndex = 0;

            // TODO - Notify cappers
            // TODO - Animate

        } else if (capProgress == 1 && animationIndex == 0) {
            System.out.println("Captured Flag");
            changeTeam(currentOwners);
            animationIndex += 1;

            // TODO - Notify cappers
            // TODO - Animate

        } else if (capProgress > animationIndex) {
            System.out.println("Cap Up");
            // Cap Up
            if (animationIndex >= maxCap) {
                return;
            }

            animationIndex += 1;

            // TODO - Increase the animation
            // TODO - Notify cappers

            if (animationIndex == maxCap) {
                // TODO - Notify cappers
                System.out.println("Max Cap");
            }

        } else if (capProgress < animationIndex) {
            System.out.println("Cap Down!");
            // Cap down
            animationIndex -= 1;

            // TODO - Increase the animation
            // TODO - Notify cappers

        } else {
            System.out.println("else");
            // Equal, no capping done
            // TODO - Notify cappers
        }
    }

    /**
     * Gets the message to send to the user when they spawn in
     * @return the message to send
     */
    public String getSpawnMessage() {
        Team team = MapController.getCurrentMap().getTeam(currentOwners);
        return team.secondaryChatColor + "Spawning at:" + team.primaryChatColor + " " + name;
    }

    /**
     * Called when a player enters the capture zone
     * @param player the player that entered
     */
    public void playerEnter(Player player) {
        players.add(player.getUniqueId());
        capturing();
    }

    /**
     * Called when a player leaves the capture zone
     * @param player the player that exited
     */
    public void playerExit(Player player) {
        players.remove(player.getUniqueId());
    }

    /**
     * Handles the capturing of a flag
     */
    private synchronized void capturing() {
        // Only one loop can run at a time
        if (isRunning.get() > 0) {
            return;
        }
        isRunning.incrementAndGet();

        // Keep running as long as there are players in the area
        if (players.size() > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    captureProgress();

                    if (progress / progressMultiplier != animationIndex || progress < progressMultiplier)
                        captureFlag();

                    // No more players in the zone
                    if (players.size() <= 0 || isRunning.get() > 1) {
                        isRunning.decrementAndGet();
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(Main.plugin, 10, 10);
        } else {
            isRunning.decrementAndGet();
        }
    }

    private synchronized void captureProgress() {
        if (currentOwners == null) {
            currentOwners = getLargestTeam();
        }

        Tuple<Integer, Integer> counts = getPlayerCounts();

        int amount = 69;
        if (counts.getFirst() > counts.getSecond()) {
            amount = (int) (progressAmount * Math.pow(capMultiplier, counts.getFirst() - counts.getSecond() - 1));
            progress += Math.min(amount, 25);
        } else if (counts.getSecond() > counts.getFirst()) {
            amount = (int) (progressAmount * Math.pow(capMultiplier, counts.getSecond() - counts.getFirst() - 1));
            progress -= Math.min(amount, 25);
        }

        if (progress < 0) {
            progress = 0;
        } else if (progress > maxCap * progressMultiplier) {
            progress = maxCap * progressMultiplier;
        }
    }

    public boolean underAttack() {
        if (isRunning.intValue() <= 0) {
            return false;
        }

        Tuple<Integer, Integer> counts = getPlayerCounts();

        return counts.getFirst() > counts.getSecond();
    }

    private Tuple<Integer, Integer> getPlayerCounts() {
        Tuple<Integer, Integer> counts = new Tuple<>(0, 0);

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                String team = MapController.getCurrentMap().getTeam(uuid).name;
                if (currentOwners.equals(team)) {
                    counts.setFirst(counts.getFirst() + 1);
                } else {
                    counts.setSecond(counts.getSecond() + 1);
                }
            }
        }

        return counts;
    }

    private String getLargestTeam() {
        HashMap<String, Integer> teamCounts = new HashMap<>();
        String largestTeam = null;

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                // Add the player to the team counts
                String team = MapController.getCurrentMap().getTeam(uuid).name;
                if (teamCounts.get(name) == null) {
                    teamCounts.put(team, 1);
                } else {
                    teamCounts.put(name, teamCounts.get(name) + 1);
                }

                // Get the largest team
                if (largestTeam == null || largestTeam.equals(team)) {
                    largestTeam = team;
                } else if (teamCounts.get(largestTeam) > teamCounts.get(team)) {
                    largestTeam = team;
                }
            }
        }

        return largestTeam;
    }
}