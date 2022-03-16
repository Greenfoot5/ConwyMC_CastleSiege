package me.huntifi.castlesiege.flags;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

// you need to decide what every flag has
// (capture area,
// spawn point,
// starting team,
// primary/secondary colour for each team)
public class Flag {
    public String name;
    private String startingTeam;

    // Location Data
    public Location spawnPoint;
    private final List<UUID> players;

    // Game Data
    public Team currentOwners;
    private int maxCap = 13;
    private int capTimer;

    // Capturing data
    private boolean isRunning;
    public int capStatus;
    public double capMultiplier;

    public Flag(String name, String startingTeam, int maxCapValue, int capTimer) {
        this.name = name;
        this.startingTeam = startingTeam;
        this.maxCap = maxCapValue;
        this.capTimer = capTimer;
        this.players = new ArrayList<>();
    }

    /**
     * Attempts to change the team holding the flag.
     * @param newTeam The new flag owners
     * @return true if the team was changed, false if invalid team
     */
    private boolean changeTeam(Team newTeam) {
        if (currentOwners == null && newTeam != null)
        {
            currentOwners = newTeam;
            return true;
        }
        else if (currentOwners != null && newTeam == null)
        {
            currentOwners = null;
            return true;
        }
        // Invalid team change
        return false;
    }

    private void captureFlag() {
        int ownerCount = 0;
        int attackerCount = 0;

        HashMap<String, Integer> counts = getPlayerCounts();
        for (String name : counts.keySet()) {
            if (currentOwners.name.equals(name)) {
                ownerCount = counts.get(name);
            } else {
                attackerCount += counts.get(name);
            }
        }

        // TODO - Animate it
    }

    /**
     * Gets the message to send to the user when they spawn in
     * @return the message to send
     */
    public String getSpawnMessage() {
        return currentOwners.secondaryChatColor + "Spawning at:" + currentOwners.primaryChatColor + " " + name;
    }

    /**
     * Called when a player enters the capture zone
     * @param player the player that entered
     */
    public void playerEnter(Player player) {
        System.out.println("Player entered");
        players.add(player.getUniqueId());
    }

    /**
     * Called when a player leaves the capture zone
     * @param player the player that exited
     */
    public void playerExit(Player player) {
        System.out.println(player.getDisplayName() + " left " + name);
        players.add(player.getUniqueId());
    }

    /**
     * Handles the capturing of a flag
     */
    private void capturing() {
        // Only one loop can run at a time
        if (isRunning) {
            return;
        }
        isRunning = true;

        // Keep running as long as there are players in the area
        while (players.size() > 0) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    captureFlag();
                    if (players.size() <= 0) {
                        isRunning = false;
                    }
                }
            }.runTaskLater(Main.plugin, getTimer());
        }

        isRunning = false;
    }

    private int getTimer() {
        if (currentOwners == null) {
            return capTimer;
        }

        int ownerCount = 0;
        int attackerCount = 0;

        HashMap<String, Integer> counts = getPlayerCounts();
        for (String name : counts.keySet()) {
            if (currentOwners.name.equals(name)) {
                ownerCount = counts.get(name);
            } else {
                attackerCount += counts.get(name);
            }
        }

        int timer = (int) (capTimer * Math.pow(capMultiplier, ownerCount - attackerCount));
        if (timer < 0) {
            timer = (int) (capTimer * Math.pow(capMultiplier, attackerCount - ownerCount));
        }

        return Math.max(timer, 40);
    }

    public boolean underAttack() {
        if (!isRunning) {
            return false;
        }

        int ownerCount = 0;
        int attackerCount = 0;

        HashMap<String, Integer> counts = getPlayerCounts();
        for (String name : counts.keySet()) {
            if (currentOwners.name.equals(name)) {
                ownerCount = counts.get(name);
            } else {
                attackerCount += counts.get(name);
            }
        }

        return ownerCount > attackerCount;
    }

    private HashMap<String, Integer> getPlayerCounts() {
        HashMap<String, Integer> counts = new HashMap<>();

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                String team = MapController.getCurrentMap().getTeam(uuid).name;
                if (counts.get(team) != null) {
                    counts.put(team, 1);
                } else {
                    counts.put(team, counts.get(team) + 1);
                }
            }
        }

        return counts;
    }
}